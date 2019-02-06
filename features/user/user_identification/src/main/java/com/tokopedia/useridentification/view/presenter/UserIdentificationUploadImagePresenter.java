package com.tokopedia.useridentification.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user_identification_common.KYCConstant;
import com.tokopedia.useridentification.domain.pojo.RegisterIdentificationPojo;
import com.tokopedia.useridentification.domain.pojo.UploadIdentificationPojo;
import com.tokopedia.useridentification.domain.usecase.RegisterIdentificationUseCase;
import com.tokopedia.useridentification.domain.usecase.UploadIdentificationUseCase;
import com.tokopedia.useridentification.view.listener.UserIdentificationUploadImage;
import com.tokopedia.useridentification.view.viewmodel.AttachmentImageModel;
import com.tokopedia.useridentification.view.viewmodel.ImageUploadModel;
import com.tokopedia.useridentification.view.viewmodel.UserIdentificationStepperModel;
import com.tokopedia.useridentification.R;
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
 * @author by alvinatin on 21/11/18.
 */

public class UserIdentificationUploadImagePresenter extends
        BaseDaggerPresenter<UserIdentificationUploadImage.View>
        implements UserIdentificationUploadImage.Presenter {

    private static final String RESOLUTION_300 = "300";
    private static final String PARAM_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String PARAM_RESOLUTION = "param_resolution";
    private static final String DEFAULT_UPLOAD_PATH = "/upload/attachment";
    private static final String DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg";
    private CompositeSubscription compositeSubscription;

    private final UploadImageUseCase<AttachmentImageModel> uploadImageUseCase;
    private final UploadIdentificationUseCase uploadIdentificationUseCase;
    private final RegisterIdentificationUseCase registerIdentificationUseCase;
    private final UserSession userSession;

    @Inject
    public UserIdentificationUploadImagePresenter(UploadImageUseCase<AttachmentImageModel>
                                                          uploadImageUseCase,
                                                  UploadIdentificationUseCase
                                                          uploadIdentificationUseCase,
                                                  RegisterIdentificationUseCase registerIdentificationUseCase,
                                                  UserSession userSession,
                                                  CompositeSubscription compositeSubscription) {
        this.uploadImageUseCase = uploadImageUseCase;
        this.uploadIdentificationUseCase = uploadIdentificationUseCase;
        this.registerIdentificationUseCase = registerIdentificationUseCase;
        this.userSession = userSession;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void uploadImage(UserIdentificationStepperModel model) {
        List<ImageUploadModel> attachments = parseToModel(model);
        compositeSubscription.add(Observable.from(attachments)
                .flatMap(new Func1<ImageUploadModel, Observable<ImageUploadModel>>() {
                    @Override
                    public Observable<ImageUploadModel> call(ImageUploadModel
                                                                     imageUploadModel) {
                        return Observable.zip(Observable.just(imageUploadModel),
                                uploadImageUseCase.createObservable(
                                        createParam(imageUploadModel.getFilePath())
                                ), new Func2<ImageUploadModel,
                                        ImageUploadDomainModel<AttachmentImageModel>,
                                        ImageUploadModel>() {
                                    @Override
                                    public ImageUploadModel call(ImageUploadModel
                                                                         imageUploadModel,
                                                                 ImageUploadDomainModel<AttachmentImageModel> uploadDomainModel) {
                                        imageUploadModel.setPicObjKyc(uploadDomainModel
                                                .getDataResultImageUpload().getData()
                                                .getPicObj());
                                        return imageUploadModel;
                                    }
                                });
                    }
                }).toList()
                .flatMap(new Func1<List<ImageUploadModel>, Observable<ImageUploadModel>>() {
                    @Override
                    public Observable<ImageUploadModel> call(List<ImageUploadModel>
                                                                     imageUploadModels) {
                        return Observable.from(imageUploadModels)
                                .flatMap(new Func1<ImageUploadModel,
                                        Observable<ImageUploadModel>>() {
                                    @Override
                                    public Observable<ImageUploadModel> call
                                            (ImageUploadModel imageUploadModel) {
                                        return Observable.zip(Observable.just
                                                        (imageUploadModel),
                                                uploadIdentificationUseCase.createObservable(
                                                                UploadIdentificationUseCase.getRequestParam(
                                                                        imageUploadModel.getKycType(),
                                                                        imageUploadModel.getPicObjKyc()
                                                                )
                                                        ), new Func2<ImageUploadModel,
                                                        GraphqlResponse, ImageUploadModel>() {
                                                    @Override
                                                    public ImageUploadModel call(ImageUploadModel imageUploadModel,
                                                                                 GraphqlResponse graphqlResponse) {
                                                        UploadIdentificationPojo pojo =
                                                                graphqlResponse.getData(UploadIdentificationPojo.class);
                                                        imageUploadModel.setError(pojo.getKycUpload().getError());
                                                        imageUploadModel.setIsSuccess(pojo.getKycUpload().getIsSuccess());
                                                        return imageUploadModel;
                                                    }
                                                });
                                    }
                                });
                    }
                })
                .toList()
                .flatMap(new Func1<List<ImageUploadModel>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<ImageUploadModel> imageUploadModels) {
                        int totalSuccess = 0;
                        for (ImageUploadModel imageUploadModel : imageUploadModels) {
                            totalSuccess = totalSuccess + imageUploadModel.getIsSuccess();
                        }
                        return Observable.just(totalSuccess == KYCConstant.IS_ALL_MUTATION_SUCCESS);
                    }
                })
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        return registerIdentificationUseCase.createObservable(
                                RegisterIdentificationUseCase.getRequestParam()
                        ).flatMap(new Func1<GraphqlResponse, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(GraphqlResponse graphqlResponse) {
                                RegisterIdentificationPojo pojo = graphqlResponse.getData(RegisterIdentificationPojo.class);
                                return Observable.just(pojo != null && pojo.getKycRegister() != null && pojo.getKycRegister().getIsSuccess() == 1);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().onErrorUpload(ErrorHandler.getErrorMessage(getView().getContext
                                (), throwable));
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getView().onSuccessUpload();
                        } else {
                            getView().onErrorUpload(String.format(
                                    getView().getContext().getString(R.string.error_upload_image_kyc),
                                    KYCConstant.ERROR_UPLOAD_IMAGE));
                        }
                    }
                })
        );
    }

    private List<ImageUploadModel> parseToModel(UserIdentificationStepperModel model) {
        List<ImageUploadModel> list = new ArrayList<>();
        list.add(new ImageUploadModel(UploadIdentificationUseCase.TYPE_KTP, model.getKtpFile()));
        list.add(new ImageUploadModel(UploadIdentificationUseCase.TYPE_SELFIE, model.getFaceFile
                ()));
        return list;
    }

    private RequestParams createParam(String cameraLoc) {
        Map<String, RequestBody> maps = new HashMap<String, RequestBody>();
        RequestBody webService = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), RESOLUTION_300);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userSession.getUserId
                () + UUID.randomUUID() + System.currentTimeMillis());
        maps.put(PARAM_WEB_SERVICE, webService);
        maps.put(PARAM_ID, id);
        maps.put(PARAM_RESOLUTION, resolution);
        return uploadImageUseCase.createRequestParam(cameraLoc, DEFAULT_UPLOAD_PATH,
                DEFAULT_UPLOAD_TYPE, maps);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.unsubscribe();
    }
}
