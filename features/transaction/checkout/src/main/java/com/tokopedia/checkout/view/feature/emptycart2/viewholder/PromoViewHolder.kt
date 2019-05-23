package com.tokopedia.checkout.view.feature.emptycart2.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.datamodel.promostacking.AutoApplyStackData
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.EmptyCartPlaceholderUiModel
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.PromoUiModel
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import kotlinx.android.synthetic.main.item_empty_cart_promo.view.*

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class PromoViewHolder(val view: View, val listener: ActionListener) : AbstractViewHolder<PromoUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_empty_cart_promo
    }

    override fun bind(element: PromoUiModel) {
        val autoApplyStackData = AutoApplyStackData()
        autoApplyStackData.messageSuccess = element.messageSuccess
        autoApplyStackData.state = element.state
        autoApplyStackData.titleDescription = element.titleDescription
        autoApplyStackData.code = element.code

        if (autoApplyStackData.state.mapToStatePromoStackingCheckout() !== com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView.State.EMPTY) {
            itemView.ticker_promostacking_checkout_view.state = autoApplyStackData.state.mapToStatePromoStackingCheckout()
            itemView.ticker_promostacking_checkout_view.title = autoApplyStackData.titleDescription
            itemView.ticker_promostacking_checkout_view.desc = autoApplyStackData.messageSuccess
            itemView.ticker_promostacking_checkout_view.actionListener = object : TickerPromoStackingCheckoutView.ActionListener {
                override fun onClickUsePromo() {
                    //do nothing
                }

                override fun onResetPromoDiscount() {
                    listener.onClearPromo(autoApplyStackData.code)
                }

                override fun onClickDetailPromo() {
                    //do nothing
                }

                override fun onDisablePromoDiscount() {

                }
            }
            itemView.ticker_promostacking_checkout_view.setVisibility(View.VISIBLE)
        } else {
            itemView.ticker_promostacking_checkout_view.setVisibility(View.GONE)
        }
    }

}