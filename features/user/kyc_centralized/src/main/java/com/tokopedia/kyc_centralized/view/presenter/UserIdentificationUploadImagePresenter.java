package com.tokopedia.kyc_centralized.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.user_identification_common.domain.usecase.UploadUserIdentificationUseCase;
import com.tokopedia.user_identification_common.util.SchedulerProvider;
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage;
import com.tokopedia.kyc_centralized.view.model.AttachmentImageModel;
import com.tokopedia.kyc_centralized.view.model.ImageUploadModel;
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel;

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
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by alvinatin on 21/11/18.
 */

public class UserIdentificationUploadImagePresenter extends
        BaseDaggerPresenter<UserIdentificationUploadImage.View>
        implements UserIdentificationUploadImage.Presenter {

    private static final String RESOLUTION_300 = "300";
    private static final String PARAM_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String PARAM_USER_ID = "userId"; //override for imageuploader
    private static final String PARAM_RESOLUTION = "param_resolution";
    private static final String DEFAULT_UPLOAD_PATH = "/kyc/upload";
    private static final String DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg";
    private CompositeSubscription compositeSubscription;

    private final UploadImageUseCase<AttachmentImageModel> uploadImageUseCase;
    private final SchedulerProvider schedulerProvider;
    private final UserSessionInterface userSession;

    @Inject
    public UserIdentificationUploadImagePresenter(UploadImageUseCase<AttachmentImageModel> uploadImageUseCase,
                                                  UserSessionInterface userSession,
                                                  CompositeSubscription compositeSubscription,
                                                  SchedulerProvider schedulerProvider) {
        this.uploadImageUseCase = uploadImageUseCase;
        this.userSession = userSession;
        this.compositeSubscription = compositeSubscription;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Observable<ImageUploadModel> uploadImageUseCase(ImageUploadModel imageUploadModel) {
        return Observable.zip(Observable.just(imageUploadModel),
                uploadImageUseCase.createObservable(createParam(imageUploadModel.getFilePath())),
                (imageUploadModel1, uploadDomainModel) -> {
                    imageUploadModel1.setPicObjKyc(uploadDomainModel
                            .getDataResultImageUpload().getPictureObj());
                    return imageUploadModel1;
                });
    }

    @Override
    public void uploadImage(UserIdentificationStepperModel model, int projectId) {
        List<ImageUploadModel> attachments = parseToModel(model);
        compositeSubscription.add(Observable.from(attachments)
                .flatMap(new Func1<ImageUploadModel, Observable<ImageUploadModel>>() {
                    @Override
                    public Observable<ImageUploadModel> call(ImageUploadModel imageUploadModel) {
                        return uploadImageUseCase(imageUploadModel);
                    }
                }).toList()
                .subscribeOn(schedulerProvider.io())
                .unsubscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(new Subscriber<List<ImageUploadModel>>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().onErrorUpload(ErrorHandler.getErrorMessage(getView().getGetContext(), throwable));
                    }

                    @Override
                    public void onNext(List<ImageUploadModel> imageUploadModels) {
                        for (ImageUploadModel imageUpload: imageUploadModels) {
                            if(imageUpload != null) {
                                getView().onSuccessUpload(
                                        imageUpload.getKycType(),
                                        imageUpload.getPicObjKyc(),
                                        projectId);
                            } else {
                                getView().onErrorUpload(
                                        String.format(getView().getGetContext().getString(R.string.error_upload_image_kyc),
                                                KYCConstant.ERROR_UPLOAD_IMAGE)
                                );
                            }
                        }
                    }
                })
        );
    }

    @Override
    public RequestParams createParam(String cameraLoc) {
        Map<String, RequestBody> maps = new HashMap<String, RequestBody>();
        RequestBody webService = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), RESOLUTION_300);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userSession.getUserId());
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userSession.getUserId
                () + UUID.randomUUID() + System.currentTimeMillis());
        maps.put(PARAM_WEB_SERVICE, webService);
        maps.put(PARAM_ID, id);
        maps.put(PARAM_RESOLUTION, resolution);
        maps.put(PARAM_USER_ID, userId);
        return uploadImageUseCase.createRequestParam(cameraLoc, DEFAULT_UPLOAD_PATH,
                DEFAULT_UPLOAD_TYPE, maps);
    }

    private List<ImageUploadModel> parseToModel(UserIdentificationStepperModel model) {
        List<ImageUploadModel> list = new ArrayList<>();
        list.add(new ImageUploadModel(UploadUserIdentificationUseCase.TYPE_KTP, model.getKtpFile()));
        list.add(new ImageUploadModel(UploadUserIdentificationUseCase.TYPE_SELFIE, model.getFaceFile()));
        return list;
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.unsubscribe();
    }
}
