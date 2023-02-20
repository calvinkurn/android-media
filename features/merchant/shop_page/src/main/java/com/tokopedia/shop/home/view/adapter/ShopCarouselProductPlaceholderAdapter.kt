package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R

class ShopCarouselProductPlaceholderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TOTAL_PLACEHOLDER_ITEM = 5
    }

    class PlaceholderItem(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlaceholderItem(
            LayoutInflater.from(parent.context).inflate(
                R.layout.shop_home_product_carousel_placeholder_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    override fun getItemCount(): Int {
        return TOTAL_PLACEHOLDER_ITEM
    }
}
