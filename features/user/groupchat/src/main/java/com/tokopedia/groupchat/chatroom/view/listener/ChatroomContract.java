package com.tokopedia.groupchat.chatroom.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;

/**
 * @author by nisie on 2/6/18.
 */

public interface ChatroomContract {

    interface QuickReply extends CustomerView {
        void addQuickReply(String text);
    }

    interface ChatItem extends CustomerView {
        interface ImageAnnouncementViewHolderListener {
            void onImageAnnouncementClicked(String url);
        }

        interface VoteAnnouncementViewHolderListener {
            void onVoteComponentClicked(String type, String name);
        }

        interface SprintSaleViewHolderListener {
            void onSprintSaleProductClicked(SprintSaleProductViewModel sprintSaleViewModel, int
                    position);

            void onSprintSaleComponentClicked(SprintSaleAnnouncementViewModel sprintSaleAnnouncementViewModel);

            void onSprintSaleIconClicked(SprintSaleViewModel sprintSaleViewModel);

        }

        interface GroupChatPointsViewHolderListener{
            void onPointsClicked(String url);
        }
    }

    interface View extends CustomerView {

        Context getContext();

        void onErrorSendMessage(PendingChatViewModel pendingChatViewModel, String errorMessage);

        void onSuccessSendMessage(PendingChatViewModel pendingChatViewModel);

        void setSprintSaleIcon(SprintSaleViewModel sprintSaleViewModel);

        void autoAddSprintSaleAnnouncement(SprintSaleViewModel sprintSaleViewModel, ChannelInfoViewModel channelInfoViewModel);

    }

    interface Presenter extends CustomerPresenter<View> {

        String checkText(String replyText);
    }
}
