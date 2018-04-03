package com.tokopedia.tkpd.tkpdreputation.review.shop.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopModelContent;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public interface ReviewShopContract {
    interface Presenter extends CustomerPresenter<View> {

    }

    interface View extends BaseListViewListener<ReviewShopModelContent> {

        void onErrorDeleteReview(Throwable e);

        void onSuccessDeleteReview(DeleteReviewResponseDomain deleteReviewResponseDomain, String reviewId);

        void onErrorPostLikeDislike(Throwable e);

        void onSuccessPostLikeDislike(LikeDislikeDomain likeDislikeDomain, String reviewId);

        void hideProgressLoading();

        void showProgressLoading();
    }
}
