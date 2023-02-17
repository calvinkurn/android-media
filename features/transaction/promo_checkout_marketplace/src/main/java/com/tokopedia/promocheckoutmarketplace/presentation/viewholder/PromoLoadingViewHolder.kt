package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemLoadingPromoPageBinding

class PromoLoadingViewHolder(val viewBinding: PromoCheckoutMarketplaceModuleItemLoadingPromoPageBinding) : LoadingViewholder(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_loading_promo_page
    }
}
