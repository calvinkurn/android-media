package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.promocheckoutmarketplace.R

/**
 * Created by Irfan Khoirul on 30/01/19.
 */

class PromoLoadingViewHolder(val view: View) : LoadingViewholder(view) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_loading_promo_page
    }

}