//package com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory
//
//import android.view.View
//
//import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
//import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
//import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.AdminAnnouncementViewHolder
//import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.ChatViewHolder
//import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.GeneratedMessageViewHolder
//import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.GroupChatPointsViewHolder
//import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.ImageAnnouncementViewHolder
//import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.PendingChatViewHolder
//import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.SprintSaleViewHolder
//import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.UserActionViewHolder
//import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.VoteAnnouncementViewHolder
//import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatFragment
//import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.AdminAnnouncementViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GeneratedMessageViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ImageAnnouncementViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.UserActionViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VibrateViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayCloseViewModel
//import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
//
///**
// * @author by nisie on 2/7/18.
// */
//
//class GroupChatTypeFactoryImpl(fragment: GroupChatFragment) : BaseAdapterTypeFactory(), GroupChatTypeFactory {
//
//    internal var imageListener: ChatroomContract.View.ImageAnnouncementViewHolderListener
//    internal var voteAnnouncementViewHolderListener: ChatroomContract.View.VoteAnnouncementViewHolderListener
//    internal var sprintSaleViewHolderListener: ChatroomContract.View.SprintSaleViewHolderListener
//    internal var groupChatPointsViewHolderListener: ChatroomContract.View.GroupChatPointsViewHolderListener
//
//
//    init {
//        imageListener = fragment
//        voteAnnouncementViewHolderListener = fragment
//        sprintSaleViewHolderListener = fragment
//        groupChatPointsViewHolderListener = fragment
//    }
//
//    override fun type(adminAnnouncementViewModel: AdminAnnouncementViewModel): Int {
//        return AdminAnnouncementViewHolder.LAYOUT
//    }
//
//    override fun type(myChatViewModel: ChatViewModel): Int {
//        return ChatViewHolder.LAYOUT
//    }
//
//    override fun type(pendingChatViewModel: PendingChatViewModel): Int {
//        return PendingChatViewHolder.LAYOUT
//    }
//
//    override fun type(userActionViewModel: UserActionViewModel): Int {
//        return UserActionViewHolder.LAYOUT
//    }
//
//    override fun type(imageViewModel: ImageAnnouncementViewModel): Int {
//        return ImageAnnouncementViewHolder.LAYOUT
//    }
//
//    override fun type(voteAnnouncementViewModel: VoteAnnouncementViewModel): Int {
//        return VoteAnnouncementViewHolder.LAYOUT
//    }
//
//    override fun type(flashSaleViewModel: SprintSaleAnnouncementViewModel): Int {
//        return SprintSaleViewHolder.LAYOUT
//    }
//
//    override fun type(groupChatPointsViewModel: GroupChatPointsViewModel): Int {
//        return GroupChatPointsViewHolder.LAYOUT
//    }
//
//    override fun type(vibrateViewModel: VibrateViewModel): Int {
//        return 0
//    }
//
//    override fun type(generatedMessageViewModel: GeneratedMessageViewModel): Int {
//        return GeneratedMessageViewHolder.LAYOUT
//    }
//
//    override fun type(viewModel: OverlayViewModel): Int {
//        return 0
//    }
//
//    override fun type(overlayCloseViewModel: OverlayCloseViewModel): Int {
//        return 0
//    }
//
//    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
//        val viewHolder: AbstractViewHolder<*>
//
//        if (type == ChatViewHolder.LAYOUT) {
//            viewHolder = ChatViewHolder(parent)
//        } else if (type == AdminAnnouncementViewHolder.LAYOUT) {
//            viewHolder = AdminAnnouncementViewHolder(parent)
//        } else if (type == PendingChatViewHolder.LAYOUT) {
//            viewHolder = PendingChatViewHolder(parent)
//        } else if (type == UserActionViewHolder.LAYOUT) {
//            viewHolder = UserActionViewHolder(parent)
//        } else if (type == ImageAnnouncementViewHolder.LAYOUT) {
//            viewHolder = ImageAnnouncementViewHolder(parent, imageListener)
//        } else if (type == VoteAnnouncementViewHolder.LAYOUT) {
//            viewHolder = VoteAnnouncementViewHolder(parent, voteAnnouncementViewHolderListener)
//        } else if (type == SprintSaleViewHolder.LAYOUT) {
//            viewHolder = SprintSaleViewHolder(parent, sprintSaleViewHolderListener)
//        } else if (type == GeneratedMessageViewHolder.LAYOUT) {
//            viewHolder = GeneratedMessageViewHolder(parent)
//        } else if (type == GroupChatPointsViewHolder.LAYOUT) {
//            viewHolder = GroupChatPointsViewHolder(parent, groupChatPointsViewHolderListener)
//        } else {
//            viewHolder = super.createViewHolder(parent, type)
//        }
//        return viewHolder
//    }
//}
