package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory

import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.BaseDynamicButtonViewHolder
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.DynamicButtonViewHolder
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.InteractiveButtonViewHolder
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.InteractiveButton

/**
 * @author : Steven 22/05/19
 */
class DynamicButtonTypeFactoryImpl(
        var dynamicButtonClickListener: ChatroomContract.DynamicButtonItem.DynamicButtonListener,
        var interactiveButtonClickListener: ChatroomContract.DynamicButtonItem.InteractiveButtonListener,
        var interactionGuideline: FrameLayout
) : DynamicButtonTypeFactory{

    override fun type(viewModel: DynamicButton): Int {
        return DynamicButtonViewHolder.LAYOUT
    }

    override fun type(viewModel: InteractiveButton): Int {
        return InteractiveButtonViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): BaseDynamicButtonViewHolder<Visitable<DynamicButtonTypeFactory>> {

        return when (viewType) {
            InteractiveButtonViewHolder.LAYOUT -> InteractiveButtonViewHolder(view, interactiveButtonClickListener)
            else -> DynamicButtonViewHolder(view, dynamicButtonClickListener)
        }
    }

}