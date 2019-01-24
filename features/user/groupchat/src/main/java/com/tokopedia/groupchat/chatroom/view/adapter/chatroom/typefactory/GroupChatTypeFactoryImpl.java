package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.AdminAnnouncementViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.ChatViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.GeneratedMessageViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.GroupChatPointsViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.ImageAnnouncementViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.PendingChatViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.SprintSaleViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.UserActionViewHolder;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.VoteAnnouncementViewHolder;
import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.AdminAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GeneratedMessageViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ImageAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.UserActionViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VibrateViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayCloseViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatTypeFactoryImpl extends BaseAdapterTypeFactory implements GroupChatTypeFactory {

    ChatroomContract.View.ImageAnnouncementViewHolderListener imageListener;
    ChatroomContract.View.VoteAnnouncementViewHolderListener voteAnnouncementViewHolderListener;
    ChatroomContract.View.SprintSaleViewHolderListener sprintSaleViewHolderListener;
    ChatroomContract.View.GroupChatPointsViewHolderListener groupChatPointsViewHolderListener;


    public GroupChatTypeFactoryImpl(GroupChatFragment fragment) {
        imageListener = fragment;
        voteAnnouncementViewHolderListener = fragment;
        sprintSaleViewHolderListener = fragment;
        groupChatPointsViewHolderListener = fragment;
    }

    @Override
    public int type(AdminAnnouncementViewModel adminAnnouncementViewModel) {
        return AdminAnnouncementViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatViewModel myChatViewModel) {
        return ChatViewHolder.LAYOUT;
    }

    @Override
    public int type(PendingChatViewModel pendingChatViewModel) {
        return PendingChatViewHolder.LAYOUT;
    }

    @Override
    public int type(UserActionViewModel userActionViewModel) {
        return UserActionViewHolder.LAYOUT;
    }

    @Override
    public int type(ImageAnnouncementViewModel imageViewModel) {
        return ImageAnnouncementViewHolder.LAYOUT;
    }

    @Override
    public int type(VoteAnnouncementViewModel voteAnnouncementViewModel) {
        return VoteAnnouncementViewHolder.LAYOUT;
    }

    @Override
    public int type(SprintSaleAnnouncementViewModel flashSaleViewModel) {
        return SprintSaleViewHolder.LAYOUT;
    }

    @Override
    public int type(GroupChatPointsViewModel groupChatPointsViewModel) {
        return GroupChatPointsViewHolder.LAYOUT;
    }

    @Override
    public int type(VibrateViewModel vibrateViewModel) {
        return 0;
    }

    @Override
    public int type(GeneratedMessageViewModel generatedMessageViewModel) {
        return GeneratedMessageViewHolder.LAYOUT;
    }

    @Override
    public int type(OverlayViewModel viewModel) {
        return 0;
    }

    @Override
    public int type(OverlayCloseViewModel overlayCloseViewModel) {
        return 0;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;

        if (type == ChatViewHolder.LAYOUT) {
            viewHolder = new ChatViewHolder(parent);
        } else if (type == AdminAnnouncementViewHolder.LAYOUT) {
            viewHolder = new AdminAnnouncementViewHolder(parent);
        } else if (type == PendingChatViewHolder.LAYOUT) {
            viewHolder = new PendingChatViewHolder(parent);
        } else if (type == UserActionViewHolder.LAYOUT) {
            viewHolder = new UserActionViewHolder(parent);
        } else if (type == ImageAnnouncementViewHolder.LAYOUT) {
            viewHolder = new ImageAnnouncementViewHolder(parent, imageListener);
        } else if (type == VoteAnnouncementViewHolder.LAYOUT) {
            viewHolder = new VoteAnnouncementViewHolder(parent, voteAnnouncementViewHolderListener);
        } else if (type == SprintSaleViewHolder.LAYOUT) {
            viewHolder = new SprintSaleViewHolder(parent, sprintSaleViewHolderListener);
        } else if (type == GeneratedMessageViewHolder.LAYOUT) {
            viewHolder = new GeneratedMessageViewHolder(parent);
        } else if (type == GroupChatPointsViewHolder.LAYOUT) {
            viewHolder = new GroupChatPointsViewHolder(parent, groupChatPointsViewHolderListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
