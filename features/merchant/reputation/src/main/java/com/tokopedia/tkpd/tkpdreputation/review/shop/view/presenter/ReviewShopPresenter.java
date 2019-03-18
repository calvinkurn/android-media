package com.tokopedia.tkpd.tkpdreputation.review.shop.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductListMapper;
import com.tokopedia.tkpd.tkpdreputation.review.shop.domain.ReviewShopUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ReviewShopPresenter extends BaseDaggerPresenter<ReviewShopContract.View> implements ReviewShopContract.Presenter {

    protected final ReviewShopUseCase shopReviewUseCase;
    private final LikeDislikeReviewUseCase likeDislikeReviewUseCase;
    private final DeleteReviewResponseUseCase deleteReviewResponseUseCase;
    protected final ReviewProductListMapper productReviewListMapper;
    protected final UserSessionInterface userSession;

    @Inject
    public ReviewShopPresenter(ReviewShopUseCase shopReviewUseCase,
                               LikeDislikeReviewUseCase likeDislikeReviewUseCase,
                               DeleteReviewResponseUseCase deleteReviewResponseUseCase,
                               ReviewProductListMapper productReviewListMapper,
                               UserSessionInterface userSession) {
        this.shopReviewUseCase = shopReviewUseCase;
        this.likeDislikeReviewUseCase = likeDislikeReviewUseCase;
        this.deleteReviewResponseUseCase = deleteReviewResponseUseCase;
        this.productReviewListMapper = productReviewListMapper;
        this.userSession = userSession;
    }

    @Override
    public void onDestroy() {
        if (shopReviewUseCase != null) {
            shopReviewUseCase.unsubscribe();
        }

        if (likeDislikeReviewUseCase != null) {
            likeDislikeReviewUseCase.unsubscribe();
        }

        if (deleteReviewResponseUseCase != null) {
            deleteReviewResponseUseCase.unsubscribe();
        }
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

    public void getShopReview(String shopDomain, String shopId, int page) {
        shopReviewUseCase.execute(shopReviewUseCase.createRequestParams(shopDomain, shopId, String.valueOf(page), userSession.getUserId()),
                getSubscriberGetShopReview());
    }

    protected Subscriber<DataResponseReviewShop> getSubscriberGetShopReview() {
        return new Subscriber<DataResponseReviewShop>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(DataResponseReviewShop dataResponseReviewShop) {
                getView().renderList(productReviewListMapper.map(dataResponseReviewShop, userSession.getUserId()),
                        !TextUtils.isEmpty(dataResponseReviewShop.getPaging().getUriNext()));
            }
        };
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }
}
