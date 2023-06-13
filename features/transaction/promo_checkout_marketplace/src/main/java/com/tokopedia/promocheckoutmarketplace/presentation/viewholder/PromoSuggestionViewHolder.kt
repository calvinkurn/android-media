package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoSuggestionBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutSuggestionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoSuggestionItemUiModel

class PromoSuggestionViewHolder(val viewBinding: PromoCheckoutMarketplaceModuleItemPromoSuggestionBinding, val actionListener: PromoCheckoutSuggestionListener) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bindData(data: PromoSuggestionItemUiModel) {
        with(viewBinding) {
            labelPromoItemTitle.text = data.uiData.promoTitle
            labelPromoCode.text = data.uiData.promoCode
            itemView.setOnClickListener {
                actionListener.onClickItem(data)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_suggestion
    }
}
