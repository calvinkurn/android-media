package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoListHeaderDisabledBinding
import com.tokopedia.promocheckoutmarketplace.presentation.IconHelper
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListHeaderUiModel

class PromoListHeaderDisabledViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoListHeaderDisabledBinding,
                                        private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListHeaderUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_list_header_disabled
    }

    override fun bind(element: PromoListHeaderUiModel) {
        with(viewBinding) {
            if (element.uiData.iconUnify.isNotBlank()) {
                iconPromoListHeader.setImage(IconHelper.getIcon(element.uiData.iconUnify))
                iconPromoListHeader.show()
            } else {
                iconPromoListHeader.gone()
            }

            labelPromoListHeaderTitle.text = element.uiData.title

            itemView.setOnClickListener {}
        }
    }

}