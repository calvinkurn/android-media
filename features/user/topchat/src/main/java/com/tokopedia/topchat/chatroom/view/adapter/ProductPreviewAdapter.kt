package com.tokopedia.topchat.chatroom.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ProductPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.viewmodel.ProductPreview

class ProductPreviewAdapter : RecyclerView.Adapter<ProductPreviewViewHolder>() {

    val products = arrayListOf<ProductPreview>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductPreviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_preview, parent, false)
        return ProductPreviewViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductPreviewViewHolder, position: Int) {
        holder.bind(products[position])
    }

    fun updateProduct(productPreview: ProductPreview) {
        products.clear()
        products.add(productPreview)
        notifyDataSetChanged()
    }
}