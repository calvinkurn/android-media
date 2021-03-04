package com.tokopedia.review.feature.inbox.buyerreview.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageUpload;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 8/19/17.
 */

public interface InboxReputationDetail {

    interface View extends CustomerView {

        void showLoading();

        void onErrorGetInboxDetail(Throwable throwable);

        void onSuccessGetInboxDetail(InboxReputationItemUiModel inboxReputationItemUiModel,
                                     List<Visitable> visitables);

        void finishLoading();

        void onErrorSendSmiley(String errorMessage);

        void showLoadingDialog();

        void finishLoadingDialog();

        void showRefresh();

        void onErrorRefreshInboxDetail(Throwable throwable);

        void onSuccessRefreshGetInboxDetail(InboxReputationItemUiModel inboxReputationViewModel,
                                            List<Visitable> visitables);

        void finishRefresh();

        void goToPreviewImage(int position, ArrayList<ImageUpload> list);

        int getTab();

        Context getContext();

        void onGoToReportReview(int shopId, String reviewId);

        void onSuccessSendSmiley(int score);

        void onErrorFavoriteShop(String errorMessage);

        void onSuccessFavoriteShop();

        void onDeleteReviewResponse(InboxReputationDetailItemUiModel element);

        void onErrorDeleteReviewResponse(String errorMessage);

        void onSuccessDeleteReviewResponse();

        void onSendReplyReview(InboxReputationDetailItemUiModel element, String replyReview);

        void onErrorReplyReview(String errorMessage);

        void onSuccessReplyReview();

        void onShareReview(InboxReputationDetailItemUiModel inboxReputationDetailItemUiModel, int adapterPosition);

        void onGoToProductDetail(String productId, String productAvatar, String productName);

        void onSmoothScrollToReplyView(int adapterPosition);

        void onGoToProfile(int reviewerId);

        void onGoToShopInfo(int shopId);

        UserSessionInterface getUserSession();

        void onClickReviewOverflowMenu(InboxReputationDetailItemUiModel inboxReputationDetailItemUiModel,
                                       int adapterPosition);

        void onClickToggleReply(InboxReputationDetailItemUiModel inboxReputationDetailItemUiModel,
                                int adapterPosition);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getInboxDetail(String id, int anInt);

        void sendSmiley(String reputationId, String score, int role);

        void deleteReviewResponse(String reviewId, String productId, String shopId, String
                reputationId);

        void sendReplyReview(int reputationId, String productId, int shopId,
                             String reviewId, String replyReview);
    }
}
