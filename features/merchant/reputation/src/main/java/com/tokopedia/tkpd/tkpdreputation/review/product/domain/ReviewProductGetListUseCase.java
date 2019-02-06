package com.tokopedia.tkpd.tkpdreputation.review.product.domain;

import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeListDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.Review;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ReviewProductGetListUseCase extends UseCase<DataResponseReviewProduct> {

    public static final String PRODUCT_ID = "product_id";
    public static final String PAGE = "page";
    public static final String PER_PAGE = "per_page";
    public static final String RATING = "rating";
    public static final String DEFAULT_PER_PAGE = "10";
    public static final String USER_ID = "user_id";
    public static final String WITH_ATTACHMENT = "with_attachment";
    public static final String WITH_ATTACHMENT_IMAGE_VALUE = "1";
    public static final String DEFAULT_NO_ATTACHMENT = "0";

    private final ReputationRepository reputationRepository;
    private GetLikeDislikeReviewUseCase getLikeDislikeReviewUseCase;

    @Inject
    public ReviewProductGetListUseCase(ReputationRepository reputationRepository,
                                       GetLikeDislikeReviewUseCase getLikeDislikeReviewUseCase) {
        this.reputationRepository = reputationRepository;
        this.getLikeDislikeReviewUseCase = getLikeDislikeReviewUseCase;
    }

    @Override
    public Observable<DataResponseReviewProduct> createObservable(final RequestParams requestParams) {
        return reputationRepository.getReviewProductList(
                requestParams.getString(PRODUCT_ID, ""),
                requestParams.getString(PAGE, ""),
                requestParams.getString(PER_PAGE, ""),
                requestParams.getString(RATING, ""),
                requestParams.getString(WITH_ATTACHMENT, DEFAULT_NO_ATTACHMENT)
                )
                .flatMap(new Func1<DataResponseReviewProduct, Observable<DataResponseReviewProduct>>() {
                    @Override
                    public Observable<DataResponseReviewProduct> call(final DataResponseReviewProduct dataResponseReviewProduct) {
                        if(dataResponseReviewProduct.getList()!= null && dataResponseReviewProduct.getList().size() > 0) {
                            return getLikeDislikeReviewUseCase.createObservable(
                                    GetLikeDislikeReviewUseCase.getParam(createReviewIds(dataResponseReviewProduct), requestParams.getString(USER_ID, "")))
                                    .map(new Func1<GetLikeDislikeReviewDomain, DataResponseReviewProduct>() {
                                        @Override
                                        public DataResponseReviewProduct call(GetLikeDislikeReviewDomain getLikeDislikeReviewDomain) {
                                            return mapLikeModelToReviewModel(getLikeDislikeReviewDomain, dataResponseReviewProduct);
                                        }
                                    });
                        }else{
                            return Observable.just(dataResponseReviewProduct);
                        }
                    }
                });
    }

    private DataResponseReviewProduct mapLikeModelToReviewModel(GetLikeDislikeReviewDomain getLikeDislikeReviewDomain,
                                                                DataResponseReviewProduct dataResponseReviewProduct) {
        for(Review review : dataResponseReviewProduct.getList()) {
            for (LikeDislikeListDomain likeDislikeListDomain : getLikeDislikeReviewDomain.getList()) {
                if(likeDislikeListDomain.getReviewId() == review.getReviewId()){
                    review.setTotalLike(likeDislikeListDomain.getTotalLike());
                    review.setLikeStatus(likeDislikeListDomain.getLikeStatus());
                    break;
                }
            }
        }
        return dataResponseReviewProduct;
    }

    private String createReviewIds(DataResponseReviewProduct dataResponseReviewProduct) {
        List<String> listIds = new ArrayList<>();
        for(Review review :dataResponseReviewProduct.getList()){
            listIds.add(String.valueOf(review.getReviewId()));
        }
        return StringUtils.convertListToStringDelimiter(listIds,"~");
    }

    public RequestParams createRequestParams(String productId, String page, String rating, String userId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PRODUCT_ID, productId);
        requestParams.putString(PAGE, page);
        requestParams.putString(PER_PAGE, DEFAULT_PER_PAGE);
        requestParams.putString(RATING, rating);
        requestParams.putString(USER_ID, userId);
        return requestParams;
    }

    public RequestParams withPhotoParams(RequestParams requestParams){
        requestParams.putString(WITH_ATTACHMENT, WITH_ATTACHMENT_IMAGE_VALUE);
        return requestParams;
    }
}
