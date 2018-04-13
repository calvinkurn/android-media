package com.tokopedia.groupchat.chatroom.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.groupchat.chatroom.view.listener.ChannelVoteContract;
import com.tokopedia.groupchat.common.util.GroupChatErrorHandler;
import com.tokopedia.groupchat.vote.domain.usecase.SendVoteUseCase;
import com.tokopedia.groupchat.vote.view.model.VoteStatisticViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 2/6/18.
 */

public class ChannelVotePresenter extends BaseDaggerPresenter<ChannelVoteContract.View> implements
        ChannelVoteContract.Presenter {

    private final SendVoteUseCase sendVoteUseCase;

    @Inject
    public ChannelVotePresenter(SendVoteUseCase sendVoteUseCase) {
        this.sendVoteUseCase = sendVoteUseCase;
    }

    @Override
    public void sendVote(UserSession userSession, String pollId, boolean voted, final VoteViewModel element) {

        if(userSession == null || !userSession.isLoggedIn()) {
            getView().redirectToLogin();
            return;
        }
        if (voted) {
            getView().showHasVoted();
        } else {
            sendVoteUseCase.execute(SendVoteUseCase.createParams(pollId,
                    element.getOptionId()), new Subscriber<VoteStatisticViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (getView() != null) {
                        getView().onErrorVote(GroupChatErrorHandler.getErrorMessage(getView()
                                .getContext(), e, true));
                    }
                }

                @Override
                public void onNext(VoteStatisticViewModel voteStatisticViewModel) {
                    if (getView() != null) {
                        getView().onSuccessVote(element, voteStatisticViewModel);
                        getView().showSuccessVoted();
                    }
                }
            });
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        sendVoteUseCase.unsubscribe();
    }
}
