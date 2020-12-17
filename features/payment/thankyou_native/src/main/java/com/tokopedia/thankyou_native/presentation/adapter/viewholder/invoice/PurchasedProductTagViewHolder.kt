package com.tokopedia.thankyou_native.presentation.adapter.viewholder.invoice

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.PurchasedProductTag

class PurchasedProductTagViewHolder(val view: View) : AbstractViewHolder<PurchasedProductTag>(view) {

    override fun bind(element: PurchasedProductTag?) {
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_item_invoice_product
    }
}