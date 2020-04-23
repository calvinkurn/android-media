package com.tokopedia.orderhistory.view.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.orderhistory.R
import com.tokopedia.orderhistory.data.Invoice
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx

class OrderHistoryViewHolder(itemView: View?, val listener: Listener) : AbstractViewHolder<Invoice>(itemView) {

    interface Listener {

    }

    override fun bind(element: Invoice?) {
        if (element == null) return
    }

    companion object {
//        val LAYOUT = R.layout.item_order_history
    }
}