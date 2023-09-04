package com.tokopedia.cartrevamp.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cartrevamp.view.uimodel.CartLoadingHolderData

/**
 * Created by Irfan Khoirul on 2019-06-26.
 */

class CartLoadingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = com.tokopedia.baselist.R.layout.loading_layout
    }

    fun bind(element: CartLoadingHolderData) {
        // no op
    }
}
