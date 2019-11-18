package com.tokopedia.feedcomponent.view.adapter.viewholder.highlight

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_dynamic_post.view.*

/**
 * @author by yoasfs on 2019-08-06
 */
class HighlightViewHolder(val v: View,
                          val highlightListener: HighlightAdapter.HighlightListener,
                          val cardTitleListener: CardTitleView.CardTitleListener):
        AbstractViewHolder<HighlightViewModel>(v) {

    private lateinit var adapter: HighlightAdapter
    private lateinit var highlightRv: RecyclerView
    private lateinit var text: Typography
    private lateinit var badge: ImageView
    private lateinit var cta: Typography


    companion object {
        const val PAYLOAD_UPDATE_LIKE = 1121
        const val PAYLOAD_UPDATE_COMMENT = 1122
        @LayoutRes
        val LAYOUT = R.layout.item_feed_highlight
    }


    override fun bind(element: HighlightViewModel?) {
        if (element == null) {
            itemView.hide()
            return
        }
        highlightRv = itemView.findViewById(R.id.highlightRv)
        text = itemView.findViewById(R.id.text)
        badge = itemView.findViewById(R.id.badge)
        cta = itemView.findViewById(R.id.cta)
        element.cards.forEach { it.positionInFeed = adapterPosition }
        adapter = HighlightAdapter(element.cards, highlightListener)
        highlightRv.adapter = adapter
        highlightRv.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        highlightRv.isNestedScrollingEnabled = false
        bindTitle(element.title, element.template.cardhighlight.title)
    }

    override fun bind(element: HighlightViewModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }
        val payloadDataList: List<Any> = payloads[0]  as ArrayList<Any>
        if (payloadDataList.size > 0) {
            var columnNumber = 0
            if (payloadDataList.size > 1) {
                columnNumber = payloadDataList[1] as Int
            }

            when (payloadDataList[0] as Int) {
                PAYLOAD_UPDATE_LIKE, PAYLOAD_UPDATE_COMMENT -> {
                    adapter.notifyItemChanged(columnNumber)
                }
                else -> bind(element)
            }
        }
    }

    private fun bindTitle(title: Title, template: TemplateTitle) {
        itemView.cardTitle.shouldShowWithAction(shouldShowTitle(template)) {
            itemView.cardTitle.bind(title, template, adapterPosition)
        }
        itemView.cardTitle.listener = cardTitleListener
    }

    private fun shouldShowTitle(template: TemplateTitle): Boolean {
        return template.text || template.textBadge || template.ctaLink
    }
}