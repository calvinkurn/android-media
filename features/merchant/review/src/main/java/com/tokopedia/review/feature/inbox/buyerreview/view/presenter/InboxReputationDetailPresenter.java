package com.tokopedia.review.feature.inbox.buyerreview.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.DeleteReviewResponseUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendReplyReviewUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendSmileyReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail;
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.DeleteReviewResponseSubscriber;
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.GetInboxReputationDetailSubscriber;
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.RefreshInboxReputationDetailSubscriber;
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.ReplyReviewSubscriber;
import com.tokopedia.review.feature.inbox.buyerreview.view.subscriber.SendSmileySubscriber;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailPresenter
        extends BaseDaggerPresenter<InboxReputationDetail.View>
        implements InboxReputationDetail.Presenter {

    private final GetInboxReputationDetailUseCase getInboxReputationDetailUseCase;
    private final UserSessionInterface userSession;
    private final SendSmileyReputationUseCase sendSmileyReputationUseCase;
    private final DeleteReviewResponseUseCase deleteReviewResponseUseCase;
    private final SendReplyReviewUseCase sendReplyReviewUseCase;
    private InboxReputationDetail.View viewListener;

    @Inject
    InboxReputationDetailPresenter(
            GetInboxReputationDetailUseCase getInboxReputationDetailUseCase,
            SendSmileyReputationUseCase sendSmileyReputationUseCase,
            DeleteReviewResponseUseCase deleteReviewResponseUseCase,
            SendReplyReviewUseCase sendReplyReviewUseCase,
            UserSessionInterface userSession) {
        this.getInboxReputationDetailUseCase = getInboxReputationDetailUseCase;
        this.sendSmileyReputationUseCase = sendSmileyReputationUseCase;
        this.deleteReviewResponseUseCase = deleteReviewResponseUseCase;
        this.sendReplyReviewUseCase = sendReplyReviewUseCase;
        this.userSession = userSession;
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
        deleteReviewResponseUseCase.unsubscribe();
        sendReplyReviewUseCase.unsubscribe();
    }

    @Override
    public void getInboxDetail(String reputationId, int tab) {
        viewListener.showLoading();
        getInboxReputationDetailUseCase.execute(
                GetInboxReputationDetailUseCase.getParam(reputationId,
                        userSession.getUserId(),
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
                        userSession.getUserId(),
                        tab),
                new RefreshInboxReputationDetailSubscriber(viewListener));
    }
}

