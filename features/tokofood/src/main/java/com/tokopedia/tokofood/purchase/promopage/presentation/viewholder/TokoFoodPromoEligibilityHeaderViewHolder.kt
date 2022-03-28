package com.tokopedia.tokofood.purchase.promopage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodPromoListAvailabilityHeaderBinding
import com.tokopedia.tokofood.purchase.promopage.presentation.TokoFoodPromoActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseAddressTokoFoodPurchaseUiModel

class TokoFoodPromoEligibilityHeaderViewHolder(private val viewBinding: ItemTokofoodPromoListAvailabilityHeaderBinding,
                                               private val listener: TokoFoodPromoActionListener)
    : AbstractViewHolder<TokoFoodPurchaseAddressTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_promo_list_availability_header
    }

    override fun bind(element: TokoFoodPurchaseAddressTokoFoodPurchaseUiModel) {

    }

}