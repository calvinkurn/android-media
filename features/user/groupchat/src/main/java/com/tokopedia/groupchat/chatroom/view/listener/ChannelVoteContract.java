package com.tokopedia.groupchat.chatroom.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;
import com.tokopedia.user.session.UserSessionInterface;

/**
 * @author by nisie on 2/6/18.
 */

public interface ChannelVoteContract {

    interface View extends CustomerView {

        Context getContext();

        void showHasVoted();

        void showSuccessVoted();

        void onSuccessVote(VoteViewModel element, VoteStatisticDomainModel voteStatisticViewModel);

        void onErrorVote(String errorMessage);

        void redirectToLogin();

        void showVoteLayout(VoteInfoViewModel voteInfoViewModel);

        interface VoteOptionListener{
            void onVoteOptionClicked(VoteViewModel element);
        }

        interface VoteAnnouncementViewHolderListener {
            void onVoteComponentClicked(String type, String name);
        }
    }

    interface Presenter extends CustomerPresenter<View> {

        void sendVote(UserSessionInterface userSession, String pollId, boolean voted, VoteViewModel element, String groupChatToken);

    }
}
