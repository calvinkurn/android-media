package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemErrorPromoPageBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoErrorStateUiModel

class PromoErrorViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemErrorPromoPageBinding,
                           private val listener: PromoCheckoutActionListener)
    : AbstractViewHolder<PromoErrorStateUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_error_promo_page
    }

    override fun bind(element: PromoErrorStateUiModel) {
        with(viewBinding) {
            errorStatePromo.apply {
                setTitle(element.uiData.title)
                setImageUrl(element.uiData.imageUrl)
                setDescription(element.uiData.description)
                setPrimaryCTAText(element.uiData.buttonText)
                setPrimaryCTAClickListener {
                    listener.onClickErrorStateButton(element.uiData.buttonDestination)
                }
            }
        }
    }
}