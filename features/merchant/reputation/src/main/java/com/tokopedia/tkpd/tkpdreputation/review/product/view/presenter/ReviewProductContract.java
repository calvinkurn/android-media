package com.tokopedia.tkpd.tkpdreputation.review.product.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public interface ReviewProductContract {
    interface Presenter extends CustomerPresenter<View> {

    }

    interface View extends BaseListViewListener<ReviewProductModel>{

        void onGetListReviewProduct(List<ReviewProductModel> map, boolean isHasNextPage);

        void onErrorGetListReviewProduct(Throwable e);

        void onGetListReviewHelpful(List<ReviewProductModel> map);

        void onErrorGetListReviewHelpful(Throwable e);

        void onGetRatingReview(DataResponseReviewStarCount dataResponseReviewStarCount);

        void onErrorGetRatingView(Throwable e);

        void onSuccessPostLikeDislike(LikeDislikeDomain likeDislikeDomain, String reviewId);

        void onErrorPostLikeDislike(Throwable e, String reviewId, int likeStatus);

        void onSuccessDeleteReview(DeleteReviewResponseDomain deleteReviewResponseDomain, String reviewId);

        void onErrorDeleteReview(Throwable e);

        void showProgressLoading();

        void hideProgressLoading();
    }
}
