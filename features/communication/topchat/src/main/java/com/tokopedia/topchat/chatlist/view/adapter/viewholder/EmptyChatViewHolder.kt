package com.tokopedia.topchat.chatlist.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.analytic.ChatListAnalytic
import com.tokopedia.topchat.chatlist.view.uimodel.EmptyChatModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * @author : Steven 2019-08-07
 */
class EmptyChatViewHolder constructor(
        itemView: View,
        private val chatListAnalytics: ChatListAnalytic
) : AbstractViewHolder<EmptyChatModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.empty_chat
    }

    val title: Typography = itemView.findViewById(R.id.title_empty_chat_list)
    val btnCta: UnifyButton = itemView.findViewById(R.id.btn_cta)
    val subtitle: Typography = itemView.findViewById(R.id.subtitle)
    val image: ImageView = itemView.findViewById(R.id.thumbnail_empty_chat_list)

    override fun bind(element: EmptyChatModel) {
        bindText(element)
        bindDescription(element)
        bindImage(element)
        bindCta(element)
        bindTrackView(element)
    }

    private fun bindText(element: EmptyChatModel) {
        title.text = element.title
    }

    private fun bindDescription(element: EmptyChatModel) {
        subtitle.text = element.body
    }

    private fun bindImage(element: EmptyChatModel) {
        image.loadImage(element.image) {
            setErrorDrawable(R.drawable.empty_chat)
        }
    }

    private fun bindCta(element: EmptyChatModel) {
        btnCta.apply {
            if (element.ctaText.isEmpty() || element.ctaApplink.isEmpty()) {
                hide()
            } else {
                show()
            }
            text = element.ctaText
            setOnClickListener {
                if (element.isTopAds) {
                    chatListAnalytics.eventClickCtaTopAds()
                }
                RouteManager.route(it.context, element.ctaApplink)
            }
        }
    }

    private fun bindTrackView(element: EmptyChatModel) {
        if (!element.isTopAds) return
        chatListAnalytics.eventViewCtaTopAds()
    }
}
