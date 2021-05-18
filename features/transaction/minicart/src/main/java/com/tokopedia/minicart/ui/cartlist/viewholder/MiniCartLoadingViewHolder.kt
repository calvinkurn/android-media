package com.tokopedia.minicart.ui.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.ui.cartlist.MiniCartListActionListener

class MiniCartLoadingViewHolder(private val view: View)
    : LoadingViewholder(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_loading
    }

}