package com.tokopedia.checkout.view.feature.cartlist.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartLoadingHolderData

/**
 * Created by Irfan Khoirul on 2019-06-26.
 */

class CartLoadingViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.loading_layout
    }

    fun bind(element: CartLoadingHolderData) {
    }

}