package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.*
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayCloseViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel

/**
 * @author by nisie on 2/7/18.
 */

class GroupChatTypeFactoryImpl(imageListen: ChatroomContract.ChatItem.ImageAnnouncementViewHolderListener,
                               voteAnnouncementListen: ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener,
                               sprintSaleViewHolderListen: ChatroomContract.ChatItem.SprintSaleViewHolderListener,
                               groupChatPointsViewHolderListen: ChatroomContract.ChatItem.GroupChatPointsViewHolderListener
                               ) : BaseAdapterTypeFactory(), GroupChatTypeFactory {

    internal var imageListener: ChatroomContract.ChatItem.ImageAnnouncementViewHolderListener
    internal var voteAnnouncementViewHolderListener: ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener
    internal var sprintSaleViewHolderListener: ChatroomContract.ChatItem.SprintSaleViewHolderListener
    internal var groupChatPointsViewHolderListener: ChatroomContract.ChatItem.GroupChatPointsViewHolderListener


    init {
        imageListener = imageListen
        voteAnnouncementViewHolderListener = voteAnnouncementListen
        sprintSaleViewHolderListener = sprintSaleViewHolderListen
        groupChatPointsViewHolderListener = groupChatPointsViewHolderListen
    }

    override fun type(adminAnnouncementViewModel: AdminAnnouncementViewModel): Int {
        return AdminAnnouncementViewHolder.LAYOUT
    }

    override fun type(myChatViewModel: ChatViewModel): Int {
        return PlayChatViewHolder.LAYOUT
//        return ChatViewHolder.LAYOUT
    }

    override fun type(pendingChatViewModel: PendingChatViewModel): Int {
        return PendingChatViewHolder.LAYOUT
    }

    override fun type(userActionViewModel: UserActionViewModel): Int {
        return UserActionViewHolder.LAYOUT
    }

    override fun type(imageViewModel: ImageAnnouncementViewModel): Int {
        return PlayImageAnnouncementViewHolder.LAYOUT
    }

    override fun type(voteAnnouncementViewModel: VoteAnnouncementViewModel): Int {
        return PlayQuizAnnouncementViewHolder.LAYOUT
//        return VoteAnnouncementViewHolder.LAYOUT
    }

    override fun type(flashSaleViewModel: SprintSaleAnnouncementViewModel): Int {
        return SprintSaleViewHolder.LAYOUT
    }

    override fun type(groupChatPointsViewModel: GroupChatPointsViewModel): Int {
        return GroupChatPointsViewHolder.LAYOUT
    }

    override fun type(vibrateViewModel: VibrateViewModel): Int {
        return 0
    }

    override fun type(generatedMessageViewModel: GeneratedMessageViewModel): Int {
        return GeneratedMessageViewHolder.LAYOUT
    }

    override fun type(viewModel: OverlayViewModel): Int {
        return 0
    }

    override fun type(overlayCloseViewModel: OverlayCloseViewModel): Int {
        return 0
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>

//        if (type == ChatViewHolder.LAYOUT) {
//            viewHolder = ChatViewHolder(parent)
//        } else
        if (type == AdminAnnouncementViewHolder.LAYOUT) {
            viewHolder = AdminAnnouncementViewHolder(parent)
        } else if (type == PendingChatViewHolder.LAYOUT) {
            viewHolder = PendingChatViewHolder(parent)
        } else if (type == UserActionViewHolder.LAYOUT) {
            viewHolder = UserActionViewHolder(parent)
//        } else if (type == ImageAnnouncementViewHolder.LAYOUT) {
//            viewHolder = ImageAnnouncementViewHolder(parent, imageListener)
//        } else if (type == VoteAnnouncementViewHolder.LAYOUT) {
//            viewHolder = VoteAnnouncementViewHolder(parent, voteAnnouncementViewHolderListener)
        } else if (type == SprintSaleViewHolder.LAYOUT) {
            viewHolder = SprintSaleViewHolder(parent, sprintSaleViewHolderListener)
        } else if (type == GeneratedMessageViewHolder.LAYOUT) {
            viewHolder = GeneratedMessageViewHolder(parent)
        } else if (type == GroupChatPointsViewHolder.LAYOUT) {
            viewHolder = GroupChatPointsViewHolder(parent, groupChatPointsViewHolderListener)
        } else if (type == PlayChatViewHolder.LAYOUT){
            viewHolder = PlayChatViewHolder(parent)
        } else if (type == PlayQuizAnnouncementViewHolder.LAYOUT){
            viewHolder = PlayQuizAnnouncementViewHolder(parent, voteAnnouncementViewHolderListener)
        } else if (type == PlayImageAnnouncementViewHolder.LAYOUT){
            viewHolder = PlayImageAnnouncementViewHolder(parent, imageListener)
        } else {
            viewHolder = super.createViewHolder(parent, type)
        }
        return viewHolder
    }
}
