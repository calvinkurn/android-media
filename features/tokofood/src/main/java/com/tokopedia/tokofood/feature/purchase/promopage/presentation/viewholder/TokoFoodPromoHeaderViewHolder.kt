package com.tokopedia.tokofood.feature.purchase.promopage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodPromoListHeaderBinding
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoHeaderUiModel

class TokoFoodPromoHeaderViewHolder(private val viewBinding: ItemTokofoodPromoListHeaderBinding)
    : AbstractViewHolder<TokoFoodPromoHeaderUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_promo_list_header
    }

    override fun bind(element: TokoFoodPromoHeaderUiModel) {
        with(viewBinding) {
            iconPromoListHeaderTokofood.setImageUrl(element.iconUrl)
            labelPromoListHeaderTitleTokofood.text = element.title
        }
    }

}
