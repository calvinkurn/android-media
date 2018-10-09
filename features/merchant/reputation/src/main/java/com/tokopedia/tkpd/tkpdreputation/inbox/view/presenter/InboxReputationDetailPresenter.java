package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.FavoriteShopUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendReplyReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendSmileyReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.DeleteReviewResponseSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.FavoriteShopSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.GetInboxReputationDetailSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.RefreshInboxReputationDetailSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.ReplyReviewSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.SendSmileySubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailPresenter
        extends BaseDaggerPresenter<InboxReputationDetail.View>
        implements InboxReputationDetail.Presenter {

    private static final String SRC_INBOX_REPUTATION_DETAIL = "inbox_reputation_detail";
    private final GetInboxReputationDetailUseCase getInboxReputationDetailUseCase;
    private final SessionHandler sessionHandler;
    private final SendSmileyReputationUseCase sendSmileyReputationUseCase;
    private final FavoriteShopUseCase favoriteShopUseCase;
    private final DeleteReviewResponseUseCase deleteReviewResponseUseCase;
    private final SendReplyReviewUseCase sendReplyReviewUseCase;
    private InboxReputationDetail.View viewListener;

    @Inject
    InboxReputationDetailPresenter(
            GetInboxReputationDetailUseCase getInboxReputationDetailUseCase,
            SendSmileyReputationUseCase sendSmileyReputationUseCase,
            FavoriteShopUseCase favoriteShopUseCase,
            DeleteReviewResponseUseCase deleteReviewResponseUseCase,
            SendReplyReviewUseCase sendReplyReviewUseCase,
            LikeDislikeReviewUseCase likeDislikeReviewUseCase,
            SessionHandler sessionHandler) {
        this.getInboxReputationDetailUseCase = getInboxReputationDetailUseCase;
        this.sendSmileyReputationUseCase = sendSmileyReputationUseCase;
        this.favoriteShopUseCase = favoriteShopUseCase;
        this.deleteReviewResponseUseCase = deleteReviewResponseUseCase;
        this.sendReplyReviewUseCase = sendReplyReviewUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void attachView(InboxReputationDetail.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getInboxReputationDetailUseCase.unsubscribe();
        sendSmileyReputationUseCase.unsubscribe();
        favoriteShopUseCase.unsubscribe();
        deleteReviewResponseUseCase.unsubscribe();
        sendReplyReviewUseCase.unsubscribe();
    }

    @Override
    public void getInboxDetail(String reputationId, int tab) {
        viewListener.showLoading();
        getInboxReputationDetailUseCase.execute(
                GetInboxReputationDetailUseCase.getParam(reputationId,
                        sessionHandler.getLoginID(),
                        tab),
                new GetInboxReputationDetailSubscriber(viewListener));
    }


    @Override
    public void sendSmiley(String reputationId, String score, int role) {
        viewListener.showLoadingDialog();
        sendSmileyReputationUseCase.execute(SendSmileyReputationUseCase.getParam(reputationId,
                score,
                role),
                new SendSmileySubscriber(viewListener, score));
    }

    @Override
    public void onFavoriteShopClicked(int shopId) {
        viewListener.showLoadingDialog();
        favoriteShopUseCase.execute(FavoriteShopUseCase.getParam(shopId,
                SRC_INBOX_REPUTATION_DETAIL), new
                FavoriteShopSubscriber(viewListener));
    }

    @Override
    public void deleteReviewResponse(String reviewId, String productId, String shopId, String
            reputationId) {
        viewListener.showLoadingDialog();
        deleteReviewResponseUseCase.execute(DeleteReviewResponseUseCase.getParam(
                reviewId,
                productId,
                shopId,
                reputationId), new DeleteReviewResponseSubscriber(viewListener));
    }

    @Override
    public void sendReplyReview(int reputationId, String productId, int shopId,
                                String reviewId, String replyReview) {
        viewListener.showLoadingDialog();
        sendReplyReviewUseCase.execute(SendReplyReviewUseCase.getParam(
                String.valueOf(reputationId),
                productId,
                String.valueOf(shopId),
                reviewId,
                replyReview
        ), new ReplyReviewSubscriber(viewListener));
    }

    public void refreshPage(String reputationId, int tab) {
        viewListener.showRefresh();
        getInboxReputationDetailUseCase.execute(
                GetInboxReputationDetailUseCase.getParam(reputationId,
                        sessionHandler.getLoginID(),
                        tab),
                new RefreshInboxReputationDetailSubscriber(viewListener));
    }

}

