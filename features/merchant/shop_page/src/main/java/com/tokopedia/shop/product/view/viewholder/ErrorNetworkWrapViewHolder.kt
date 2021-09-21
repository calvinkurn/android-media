package com.tokopedia.shop.product.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import androidx.annotation.LayoutRes
import com.tokopedia.shop.R

class ErrorNetworkWrapViewHolder(itemView: View?) : ErrorNetworkViewHolder(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.partial_empty_wrap_page_error
    }
}