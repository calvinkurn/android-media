package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoEmptyBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEmptyStateUiModel

class PromoEmptyStateViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoEmptyBinding,
                                private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoEmptyStateUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_empty
    }

    override fun bind(element: PromoEmptyStateUiModel) {
        with(viewBinding) {
            emptyStatePromo.setTitle(element.uiData.title)
            emptyStatePromo.setDescription(element.uiData.description)
            emptyStatePromo.setImageUrl(element.uiData.imageUrl)

            if (element.uiState.isShowButton) {
                emptyStatePromo.setPrimaryCTAText(element.uiData.buttonText)
                emptyStatePromo.setPrimaryCTAClickListener {
                    listener.onClickEmptyStateButton(element)
                }
            } else {
                emptyStatePromo.setPrimaryCTAText("")
            }
        }
    }

}