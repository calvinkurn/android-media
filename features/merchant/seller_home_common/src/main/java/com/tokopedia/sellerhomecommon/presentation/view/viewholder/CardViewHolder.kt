package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.util.TypedValue
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import kotlinx.android.synthetic.main.shc_card_widget.view.*

/**
 * Created By @ilhamsuaib on 19/05/20
 */

class CardViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<CardWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_card_widget
        private const val GO_TO_INBOX_REVIEW = "GO_TO_INBOX_REVIEW"
    }

    override fun bind(element: CardWidgetUiModel) {
        observeState(element)

        itemView.tvCardTitle.text = element.title
    }

    private fun observeState(element: CardWidgetUiModel) {
        val data = element.data
        when {
            null == data -> {
                showViewComponent(element, false)
                showOnError(false)
                showShimmer(true)
            }
            data.error.isNotBlank() -> {
                showShimmer(false)
                showOnError(true)
                listener.setOnErrorWidget(adapterPosition, element)
            }
            else -> {
                showOnError(false)
                showShimmer(false)
                showViewComponent(element, true)
            }
        }
    }

    private fun showViewComponent(element: CardWidgetUiModel, isShown: Boolean) {
        with(itemView) {
            val visibility = if (isShown) View.VISIBLE else View.INVISIBLE
            tvCardTitle.visibility = visibility
            tvCardValue.visibility = visibility
            tvCardSubValue.visibility = visibility
            val value = element.data?.value
            if (isShown) tvCardValue.text = if (value.isNullOrBlank()) "0" else value
        }

        if (!isShown) return

        with(itemView) {
            if (element.appLink.isNotBlank()) {
                val selectableItemBg = TypedValue()
                context.theme.resolveAttribute(android.R.attr.selectableItemBackground,
                        selectableItemBg, true)
                containerCard.setBackgroundResource(selectableItemBg.resourceId)
            } else
                containerCard.setBackgroundColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))

            tvCardTitle.text = element.title
            tvCardValue.text = element.data?.value ?: "0"
            tvCardSubValue.text = element.data?.description?.parseAsHtml()
            addOnImpressionListener(element.impressHolder) {
                listener.sendCardImpressionEvent(element)
            }

            setOnClickListener {
                if (element.appLink.isNotBlank()) {
                    if (RouteManager.route(context, element.appLink)) {
                        listener.sendCardClickTracking(element)
                    }
                }
            }
        }
    }

    private fun showOnError(isError: Boolean) {
        if (!isError) return
        with(itemView) {
            tvCardTitle.visible()
            tvCardValue.visible()
            tvCardValue.text = context.getString(R.string.shc_load_failed)
            tvCardSubValue.text = ""
        }
    }

    private fun showShimmer(isLoading: Boolean) {
        with(itemView) {
            val visibility = if (isLoading) View.VISIBLE else View.GONE
            shimmerCardTitle.visibility = visibility
            shimmerCardValue.visibility = visibility
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendCardImpressionEvent(model: CardWidgetUiModel) {}

        fun sendCardClickTracking(model: CardWidgetUiModel) {}
    }
}