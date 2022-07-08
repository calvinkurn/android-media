package com.tokopedia.tokofood.feature.purchase.promopage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodPromoListAvailabilityHeaderBinding
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoEligibilityHeaderUiModel

class TokoFoodPromoEligibilityHeaderViewHolder(viewBinding: ItemTokofoodPromoListAvailabilityHeaderBinding)
    : AbstractViewHolder<TokoFoodPromoEligibilityHeaderUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_promo_list_availability_header
    }

    override fun bind(element: TokoFoodPromoEligibilityHeaderUiModel) {}

}