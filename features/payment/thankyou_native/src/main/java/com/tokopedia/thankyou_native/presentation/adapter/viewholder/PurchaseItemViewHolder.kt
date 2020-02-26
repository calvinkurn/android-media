package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.PurchaseItemAdapterModel

class PurchaseItemViewHolder(val itemView: View) : AbstractViewHolder<PurchaseItemAdapterModel>(itemView) {

    override fun bind(element: PurchaseItemAdapterModel?) {
    }


    companion object {
        val LAYOUT_ID = R.layout.thank_item_purchased
    }
}