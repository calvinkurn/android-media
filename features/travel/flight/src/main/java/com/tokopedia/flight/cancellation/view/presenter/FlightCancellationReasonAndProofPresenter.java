package com.tokopedia.flight.cancellation.view.presenter;

import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationAttachmentUploadEntity;
import com.tokopedia.flight.cancellation.domain.FlightCancellationAttachmentUploadUseCase;
import com.tokopedia.flight.cancellation.domain.model.AttachmentImageModel;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationReasonAndProofContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerAttachmentModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonAndAttachmentModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperModel;
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.network.utils.ErrorHandler;
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
    private FlightCancellationAttachmentUploadUseCase flightCancellationAttachmentUploadUseCase;

    @Inject
    public FlightCancellationReasonAndProofPresenter(UploadImageUseCase<AttachmentImageModel> uploadImageUseCase,
                                                     UserSessionInterface userSession,
                                                     FlightCancellationAttachmentUploadUseCase flightCancellationAttachmentUploadUseCase) {
        this.uploadImageUseCase = uploadImageUseCase;
        this.userSession = userSession;
        this.flightCancellationAttachmentUploadUseCase = flightCancellationAttachmentUploadUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initialize(List<FlightCancellationAttachmentModel> attachments) {
        if (attachments != null && attachments.size() > 0) {
            getView().addAttachments(attachments);
        }
    }

    @Override
    public List<FlightCancellationAttachmentModel> buildAttachmentList() {
        List<FlightCancellationAttachmentModel> attachmentViewModelList = new ArrayList<>();

        for (FlightCancellationPassengerAttachmentModel passenger : getUniquePassenger(getView().getCancellationViewModel())) {
            String[] passengerRelations = passenger.getPassengerRelation().split("-");
            attachmentViewModelList.add(new FlightCancellationAttachmentModel(
                    "",
                    "",
                    passenger.getPassengerId(),
                    passenger.getPassengerName(),
                    passenger.getPassengerRelation(),
                    passengerRelations[0],
                    0,
                    0,
                    false));
        }

        return attachmentViewModelList;
    }

    @Override
    public List<FlightCancellationAttachmentModel> buildViewAttachmentList(int docType) {
        List<FlightCancellationAttachmentModel> viewAttachmentList = new ArrayList<>();

        for (FlightCancellationAttachmentModel item : getView().getAttachments()) {
            item.setDocType(docType);
            if (!viewAttachmentList.contains(item)) {
                viewAttachmentList.add(item);
            }
        }

        return viewAttachmentList;
    }

    @Override
    public void setNextButton() {
        boolean shouldEnableNextButton = true;

        if (getView().getReason() == null) {
            shouldEnableNextButton = false;
        } else if (getView().getReason().getRequiredDocs().size() > 0) {
            for (FlightCancellationAttachmentModel item : getView().getViewAttachments()) {
                if (item.getFilename().length() <= 0 || item.getFilepath().length() <= 0) {
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
            getView().showAttachmentMinDimensionErrorMessage(com.tokopedia.flight.R.string.flight_cancellation_min_dimension_error);
            return false;
        } else if (fileSize >= MAX_FILE_SIZE) {
            getView().showAttachmentMaxSizeErrorMessage(com.tokopedia.flight.R.string.flight_cancellation_max_error);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onSuccessGetImage(String filepath, int position) {
        if (validateImageAttachment(filepath)) {
            FlightCancellationAttachmentModel viewModel = getView().getAttachments().get(position);
            viewModel.setFilepath(filepath);
            viewModel.setFilename(Uri.parse(filepath).getLastPathSegment());
            viewModel.setUploaded(false);

            getView().setAttachment(viewModel, position);
            getView().renderAttachment();
        }
    }

    @Override
    public void onNextButtonClicked() {
        if (checkIfAttachmentMandatory()) {
            List<FlightCancellationAttachmentModel> attachments = buildAttachmentsForUpload(
                    getView().getAttachments(), getView().getViewAttachments());
            Observable<Boolean> isRequiredAttachment = Observable.just(checkIfAttachmentMandatory());
            Observable<Boolean> isValidRequiredAttachment = Observable.zip(Observable.just(attachments), isRequiredAttachment,
                    (flightCancellationAttachmentViewModels, aBoolean) -> !aBoolean || (aBoolean && flightCancellationAttachmentViewModels.size() > 0));

            isValidRequiredAttachment.subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (!aBoolean)
                        getView().showRequiredMinimalOneAttachmentErrorMessage(com.tokopedia.flight.R.string.flight_cancellation_attachment_required_error_message);
                }
            });

            Observable<Boolean> isValidAttachmentLength = Observable.just(getView().getCancellationViewModel())
                    .map(new Func1<FlightCancellationWrapperModel, Integer>() {
                        @Override
                        public Integer call(FlightCancellationWrapperModel flightCancellationWrapperViewModel) {
                            return calculateTotalPassenger(getView().getCancellationViewModel());
                        }
                    }).zipWith(isRequiredAttachment, new Func2<Integer, Boolean, Boolean>() {
                        @Override
                        public Boolean call(Integer integer, Boolean aBoolean) {
                            return !aBoolean || (aBoolean && integer > 0);
                        }
                    });

            isValidAttachmentLength.subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (!aBoolean) {
                        int totalPassenger = calculateTotalPassenger(getView().getCancellationViewModel());
                        getView().showAttachmentGreaterThanPassengersTotalAndRequiredAttachmentErrorMessage(
                                String.format(
                                        getView().getString(com.tokopedia.flight.R.string.flight_cancellation_attachment_more_than_max_error_message), totalPassenger + 1)
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
            getView().navigateToNextStep(buildCancellationWrapperModel());
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

        List<FlightCancellationAttachmentModel> attachments = buildUnUploadedAttachments(buildAttachmentsForUpload(
                getView().getAttachments(), getView().getViewAttachments()));
        String invoiceId = getView().getCancellationViewModel().getInvoice();

        compositeSubscription.add(Observable.from(attachments)
                .flatMap(new Func1<FlightCancellationAttachmentModel, Observable<FlightCancellationAttachmentModel>>() {
                    @Override
                    public Observable<FlightCancellationAttachmentModel> call(FlightCancellationAttachmentModel flightCancellationAttachmentModel) {
                        return Observable.zip(Observable.just(flightCancellationAttachmentModel),
                                flightCancellationAttachmentUploadUseCase.createObservable(
                                        flightCancellationAttachmentUploadUseCase.createRequestParams(
                                                flightCancellationAttachmentModel.getFilepath(),
                                                invoiceId,
                                                flightCancellationAttachmentModel.getJourneyId(),
                                                flightCancellationAttachmentModel.getPassengerId(),
                                                flightCancellationAttachmentModel.getDocType()
                                        )
                                ), new Func2<FlightCancellationAttachmentModel, CancellationAttachmentUploadEntity, FlightCancellationAttachmentModel>() {
                                    @Override
                                    public FlightCancellationAttachmentModel call(FlightCancellationAttachmentModel flightCancellationAttachmentModel, CancellationAttachmentUploadEntity cancellationAttachmentUploadEntity) {
                                        flightCancellationAttachmentModel.setUploaded(cancellationAttachmentUploadEntity.getAttributes().isUploaded());
                                        return flightCancellationAttachmentModel;
                                    }
                                });
                    }
                })
                .toList()
                .map(new Func1<List<FlightCancellationAttachmentModel>, FlightCancellationWrapperModel>() {
                    @Override
                    public FlightCancellationWrapperModel call(List<FlightCancellationAttachmentModel> flightCancellationAttachmentModels) {
                        for (FlightCancellationAttachmentModel item : getView().getAttachments()) {
                            int indexOfItem = flightCancellationAttachmentModels.indexOf(item);
                            if (indexOfItem != -1) {
                                item.setUploaded(flightCancellationAttachmentModels.get(indexOfItem).isUploaded());
                            }
                        }

                        return buildCancellationWrapperModel();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FlightCancellationWrapperModel>() {
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
                    public void onNext(FlightCancellationWrapperModel viewModel) {
                        getView().hideProgressBar();
                        if (checkIfAllAttachmentsUploaded()) {
                            getView().navigateToNextStep(viewModel);
                        }
                    }
                })
        );
    }

    private List<FlightCancellationPassengerAttachmentModel> getUniquePassenger(FlightCancellationWrapperModel cancellationWrapperViewModel) {
        List<FlightCancellationPassengerAttachmentModel> uniquePassengers = new ArrayList<>();

        for (FlightCancellationModel viewModel : cancellationWrapperViewModel.getGetCancellations()) {
            for (FlightCancellationPassengerModel passengerViewModel : viewModel.getPassengerViewModelList()) {
                if (!uniquePassengers.contains(passengerViewModel) &&
                        passengerViewModel.getType() == FlightBookingPassenger.ADULT) {
                    uniquePassengers.add(new FlightCancellationPassengerAttachmentModel(
                            passengerViewModel.getPassengerId(),
                            getPassengerName(passengerViewModel),
                            passengerViewModel.getRelationId()
                    ));
                }
            }
        }

        return uniquePassengers;
    }

    private int calculateTotalPassenger(FlightCancellationWrapperModel cancellationViewModel) {
        List<String> uniquePassengers = new ArrayList<>();
        for (FlightCancellationModel viewModel : cancellationViewModel.getGetCancellations()) {
            for (FlightCancellationPassengerModel passengerViewModel : viewModel.getPassengerViewModelList()) {
                if (!uniquePassengers.contains(passengerViewModel.getPassengerId()) &&
                        passengerViewModel.getType() == FlightBookingPassenger.ADULT) {
                    uniquePassengers.add(passengerViewModel.getPassengerId());
                }
            }
        }
        return uniquePassengers.size();
    }

    private String getPassengerName(FlightCancellationPassengerModel passengerViewModel) {
        String name = "";

        name += passengerViewModel.getTitleString() + " ";
        name += passengerViewModel.getFirstName().substring(0, 1).toUpperCase() +
                passengerViewModel.getFirstName().substring(1) + " ";
        name += passengerViewModel.getLastName().substring(0, 1).toUpperCase() +
                passengerViewModel.getLastName().substring(1);

        return name;
    }

    private RequestParams createParam(String cameraLoc) {
        Map<String, RequestBody> maps = new HashMap<>();
        RequestBody webService = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), RESOLUTION_300);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userSession.getUserId() + UUID.randomUUID() + System.currentTimeMillis());
        maps.put(PARAM_WEB_SERVICE, webService);
        maps.put(PARAM_ID, id);
        maps.put(PARAM_RESOLUTION, resolution);
        return uploadImageUseCase.createRequestParam(cameraLoc, DEFAULT_UPLOAD_PATH, DEFAULT_UPLOAD_TYPE, maps);
    }

    private FlightCancellationWrapperModel buildCancellationWrapperModel() {
        FlightCancellationWrapperModel viewModel = getView().getCancellationViewModel();
        FlightCancellationReasonAndAttachmentModel reasonAndAttachmentViewModel = new FlightCancellationReasonAndAttachmentModel();
        reasonAndAttachmentViewModel.setReasonId(getView().getReason().getId());
        reasonAndAttachmentViewModel.setReason(getView().getReason().getDetail());
        reasonAndAttachmentViewModel.setAttachments(getView().getAttachments());
        viewModel.setCancellationReasonAndAttachment(reasonAndAttachmentViewModel);
        return viewModel;
    }

    private List<FlightCancellationAttachmentModel> buildAttachmentsForUpload(
            List<FlightCancellationAttachmentModel> allAttachments,
            List<FlightCancellationAttachmentModel> viewAttachments) {

        List<FlightCancellationAttachmentModel> uploadAttachments = new ArrayList<>();

        for (FlightCancellationAttachmentModel item : allAttachments) {
            int indexInView = viewAttachments.indexOf(item);
            if (indexInView != -1) {
                FlightCancellationAttachmentModel selectedViewAttachment = viewAttachments.get(indexInView);
                item.setFilename(selectedViewAttachment.getFilename());
                item.setFilepath(selectedViewAttachment.getFilepath());
                item.setDocType(selectedViewAttachment.getDocType());
                item.setUploaded(selectedViewAttachment.isUploaded());

                uploadAttachments.add(item);
            }
        }

        return uploadAttachments;
    }

    private List<FlightCancellationAttachmentModel> buildUnUploadedAttachments(List<FlightCancellationAttachmentModel> attachmentModelList) {
        List<FlightCancellationAttachmentModel> unUploadedAttachments = new ArrayList<>();

        for (FlightCancellationAttachmentModel item : attachmentModelList) {
            if (!item.isUploaded()) {
                unUploadedAttachments.add(item);
            }
        }

        return unUploadedAttachments;
    }

    private boolean checkIfAllAttachmentsUploaded() {
        boolean isAllUploaded = true;

        for (FlightCancellationAttachmentModel item : getView().getAttachments()) {
            if (!item.isUploaded()) {
                isAllUploaded = false;
            }
        }

        return isAllUploaded;
    }
}
