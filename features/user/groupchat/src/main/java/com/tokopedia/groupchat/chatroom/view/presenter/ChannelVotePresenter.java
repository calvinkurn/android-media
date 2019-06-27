package com.tokopedia.groupchat.chatroom.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.groupchat.chatroom.view.listener.ChannelVoteContract;
import com.tokopedia.groupchat.common.util.GroupChatErrorHandler;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;
import com.tokopedia.vote.domain.usecase.SendVoteUseCase;
import com.tokopedia.user.session.UserSessionInterface;

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
    public void sendVote(UserSessionInterface userSession, String pollId, boolean voted, final VoteViewModel element, String groupChatToken) {

        if(userSession == null || !userSession.isLoggedIn()) {
            getView().redirectToLogin();
            return;
        }
        if (voted) {
            getView().showHasVoted();
        } else {
            sendVoteUseCase.execute(SendVoteUseCase.createParams(pollId,
                    element.getOptionId(), groupChatToken), new Subscriber<VoteStatisticDomainModel>() {
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
                public void onNext(VoteStatisticDomainModel voteStatisticViewModel) {
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
//        sendVoteUseCase.unsubscribe();
    }
}
