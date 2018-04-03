package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview;

import com.drew.lang.annotations.Nullable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewRequestModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.UploadImageUseCase;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.GenerateHostDomain;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.model.UploadImageDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 8/31/17.
 */

public class SendReviewUseCase extends UseCase<SendReviewDomain> {

    public static final String IMAGE = "image";

    protected static final String PARAM_LIST_IMAGE = "LIST_IMAGE";
    protected static final String PARAM_LIST_DELETED_IMAGE = "PARAM_LIST_DELETED_IMAGE";

    private final SendReviewValidateUseCase sendReviewValidateUseCase;
    protected final GenerateHostUseCase generateHostUseCase;
    protected final UploadImageUseCase uploadImageUseCase;
    private final SendReviewSubmitUseCase sendReviewSubmitUseCase;

    public SendReviewUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             SendReviewValidateUseCase sendReviewValidateUseCase,
                             GenerateHostUseCase generateHostUseCase,
                             UploadImageUseCase uploadImageUseCase,
                             SendReviewSubmitUseCase sendReviewSubmitUseCase) {
        super(threadExecutor, postExecutionThread);
        this.sendReviewValidateUseCase = sendReviewValidateUseCase;
        this.generateHostUseCase = generateHostUseCase;
        this.uploadImageUseCase = uploadImageUseCase;
        this.sendReviewSubmitUseCase = sendReviewSubmitUseCase;
    }

    public SendReviewUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             GenerateHostUseCase generateHostUseCase,
                             UploadImageUseCase uploadImageUseCase) {
        super(threadExecutor, postExecutionThread);
        this.sendReviewSubmitUseCase = null;
        this.sendReviewValidateUseCase = null;
        this.generateHostUseCase = generateHostUseCase;
        this.uploadImageUseCase = uploadImageUseCase;
    }

    @Override
    public Observable<SendReviewDomain> createObservable(RequestParams requestParams) {
        final SendReviewRequestModel sendReviewRequestModel = new
                SendReviewRequestModel();
        sendReviewRequestModel.setListUpload((ArrayList<ImageUpload>) requestParams.getObject(PARAM_LIST_IMAGE));
        return getObservableValidateReview(getParamSendReviewValidation(requestParams),
                sendReviewRequestModel)
                .flatMap(getObservableGenerateHost(GenerateHostUseCase.getParam()))
                .flatMap(getObservableUploadImages(getListImage(requestParams)))
                .flatMap(getObservableSubmitReview())
                .flatMap(mappingResultToDomain());
    }

    protected Func1<SendReviewSubmitDomain, Observable<SendReviewRequestModel>>
    addSubmitImageResultToRequestModel(final SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<SendReviewSubmitDomain, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(SendReviewSubmitDomain sendReviewSubmitDomain) {
                sendReviewRequestModel.setIsSubmitSuccess(sendReviewSubmitDomain.getIsSuccess());
                sendReviewRequestModel.setReviewId(sendReviewSubmitDomain.getReviewId());
                return Observable.just(sendReviewRequestModel);
            }
        };
    }

    protected Func1<SendReviewRequestModel, Observable<SendReviewRequestModel>>
    getObservableSubmitReview() {
        return new Func1<SendReviewRequestModel, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(SendReviewRequestModel sendReviewRequestModel) {
                if (sendReviewRequestModel.getPostKey().isEmpty()) {
                    return Observable.just(sendReviewRequestModel);
                } else {
                    return sendReviewSubmitUseCase.createObservable(
                            SendReviewSubmitUseCase.getParam(sendReviewRequestModel))
                            .flatMap(addSubmitImageResultToRequestModel(sendReviewRequestModel));
                }
            }
        };
    }

    protected RequestParams getParamSendReviewValidation(RequestParams requestParams) {
        return SendReviewValidateUseCase.getParamWithImage(
                requestParams.getString(SendReviewValidateUseCase.PARAM_REVIEW_ID, ""),
                requestParams.getString(SendReviewValidateUseCase.PARAM_PRODUCT_ID, ""),
                requestParams.getString(SendReviewValidateUseCase.PARAM_REPUTATION_ID, ""),
                requestParams.getString(SendReviewValidateUseCase.PARAM_SHOP_ID, ""),
                requestParams.getString(SendReviewValidateUseCase.PARAM_RATING, ""),
                requestParams.getString(SendReviewValidateUseCase.PARAM_REVIEW_MESSAGE, ""),
                (ArrayList<ImageUpload>) requestParams.getObject(PARAM_LIST_IMAGE),
                (List<ImageUpload>) requestParams.getObject(PARAM_LIST_DELETED_IMAGE),
                requestParams.getInt(SendReviewValidateUseCase.PARAM_ANONYMOUS, 0) ==
                        SendReviewValidateUseCase.DEFAULT_IS_ANONYMOUS);
    }

    protected Func1<List<UploadImageDomain>, Observable<SendReviewRequestModel>>
    addListImageUploadToRequestModel(final SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<List<UploadImageDomain>, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(@Nullable List<UploadImageDomain>
                                                                   uploadImageDomains) {
                if (!sendReviewRequestModel.getPostKey().isEmpty()) {
                    for (int i = 0; i < uploadImageDomains.size(); i++) {
                        sendReviewRequestModel.getListUpload().get(i).setPicObj(uploadImageDomains
                                .get(i).getPicObj());
                        sendReviewRequestModel.getListUpload().get(i).setPicSrc(uploadImageDomains
                                .get(i).getPicSrc());
                    }
                }
                return Observable.just(sendReviewRequestModel);
            }
        };
    }

    protected List<ImageUpload> getListImage(RequestParams requestParams) {
        return (List<ImageUpload>) requestParams.getObject(PARAM_LIST_IMAGE);
    }

    protected Func1<SendReviewRequestModel, Observable<SendReviewRequestModel>>
    getObservableUploadImages(final List<ImageUpload> listImage) {
        return new Func1<SendReviewRequestModel, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(final SendReviewRequestModel sendReviewRequestModel) {
                return Observable.from(listImage)
                        .flatMap(new Func1<ImageUpload, Observable<UploadImageDomain>>() {
                            @Override
                            public Observable<UploadImageDomain> call(ImageUpload imageUpload) {
                                if (sendReviewRequestModel.getPostKey().isEmpty()) {
                                    return Observable.just(null);
                                } else {
                                    return uploadImageUseCase.createObservable(
                                            UploadImageUseCase.getParam(
                                                    sendReviewRequestModel,
                                                    imageUpload.getImageId(),
                                                    imageUpload.getFileLoc()
                                            ));
                                }
                            }
                        }).toList()
                        .flatMap(addListImageUploadToRequestModel(sendReviewRequestModel));
            }
        };
    }

    protected Func1<SendReviewRequestModel, Observable<SendReviewRequestModel>>
    getObservableGenerateHost(final RequestParams param) {
        return new Func1<SendReviewRequestModel, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(SendReviewRequestModel sendReviewRequestModel) {
                if (!sendReviewRequestModel.getPostKey().isEmpty())
                    return generateHostUseCase.createObservable(param)
                            .flatMap(addGenerateHostResultToRequestModel(sendReviewRequestModel));
                else
                    return Observable.just(sendReviewRequestModel);
            }
        };
    }

    protected Func1<GenerateHostDomain, Observable<SendReviewRequestModel>>
    addGenerateHostResultToRequestModel(final SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<GenerateHostDomain, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(@Nullable GenerateHostDomain
                                                                   generateHostDomain) {
                if (generateHostDomain != null) {
                    sendReviewRequestModel.setUploadHost(generateHostDomain.getUploadHost());
                    sendReviewRequestModel.setServerId(generateHostDomain.getServerId());
                }

                return Observable.just(sendReviewRequestModel);
            }
        };
    }

    protected Func1<SendReviewRequestModel, Observable<SendReviewDomain>> mappingResultToDomain() {
        return new Func1<SendReviewRequestModel, Observable<SendReviewDomain>>() {
            @Override
            public Observable<SendReviewDomain> call(
                    SendReviewRequestModel sendReviewRequestModel) {
                if (sendReviewRequestModel.getPostKey().isEmpty()) {
                    return Observable.just(new SendReviewDomain(sendReviewRequestModel
                            .isValidateSuccess()));
                } else {
                    return Observable.just(new SendReviewDomain(sendReviewRequestModel
                            .getIsSubmitSuccess() == 1));
                }

            }
        };
    }

    protected Observable<SendReviewRequestModel> getObservableValidateReview(
            RequestParams requestParams, final SendReviewRequestModel sendReviewRequestModel) {
        return sendReviewValidateUseCase.createObservable(requestParams)
                .flatMap(addValidateResultToRequestModel(sendReviewRequestModel));
    }

    protected Func1<SendReviewValidateDomain, Observable<SendReviewRequestModel>>
    addValidateResultToRequestModel(final SendReviewRequestModel sendReviewRequestModel) {
        return new Func1<SendReviewValidateDomain, Observable<SendReviewRequestModel>>() {
            @Override
            public Observable<SendReviewRequestModel> call(
                    SendReviewValidateDomain sendReviewValidateDomain) {
                sendReviewRequestModel.setPostKey(sendReviewValidateDomain.getPostKey());
                sendReviewRequestModel.setReviewId(sendReviewValidateDomain.getReviewId());
                sendReviewRequestModel.setValidateSuccess(sendReviewValidateDomain.getIsSuccess() == 1);
                return Observable.just(sendReviewRequestModel);
            }
        };
    }

    public static RequestParams getParam(String reviewId,
                                         String productId,
                                         String reputationId,
                                         String shopId,
                                         String rating,
                                         String reviewMessage,
                                         ArrayList<ImageUpload> list,
                                         List<ImageUpload> deletedList, boolean isAnonymous) {
        RequestParams params = RequestParams.create();
        params.getParameters().putAll(SendReviewValidateUseCase.getParamWithImage(reviewId, productId,
                reputationId, shopId, rating, reviewMessage, list, deletedList, isAnonymous).getParameters
                ());
        params.putObject(PARAM_LIST_IMAGE, list);
        params.putObject(PARAM_LIST_DELETED_IMAGE, deletedList);
        return params;
    }

}
