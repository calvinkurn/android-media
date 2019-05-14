package com.tokopedia.topchat.chatroom.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.AskProductViewHolder
import com.tokopedia.topchat.chatroom.view.viewmodel.AskedProduct

class AskProductAdapter : RecyclerView.Adapter<AskProductViewHolder>() {

    val products = arrayListOf<AskedProduct>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AskProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ask_product, parent, false)
        return AskProductViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: AskProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    fun updateProduct(askedProduct: AskedProduct) {
        products.clear()
        products.add(askedProduct)
        notifyDataSetChanged()
    }
}