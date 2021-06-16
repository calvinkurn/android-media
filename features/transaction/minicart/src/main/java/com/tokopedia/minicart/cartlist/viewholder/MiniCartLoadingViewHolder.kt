package com.tokopedia.minicart.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.minicart.R

class MiniCartLoadingViewHolder(private val view: View)
    : LoadingViewholder(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_loading
    }

}