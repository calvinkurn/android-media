package com.tokopedia.tkpd.tkpdreputation.review.product.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetHelpfulUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetListUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.domain.ReviewProductGetRatingUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductListMapper;
import com.tokopedia.usecase.RequestParams;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ReviewProductPresenter extends BaseDaggerPresenter<ReviewProductContract.View> implements ReviewProductContract.Presenter {

    private final UserSession userSession;
    private final ReviewProductGetListUseCase productReviewGetListUseCase;
    private final ReviewProductGetHelpfulUseCase productReviewGetHelpfulUseCase;
    private final ReviewProductGetRatingUseCase productReviewGetRatingUseCase;
    private final LikeDislikeReviewUseCase likeDislikeReviewUseCase;
    private final DeleteReviewResponseUseCase deleteReviewResponseUseCase;
    private final ReviewProductListMapper productReviewListMapper;

    public ReviewProductPresenter(ReviewProductGetListUseCase productReviewGetListUseCase,
                                  ReviewProductGetHelpfulUseCase productReviewGetHelpfulUseCase,
                                  ReviewProductGetRatingUseCase productReviewGetRatingUseCase,
                                  LikeDislikeReviewUseCase likeDislikeReviewUseCase,
                                  DeleteReviewResponseUseCase deleteReviewResponseUseCase,
                                  ReviewProductListMapper productReviewListMapper,
                                  UserSession userSession) {
        this.productReviewGetListUseCase = productReviewGetListUseCase;
        this.productReviewGetHelpfulUseCase = productReviewGetHelpfulUseCase;
        this.productReviewGetRatingUseCase = productReviewGetRatingUseCase;
        this.likeDislikeReviewUseCase = likeDislikeReviewUseCase;
        this.deleteReviewResponseUseCase = deleteReviewResponseUseCase;
        this.productReviewListMapper = productReviewListMapper;
        this.userSession = userSession;
    }

    public void deleteReview(String reviewId, String reputationId, String productId){
        getView().showProgressLoading();
        deleteReviewResponseUseCase.execute(DeleteReviewResponseUseCase.getParam(reviewId, productId, userSession.getShopId(), reputationId),
                getSubscriberDeleteReview(reviewId));
    }

    private Subscriber<DeleteReviewResponseDomain> getSubscriberDeleteReview(final String reviewId) {
        return new Subscriber<DeleteReviewResponseDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().hideProgressLoading();
                    getView().onErrorDeleteReview(e);
                }
            }

            @Override
            public void onNext(DeleteReviewResponseDomain deleteReviewResponseDomain) {
                getView().hideProgressLoading();
                if(deleteReviewResponseDomain.isSuccess()) {
                    getView().onSuccessDeleteReview(deleteReviewResponseDomain, reviewId);
                }else{
                    getView().onErrorDeleteReview(new RuntimeException());
                }
            }
        };
    }

    public void postLikeDislikeReview(String reviewId, int likeStatus, String productId){
        getView().showProgressLoading();
        likeDislikeReviewUseCase.execute(LikeDislikeReviewUseCase.getParam(reviewId, likeStatus, productId, userSession.getShopId()),
                getSubscriberPostLikeDislike(reviewId, likeStatus));
    }

    private Subscriber<LikeDislikeDomain> getSubscriberPostLikeDislike(final String reviewId, int likeStatus) {
        return new Subscriber<LikeDislikeDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().hideProgressLoading();
                    getView().onErrorPostLikeDislike(e, reviewId, likeStatus);
                }
            }

            @Override
            public void onNext(LikeDislikeDomain likeDislikeDomain) {
                getView().hideProgressLoading();
                getView().onSuccessPostLikeDislike(likeDislikeDomain, reviewId);
            }
        };
    }

    public void getRatingReview(String productId) {
        productReviewGetRatingUseCase.execute(productReviewGetRatingUseCase.createRequestParams(productId),
                getSubscriberGetRating());
    }

    private Subscriber<DataResponseReviewStarCount> getSubscriberGetRating() {
        return new Subscriber<DataResponseReviewStarCount>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onErrorGetRatingView(e);
                }
            }

            @Override
            public void onNext(DataResponseReviewStarCount dataResponseReviewStarCount) {
                getView().onGetRatingReview(dataResponseReviewStarCount);
            }
        };
    }

    public void getHelpfulReview(String productId) {
        productReviewGetHelpfulUseCase.execute(productReviewGetHelpfulUseCase.createRequestParams(productId, userSession.getUserId()),
                getSubscriberGetHelpfulReview(productId));
    }

    private Subscriber<DataResponseReviewHelpful> getSubscriberGetHelpfulReview(final String productId) {
        return new Subscriber<DataResponseReviewHelpful>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onErrorGetListReviewHelpful(e);
                }
            }

            @Override
            public void onNext(DataResponseReviewHelpful dataResponseReviewHelpful) {
                getView().onGetListReviewHelpful(productReviewListMapper.map(dataResponseReviewHelpful, userSession.getUserId(), productId));
            }
        };
    }

    public void getProductReview(String productId, int page, String rating, boolean isWithImage) {
        RequestParams requestParams =
                productReviewGetListUseCase.createRequestParams(productId,
                        String.valueOf(page), rating, userSession.getUserId());

        if (isWithImage) {
            requestParams = productReviewGetListUseCase.withPhotoParams(requestParams);
        }

        productReviewGetListUseCase.execute(requestParams, getSubscriberGetProductReview());
    }

    private Subscriber<DataResponseReviewProduct> getSubscriberGetProductReview() {
        return new Subscriber<DataResponseReviewProduct>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().onErrorGetListReviewProduct(e);
                }
            }

            @Override
            public void onNext(DataResponseReviewProduct dataResponseReviewProduct) {
                getView().onGetListReviewProduct(productReviewListMapper.map(dataResponseReviewProduct, userSession.getUserId()),
                        !TextUtils.isEmpty(dataResponseReviewProduct.getPaging().getUriNext()));
            }
        };
    }
}
