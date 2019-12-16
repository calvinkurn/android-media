package com.tokopedia.kyc_centralized.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.kyc_centralized.KYCConstant;
import com.tokopedia.kyc_centralized.R;
import com.tokopedia.user_identification_common.domain.pojo.RegisterIdentificationPojo;
import com.tokopedia.kyc_centruser_identification_commonalized.domain.pojo.UploadIdentificationPojo;
import com.tokopedia.user_identification_common.domain.usecase.GetKtpStatusUseCase;
import com.tokopedia.user_identification_common.domain.usecase.RegisterIdentificationUseCase;
import com.tokopedia.user_identification_common.domain.usecase.UploadIdentificationUseCase;
import com.tokopedia.user_identification_common.subscriber.GetKtpStatusSubscriber;
import com.tokopedia.user_identification_common.util.SchedulerProvider;
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage;
import com.tokopedia.kyc_centralized.view.viewmodel.AttachmentImageModel;
import com.tokopedia.kyc_centralized.view.viewmodel.ImageUploadModel;
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel;

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

    private final GetKtpStatusUseCase cekKtpStatusUseCase;
    private final UploadImageUseCase<AttachmentImageModel> uploadImageUseCase;
    private final UploadIdentificationUseCase uploadIdentificationUseCase;
    private final RegisterIdentificationUseCase registerIdentificationUseCase;
    private final SchedulerProvider schedulerProvider;
    private final UserSessionInterface userSession;

    @Inject
    public UserIdentificationUploadImagePresenter(UploadImageUseCase<AttachmentImageModel>
                                                          uploadImageUseCase,
                                                  UploadIdentificationUseCase
                                                          uploadIdentificationUseCase,
                                                  RegisterIdentificationUseCase registerIdentificationUseCase,
                                                  GetKtpStatusUseCase cekKtpStatusUseCase,
                                                  UserSessionInterface userSession,
                                                  CompositeSubscription compositeSubscription,
                                                  SchedulerProvider schedulerProvider) {
        this.uploadImageUseCase = uploadImageUseCase;
        this.uploadIdentificationUseCase = uploadIdentificationUseCase;
        this.registerIdentificationUseCase = registerIdentificationUseCase;
        this.userSession = userSession;
        this.compositeSubscription = compositeSubscription;
        this.schedulerProvider = schedulerProvider;
        this.cekKtpStatusUseCase = cekKtpStatusUseCase;
    }

    @Override
    public Observable<ImageUploadModel> uploadImageUseCase(ImageUploadModel imageUploadModel) {
        return Observable.zip(Observable.just(imageUploadModel),
                uploadImageUseCase.createObservable(createParam(imageUploadModel.getFilePath())),
                (imageUploadModel1, uploadDomainModel) -> {
                    imageUploadModel1.setPicObjKyc(uploadDomainModel
                            .getDataResultImageUpload()
                            .getPictureObj());
                    return imageUploadModel1;
                });
    }



    @Override
    public Observable<ImageUploadModel> uploadIdentificationUseCase(List<ImageUploadModel> imageUploadModels, int projectId) {
        return Observable.from(imageUploadModels)
                .flatMap((Func1<ImageUploadModel, Observable<ImageUploadModel>>) imageUploadModel ->
                        Observable.zip(Observable.just(imageUploadModel),
                                uploadIdentificationUseCase.createObservable(
                                        UploadIdentificationUseCase.getRequestParam(
                                                imageUploadModel.getKycType(),
                                                imageUploadModel.getPicObjKyc(),
                                                imageUploadModel.getFileName(),
                                                projectId
                                        )
                                ), (imageUploadModel1, graphqlResponse) -> {
                                    UploadIdentificationPojo pojo = graphqlResponse.getData(UploadIdentificationPojo.class);
                                    imageUploadModel1.setError(pojo != null ? pojo.getKycUpload().getError() : null);
                                    imageUploadModel1.setIsSuccess(pojo != null ? pojo.getKycUpload().getIsSuccess() : 0);
                                    return imageUploadModel1;
                                }));

    }

    @Override
    public Observable<Boolean> registerIdentificationUseCase(int projectId) {
        return registerIdentificationUseCase.createObservable(
                RegisterIdentificationUseCase.getRequestParam(projectId)
        ).flatMap((Func1<GraphqlResponse, Observable<Boolean>>) graphqlResponse -> {
            RegisterIdentificationPojo pojo = graphqlResponse.getData(RegisterIdentificationPojo.class);
            return Observable.just(pojo != null && pojo.getKycRegister() != null && pojo.getKycRegister().getIsSuccess() == 1);
        });
    }


    @Override
    public Observable<Boolean> isAllMutationSuccess(List<ImageUploadModel> imageUploadModels) {
        int totalSuccess = 0;
        for (ImageUploadModel imageUploadModel : imageUploadModels) {
            totalSuccess = totalSuccess + imageUploadModel.getIsSuccess();
        }
        return Observable.just(totalSuccess == KYCConstant.IS_ALL_MUTATION_SUCCESS);
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
                .flatMap(new Func1<List<ImageUploadModel>, Observable<ImageUploadModel>>() {
                    @Override
                    public Observable<ImageUploadModel> call(List<ImageUploadModel> imageUploadModels) {
                        return uploadIdentificationUseCase(imageUploadModels, projectId);
                    }
                }).toList()
                .flatMap(new Func1<List<ImageUploadModel>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<ImageUploadModel> imageUploadModels) {
                        return isAllMutationSuccess(imageUploadModels);
                    }
                })
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        return registerIdentificationUseCase(projectId);
                    }
                })
                .subscribeOn(schedulerProvider.io())
                .unsubscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().onErrorUpload(ErrorHandler.getErrorMessage(getView().getContext(), throwable));
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getView().onSuccessUpload();
                        } else {
                            getView().onErrorUpload(
                                    String.format(getView().getContext().getString(R.string.error_upload_image_kyc),
                                    KYCConstant.ERROR_UPLOAD_IMAGE)
                            );
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
        list.add(new ImageUploadModel(UploadIdentificationUseCase.TYPE_KTP, model.getKtpFile()));
        list.add(new ImageUploadModel(UploadIdentificationUseCase.TYPE_SELFIE, model.getFaceFile()));
        return list;
    }

    @Override
    public void checkKtp(String image) {
        cekKtpStatusUseCase.execute(cekKtpStatusUseCase.getRequestParam(image), new GetKtpStatusSubscriber(getView().getKtpStatusListener()));
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.unsubscribe();
    }
}
