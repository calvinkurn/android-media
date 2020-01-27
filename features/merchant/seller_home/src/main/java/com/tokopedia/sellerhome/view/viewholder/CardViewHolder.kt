package com.tokopedia.sellerhome.view.viewholder

import android.os.Handler
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.model.CardWidgetUiModel
import kotlinx.android.synthetic.main.sah_card_widget.view.*

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class CardViewHolder(itemView: View?) : AbstractViewHolder<CardWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sah_card_widget
    }

    override fun bind(element: CardWidgetUiModel) {
        with(itemView) {
            tvCardTitle.text = element.title
            tvCardValue.text = "Rp999.999.999"
            tvCardSubValue.text = "Potensi +50 jt"
        }

        Handler().postDelayed({
            showShimmer(false)
            showOnError(true)
        }, 5000)

        Handler().postDelayed({
            showOnError(false)
            showViewComponent(true)
        }, 10000)

        showShimmer(true)
        showOnError(false)
        showViewComponent(false)
    }

    private fun showViewComponent(isShown: Boolean) {
        with(itemView) {
            val visibility = if (isShown) View.VISIBLE else View.INVISIBLE
            tvCardTitle.visibility = visibility
            tvCardValue.visibility = visibility
            tvCardSubValue.visibility = visibility
            if (isShown) tvCardValue.text = "Rp999.999.999"
        }
    }

    private fun showOnError(isError: Boolean) {
        if (!isError) return
        with(itemView) {
            tvCardTitle.visibility = View.VISIBLE
            tvCardValue.visibility = View.VISIBLE
            tvCardValue.text = context.getString(R.string.sah_card_on_error)
        }
    }

    private fun showShimmer(isLoading: Boolean) {
        with(itemView) {
            val visibility = if (isLoading) View.VISIBLE else View.GONE
            shimmerCardTitle.visibility = visibility
            shimmerCardValue.visibility = visibility
        }
    }
}