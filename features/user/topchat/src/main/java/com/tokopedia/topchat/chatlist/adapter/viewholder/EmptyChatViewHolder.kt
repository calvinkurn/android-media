package com.tokopedia.topchat.chatlist.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.unifyprinciples.Typography

/**
 * @author : Steven 2019-08-07
 */
class EmptyChatViewHolder(itemView: View, var listener: ChatListItemListener) : AbstractViewHolder<EmptyChatModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.empty_chat

    }

    val title: Typography = itemView.findViewById(R.id.title)
    val subtitle: Typography = itemView.findViewById(R.id.subtitle)


    override fun bind(element: EmptyChatModel) {
        title.text = element.title
        subtitle.text = element.body
    }
}