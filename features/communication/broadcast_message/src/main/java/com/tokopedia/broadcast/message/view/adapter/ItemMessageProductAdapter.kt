package com.tokopedia.broadcast.message.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.broadcast.message.R
import kotlinx.android.synthetic.main.item_message_product.view.*

class ItemMessageProductAdapter(private val imgUrls: List<String>?): RecyclerView.Adapter<ItemMessageProductAdapter.ItemMessageProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMessageProductViewHolder {
        return ItemMessageProductViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_product, parent, false))
    }

    override fun getItemCount() = imgUrls?.size ?: 0

    override fun onBindViewHolder(holder: ItemMessageProductViewHolder, position: Int) {
        holder.bind(imgUrls?.getOrNull(position))
    }

    inner class ItemMessageProductViewHolder(val view: View): RecyclerView.ViewHolder(view){

        fun bind(imgUrl: String?){
            imgUrl?.let { ImageHandler.LoadImage(itemView.image, it) }
        }
    }
}