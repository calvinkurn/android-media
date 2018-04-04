package com.tokopedia.tkpd.tkpdreputation.inbox.view.listener;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 8/19/17.
 */

public interface InboxReputationDetail {

    interface View extends CustomerView {

        void showLoading();

        void onErrorGetInboxDetail(String errorMessage);

        void onSuccessGetInboxDetail(InboxReputationItemViewModel inboxReputationItemViewModel,
                                     List<Visitable> visitables);

        void finishLoading();

        void onEditReview(InboxReputationDetailItemViewModel element);

        void onGoToGiveReview(String reviewId, String productId, int shopId,
                              boolean reviewIsSkippable,
                              String productAvatar, String productName,
                              String productUrl, String revieweeName, int productStatus);

        void onErrorSendSmiley(String errorMessage);

        void showLoadingDialog();

        void finishLoadingDialog();

        void showRefresh();

        void onErrorRefreshInboxDetail(String errorMessage);

        void onSuccessRefreshGetInboxDetail(InboxReputationItemViewModel inboxReputationViewModel,
                                            List<Visitable> visitables);

        void finishRefresh();

        void goToPreviewImage(int position, ArrayList<ImageUpload> list);

        int getTab();

        void onGoToReportReview(int shopId, String reviewId);

        void onSuccessSendSmiley(int score);

        void onErrorFavoriteShop(String errorMessage);

        void onSuccessFavoriteShop();

        void onDeleteReviewResponse(InboxReputationDetailItemViewModel element);

        void onErrorDeleteReviewResponse(String errorMessage);

        void onSuccessDeleteReviewResponse();

        void onSendReplyReview(InboxReputationDetailItemViewModel element, String replyReview);

        void onErrorReplyReview(String errorMessage);

        void onSuccessReplyReview();

        void onShareReview(String productName, String productAvatar, String productUrl, String review);

        void onGoToProductDetail(String productId, String productAvatar, String productName);

        void onSmoothScrollToReplyView(int adapterPosition);

        void onGoToProfile(int reviewerId);

        void onGoToShopInfo(int shopId);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getInboxDetail(String id, int anInt);

        void sendSmiley(String reputationId, String score, int role);

        void onFavoriteShopClicked(int shopId);

        void deleteReviewResponse(String reviewId, String productId, String shopId, String
                reputationId);

        void sendReplyReview(int reputationId, String productId, int shopId,
                             String reviewId, String replyReview);
    }
}
