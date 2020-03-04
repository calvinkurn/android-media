package com.tokopedia.centralized_promo.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralized_promo.view.model.OnGoingPromoUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R.layout.sah_item_centralized_promo_on_going_promo
import kotlinx.android.synthetic.main.sah_item_centralized_promo_on_going_promo.view.*

class OnGoingPromoViewHolder(view: View?) : AbstractViewHolder<OnGoingPromoUiModel>(view) {

    companion object {
        val RES_LAYOUT = sah_item_centralized_promo_on_going_promo
    }

    override fun bind(element: OnGoingPromoUiModel) {
        with(itemView) {
            tvOnGoingPromoTitle.text = element.title
            tvOnGoingPromoStatus.text = element.status.text
            tvOnGoingPromoCount.text = element.status.count.toString()
            tvSeePastPromotion.text = element.footer.text

        }

        initListeners(element)
    }

    private fun initListeners(element: OnGoingPromoUiModel) {
        with(itemView) {
            tvOnGoingPromoTitle.setOnClickListener {
                openApplink(element.status.url)
            }
            tvSeePastPromotion.setOnClickListener {
                openApplink(element.footer.url)
            }
            ivCaret.setOnClickListener {
                openApplink(element.footer.url)
            }
        }
    }

    private fun openApplink(url: String) {
        with(itemView) {
            RouteManager.route(context, url)
        }
    }
}