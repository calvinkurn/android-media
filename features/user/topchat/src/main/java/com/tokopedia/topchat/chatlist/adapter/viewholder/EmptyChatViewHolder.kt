package com.tokopedia.topchat.chatlist.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.unifyprinciples.Typography

/**
 * @author : Steven 2019-08-07
 */
class EmptyChatViewHolder(itemView: View, var listener: ChatListItemListener) : AbstractViewHolder<EmptyChatModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.topchat.R.layout.empty_chat

    }

    val title: Typography = itemView.findViewById(com.tokopedia.design.R.id.title)
    val subtitle: Typography = itemView.findViewById(com.tokopedia.chat_common.R.id.subtitle)
    val image: SquareImageView = itemView.findViewById(com.tokopedia.topchat.R.id.thumbnail)


    override fun bind(element: EmptyChatModel) {
        title.text = element.title
        subtitle.text = element.body
        ImageHandler.loadImage2(image, element.image, com.tokopedia.topchat.R.drawable.empty_chat)
    }
}