//package com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory
//
//import android.view.View
//
//import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
//import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
//import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.QuickReplyItemViewHolder
//import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatFragment
//import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
//import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel
//
///**
// * @author by StevenFredian on 07/06/18.
// */
//
//class QuickReplyTypeFactoryImpl(fragment: GroupChatFragment) : BaseAdapterTypeFactory(), QuickReplyTypeFactory {
//
//
//    private val listener: ChatroomContract.View
//
//    init {
//        listener = fragment
//    }
//
//    override fun type(groupChatQuickReplyViewModel: GroupChatQuickReplyItemViewModel): Int {
//        return QuickReplyItemViewHolder.LAYOUT
//    }
//
//    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
//        val viewHolder: AbstractViewHolder<*>
//
//        if (type == QuickReplyItemViewHolder.LAYOUT) {
//            viewHolder = QuickReplyItemViewHolder(parent, listener)
//        } else {
//            viewHolder = super.createViewHolder(parent, type)
//        }
//
//        return viewHolder
//    }
//
//}
