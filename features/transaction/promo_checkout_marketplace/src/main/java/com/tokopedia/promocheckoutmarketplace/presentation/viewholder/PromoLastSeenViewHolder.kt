package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoLastSeenBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutLastSeenListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoLastSeenItemUiModel

class PromoLastSeenViewHolder(val viewBinding: PromoCheckoutMarketplaceModuleItemPromoLastSeenBinding, val actionListener: PromoCheckoutLastSeenListener) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bindData(data: PromoLastSeenItemUiModel) {
        with(viewBinding) {
            labelPromoItemTitle.text = data.uiData.promoTitle
            labelPromoCode.text = data.uiData.promoCode
            itemView.setOnClickListener {
                actionListener.onClickItem(data)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_last_seen
    }

}