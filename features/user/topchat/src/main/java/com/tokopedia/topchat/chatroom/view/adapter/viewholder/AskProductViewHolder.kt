package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.AskedProduct

class AskProductViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val productName = itemView?.findViewById<TextView>(R.id.product_name)

    fun bind(askedProduct: AskedProduct) {
        productName?.text = askedProduct.name
    }

}