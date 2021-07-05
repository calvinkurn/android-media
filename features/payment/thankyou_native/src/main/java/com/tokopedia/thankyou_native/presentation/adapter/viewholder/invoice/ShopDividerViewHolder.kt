package com.tokopedia.thankyou_native.presentation.adapter.viewholder.invoice

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.ShopDivider

class ShopDividerViewHolder(val view: View) : AbstractViewHolder<ShopDivider>(view) {

    override fun bind(element: ShopDivider?) {
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_item_shop_divider
    }
}