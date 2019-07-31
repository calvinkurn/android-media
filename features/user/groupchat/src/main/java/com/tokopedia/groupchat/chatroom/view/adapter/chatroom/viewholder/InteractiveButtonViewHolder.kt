package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.DynamicButtonTypeFactory
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract

/**
 * @author : Steven 22/05/19
 */

class InteractiveButtonViewHolder(
        itemView: View,
        var interactiveButtonListener: ChatroomContract.DynamicButtonItem.InteractiveButtonListener
) : BaseDynamicButtonViewHolder<Visitable<DynamicButtonTypeFactory>>(itemView) {


    var interactionButton: LottieAnimationView

    init {
        interactionButton = itemView.findViewById(R.id.interaction_button)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.interactive_icon_item
    }
    override fun bind(element: Visitable<DynamicButtonTypeFactory>) {
        interactiveButtonListener.onInteractiveButtonViewed(interactionButton)
        interactionButton.setOnClickListener {
            interactiveButtonListener.onInteractiveButtonClicked(interactionButton)
            interactionButton.playAnimation()
        }
    }
}