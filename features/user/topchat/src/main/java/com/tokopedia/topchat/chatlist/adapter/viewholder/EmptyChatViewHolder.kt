package com.tokopedia.topchat.chatlist.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.empty_chat.view.*

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
    val image: SquareImageView = itemView.findViewById(R.id.thumbnail)

    override fun bind(element: EmptyChatModel) {
        title.text = element.title
        subtitle.text = element.body
        ImageHandler.loadImage2(image, element.image, R.drawable.empty_chat)
        bindCta(element)
    }

    private fun bindCta(element: EmptyChatModel) {
        if (element.ctaText.isEmpty() || element.ctaApplink.isEmpty()) {
            itemView.btnCta?.hide()
        }

        itemView.btnCta?.text = element.ctaText
        itemView.btnCta?.setOnClickListener {
            RouteManager.route(it.context, element.ctaApplink)
        }
    }
}