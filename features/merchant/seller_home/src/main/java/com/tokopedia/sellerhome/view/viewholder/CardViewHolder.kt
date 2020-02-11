package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.util.parseAsHtml
import com.tokopedia.sellerhome.view.model.CardWidgetUiModel
import kotlinx.android.synthetic.main.sah_card_widget.view.*

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class CardViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<CardWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sah_card_widget
    }

    override fun bind(element: CardWidgetUiModel) {
        observeState(element)
        listener.getCardData()

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
            tvCardTitle.text = element.title
            tvCardValue.text = element.data?.value ?: "0"
            tvCardSubValue.text = element.data?.description?.parseAsHtml()

            setOnClickListener {
                if (element.appLink.isNotBlank()) {
                    RouteManager.route(context, element.appLink)
                }
            }
        }
    }

    private fun showOnError(isError: Boolean) {
        if (!isError) return
        with(itemView) {
            tvCardTitle.visible()
            tvCardValue.visible()
            tvCardValue.text = context.getString(R.string.sah_card_on_error)
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

    interface Listener {
        fun getCardData()
    }
}