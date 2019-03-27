package com.tokopedia.flight.cancellation.view.presenter;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.cancellation.domain.model.AttachmentImageModel;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationReasonAndProofContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonAndAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by alvarisi on 3/26/18.
 */

public class FlightCancellationReasonAndProofPresenter extends BaseDaggerPresenter<FlightCancellationReasonAndProofContract.View>
        implements FlightCancellationReasonAndProofContract.Presenter {
    private static final String DEFAULT_RESOLUTION = "100-square";
    private static final String RESOLUTION_300 = "300";
    private static final String PARAM_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String PARAM_RESOLUTION = "param_resolution";
    private static final String DEFAULT_UPLOAD_PATH = "/upload/attachment";
    private static final String DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg";
    private static final int MAX_FILE_SIZE = 15360;
    private static final int MINIMUM_HEIGHT = 100;
    private static final int MINIMUM_WIDTH = 300;
    private static final long DEFAULT_ONE_MEGABYTE = 1024;

    private UploadImageUseCase<AttachmentImageModel> uploadImageUseCase;
    private CompositeSubscription compositeSubscription;
    private UserSessionInterface userSession;
    private FlightModuleRouter flightModuleRouter;

    @Inject
    public FlightCancellationReasonAndProofPresenter(UploadImageUseCase<AttachmentImageModel> uploadImageUseCase,
                                                     UserSessionInterface userSession,
                                                     FlightModuleRouter flightModuleRouter) {
        this.uploadImageUseCase = uploadImageUseCase;
        this.userSession = userSession;
        this.flightModuleRouter = flightModuleRouter;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initialize(List<FlightCancellationAttachmentViewModel> attachments) {
        if (attachments != null && attachments.size() > 0) {
            getView().addAttachments(attachments);
        }
    }

    @Override
    public List<FlightCancellationAttachmentViewModel> buildAttachmentList() {
        List<FlightCancellationAttachmentViewModel> attachmentViewModelList = new ArrayList<>();

        for (String passenger : getUniquePassenger(getView().getCancellationViewModel())) {
            FlightCancellationAttachmentViewModel item = new FlightCancellationAttachmentViewModel();
            item.setPassengerName(passenger);
            item.setPercentageUpload(0);

            attachmentViewModelList.add(item);
        }

        return attachmentViewModelList;
    }

    @Override
    public void setNextButton() {
        boolean shouldEnableNextButton = true;

        if (getView().getReason() == null) {
            shouldEnableNextButton = false;
        } else if (getView().getReason().getRequiredDocs().size() > 0) {
            for (FlightCancellationAttachmentViewModel item : getView().getAttachments()) {
                if (item.getFilename() == null || item.getFilepath() == null ||
                        item.getFilename().length() <= 0 || item.getFilepath().length() <= 0) {
                    shouldEnableNextButton = false;
                    break;
                }
            }
        }

        if (shouldEnableNextButton) {
            getView().enableNextButton();
        } else {
            getView().disableNextButton();
        }
    }

    private boolean validateImageAttachment(String uri) {
        if (uri == null) return false;
        File file = new File(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        int fileSize = Integer.parseInt(String.valueOf(file.length() / DEFAULT_ONE_MEGABYTE));

        if (imageHeight < MINIMUM_HEIGHT || imageWidth < MINIMUM_WIDTH) {
            getView().showAttachmentMinDimensionErrorMessage(R.string.flight_cancellation_min_dimension_error);
            return false;
        } else if (fileSize >= MAX_FILE_SIZE) {
            getView().showAttachmentMaxSizeErrorMessage(R.string.flight_cancellation_max_error);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onSuccessGetImage(String filepath, int position) {
        if (validateImageAttachment(filepath)) {
            FlightCancellationAttachmentViewModel viewModel = getView().getAttachments().get(position);
            viewModel.setFilepath(filepath);
            viewModel.setFilename(Uri.parse(filepath).getLastPathSegment());

            getView().setAttachment(viewModel, position);
            getView().renderAttachment();
        }
    }

    @Override
    public void onNextButtonClicked() {
        if (checkIfAttachmentMandatory()) {
            List<FlightCancellationAttachmentViewModel> attachments = getView().getAttachments();
            Observable<Boolean> isRequiredAttachment = Observable.just(checkIfAttachmentMandatory());
            Observable<Boolean> isValidRequiredAttachment = Observable.zip(Observable.just(attachments), isRequiredAttachment, new Func2<List<FlightCancellationAttachmentViewModel>, Boolean, Boolean>() {
                @Override
                public Boolean call(List<FlightCancellationAttachmentViewModel> flightCancellationAttachmentViewModels, Boolean aBoolean) {
                    return !aBoolean || (aBoolean && flightCancellationAttachmentViewModels.size() > 0);
                }
            });

            isValidRequiredAttachment.subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (!aBoolean)
                        getView().showRequiredMinimalOneAttachmentErrorMessage(R.string.flight_cancellation_attachment_required_error_message);
                }
            });

            Observable<Boolean> isValidAttachmentLength = Observable.just(getView().getCancellationViewModel())
                    .map(new Func1<FlightCancellationWrapperViewModel, Integer>() {
                        @Override
                        public Integer call(FlightCancellationWrapperViewModel flightCancellationWrapperViewModel) {
                            return calculateTotalPassenger(getView().getCancellationViewModel());
                        }
                    }).zipWith(isRequiredAttachment, new Func2<Integer, Boolean, Boolean>() {
                        @Override
                        public Boolean call(Integer integer, Boolean aBoolean) {
                            return !aBoolean || (aBoolean && integer > 0);
                        }
                    });

            isValidAttachmentLength.subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (!aBoolean) {
                        int totalPassenger = calculateTotalPassenger(getView().getCancellationViewModel());
                        getView().showAttachmentGreaterThanPassengersTotalAndRequiredAttachmentErrorMessage(
                                String.format(
                                        getView().getString(R.string.flight_cancellation_attachment_more_than_max_error_message), totalPassenger + 1)
                        );
                    }
                }
            });

            Observable.combineLatest(
                    isValidRequiredAttachment, isValidAttachmentLength, new Func2<Boolean, Boolean, Boolean>() {
                        @Override
                        public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                            return aBoolean && aBoolean2;
                        }
                    }
            ).subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean) {
                        actionUploadImageAndBuildModel();
                    }
                }
            });
        } else {
            getView().hideProgressBar();
            getView().navigateToNextStep(buildCancellationWrapperModel(getView().getAttachments()));
        }
    }

    @NonNull
    private Boolean checkIfAttachmentMandatory() {
        return getView().getReason().getRequiredDocs().size() > 0 && getView().getAttachments() != null
                && getView().getAttachments().size() > 0;
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }

    @Override
    public void onComeFromEstimateRefundScreen() {
        getView().hideProgressBar();
    }

    private void actionUploadImageAndBuildModel() {
        getView().showProgressBar();

        List<FlightCancellationAttachmentViewModel> attachments = getView().getAttachments();
        compositeSubscription.add(Observable.from(attachments)
                .flatMap(new Func1<FlightCancellationAttachmentViewModel, Observable<FlightCancellationAttachmentViewModel>>() {
                    @Override
                    public Observable<FlightCancellationAttachmentViewModel> call(FlightCancellationAttachmentViewModel attachmentViewModel) {
                        return Observable.zip(Observable.just(attachmentViewModel),
                                uploadImageUseCase.createObservable(
                                        createParam(attachmentViewModel.getFilepath())
                                ), new Func2<FlightCancellationAttachmentViewModel, ImageUploadDomainModel<AttachmentImageModel>, FlightCancellationAttachmentViewModel>() {
                                    @Override
                                    public FlightCancellationAttachmentViewModel call(FlightCancellationAttachmentViewModel attachmentViewModel, ImageUploadDomainModel<AttachmentImageModel> uploadDomainModel) {
                                        String url = uploadDomainModel.getDataResultImageUpload().getData().getPicSrc();
                                        if (url.contains(DEFAULT_RESOLUTION)) {
                                            url = url.replaceFirst(DEFAULT_RESOLUTION, RESOLUTION_300);
                                        }
                                        attachmentViewModel.setImageurl(url);
                                        return attachmentViewModel;
                                    }
                                });
                    }
                })
                .toList()
                .map(new Func1<List<FlightCancellationAttachmentViewModel>, FlightCancellationWrapperViewModel>() {
                    @Override
                    public FlightCancellationWrapperViewModel call(List<FlightCancellationAttachmentViewModel> flightCancellationAttachmentViewModels) {
                        return buildCancellationWrapperModel(flightCancellationAttachmentViewModels);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FlightCancellationWrapperViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached() && !isUnsubscribed()) {
                            getView().hideProgressBar();
                            getView().showFailedToNextStepErrorMessage(ErrorHandler.getErrorMessage(getView().getActivity(), e));
                        }
                    }

                    @Override
                    public void onNext(FlightCancellationWrapperViewModel viewModel) {
                        getView().hideProgressBar();
                        getView().navigateToNextStep(viewModel);
                    }
                })
        );
    }

    private List<String> getUniquePassenger(FlightCancellationWrapperViewModel cancellationWrapperViewModel) {
        List<String> uniquePassengers = new ArrayList<>();

        for (FlightCancellationViewModel viewModel : cancellationWrapperViewModel.getGetCancellations()) {
            for (FlightCancellationPassengerViewModel passengerViewModel : viewModel.getPassengerViewModelList()) {
                if (!uniquePassengers.contains(getPassengerName(passengerViewModel)) &&
                        passengerViewModel.getType() == FlightBookingPassenger.ADULT) {
                    uniquePassengers.add(getPassengerName(passengerViewModel));
                }
            }
        }

        return uniquePassengers;
    }

    private int calculateTotalPassenger(FlightCancellationWrapperViewModel cancellationViewModel) {
        List<String> uniquePassengers = new ArrayList<>();
        for (FlightCancellationViewModel viewModel : cancellationViewModel.getGetCancellations()) {
            for (FlightCancellationPassengerViewModel passengerViewModel : viewModel.getPassengerViewModelList()) {
                if (!uniquePassengers.contains(passengerViewModel.getPassengerId()) &&
                        passengerViewModel.getType() == FlightBookingPassenger.ADULT) {
                    uniquePassengers.add(passengerViewModel.getPassengerId());
                }
            }
        }
        return uniquePassengers.size();
    }

    private String getPassengerName(FlightCancellationPassengerViewModel passengerViewModel) {
        String name = "";

        name += passengerViewModel.getTitleString() + " ";
        name += passengerViewModel.getFirstName().substring(0, 1).toUpperCase() +
                passengerViewModel.getFirstName().substring(1) + " ";
        name += passengerViewModel.getLastName().substring(0, 1).toUpperCase() +
                passengerViewModel.getLastName().substring(1);

        return name;
    }

    private RequestParams createParam(String cameraLoc) {
        File photo = flightModuleRouter.writeImage(cameraLoc, 100);
        Map<String, RequestBody> maps = new HashMap<String, RequestBody>();
        RequestBody webService = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), RESOLUTION_300);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userSession.getUserId() + UUID.randomUUID() + System.currentTimeMillis());
        maps.put(PARAM_WEB_SERVICE, webService);
        maps.put(PARAM_ID, id);
        maps.put(PARAM_RESOLUTION, resolution);
        return uploadImageUseCase.createRequestParam(cameraLoc, DEFAULT_UPLOAD_PATH, DEFAULT_UPLOAD_TYPE, maps);
    }

    private FlightCancellationWrapperViewModel buildCancellationWrapperModel(List<FlightCancellationAttachmentViewModel> flightCancellationAttachmentViewModels) {
        FlightCancellationWrapperViewModel viewModel = getView().getCancellationViewModel();
        FlightCancellationReasonAndAttachmentViewModel reasonAndAttachmentViewModel = new FlightCancellationReasonAndAttachmentViewModel();
        reasonAndAttachmentViewModel.setReasonId(getView().getReason().getId());
        reasonAndAttachmentViewModel.setReason(getView().getReason().getDetail());
        if (checkIfAttachmentMandatory()) {
            reasonAndAttachmentViewModel.setAttachments(flightCancellationAttachmentViewModels);
        } else {
            reasonAndAttachmentViewModel.setAttachments(new ArrayList<>());
        }
        viewModel.setCancellationReasonAndAttachment(reasonAndAttachmentViewModel);
        return viewModel;
    }
}
