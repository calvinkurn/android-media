package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.purchase_platform.R

/**
 * Created by Irfan Khoirul on 30/01/19.
 */

class PromoLoadingViewHolder(val view: View) : LoadingViewholder(view) {

    companion object {
        val LAYOUT = R.layout.item_loading_promo_page
    }

}