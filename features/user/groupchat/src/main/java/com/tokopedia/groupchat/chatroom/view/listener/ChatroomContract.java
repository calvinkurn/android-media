package com.tokopedia.groupchat.chatroom.view.listener;

import android.content.Context;

import com.airbnb.lottie.LottieAnimationView;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ImageAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton;

/**
 * @author by nisie on 2/6/18.
 */

public interface ChatroomContract {

    interface QuickReply extends CustomerView {
        void addQuickReply(String text);
    }

    interface ChatItem extends CustomerView {
        interface ImageAnnouncementViewHolderListener {
            void onImageAnnouncementClicked(ImageAnnouncementViewModel image);
        }

        interface VoteAnnouncementViewHolderListener {
            void onVoteComponentClicked(String type, String name, String voteUrl);
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

    interface DynamicButtonItem extends CustomerView {
        interface DynamicButtonListener {
            void onDynamicButtonClicked(DynamicButton button);
        }

        interface InteractiveButtonListener {
            void onInteractiveButtonClicked(LottieAnimationView anchorView);
            void onInteractiveButtonViewed(LottieAnimationView anchorView);
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
