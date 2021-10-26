package com.tokopedia.shop.home.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeFlashSaleTncViewHolder

class ShopHomeFlashSaleTncAdapter: RecyclerView.Adapter<ShopHomeFlashSaleTncViewHolder>() {

    private var tncDescriptions : List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeFlashSaleTncViewHolder {
        return ShopHomeFlashSaleTncViewHolder(parent.inflateLayout(R.layout.item_shop_flash_sale_tnc))
    }

    override fun onBindViewHolder(holder: ShopHomeFlashSaleTncViewHolder, position: Int) {
        holder.bind(position, tncDescriptions[position])
    }

    override fun getItemCount(): Int {
        return tncDescriptions.size
    }

    fun setTncDescriptions(tncDescriptions: List<String>) {
        this.tncDescriptions = tncDescriptions
        notifyDataSetChanged()
    }
}