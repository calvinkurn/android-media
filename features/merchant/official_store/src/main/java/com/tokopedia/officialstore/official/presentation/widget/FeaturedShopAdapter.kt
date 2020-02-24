package com.tokopedia.officialstore.official.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.FeaturedShopViewHolder

class FeaturedShopAdapter(private val context: Context, var shopList: List<Shop> = ArrayList()) :
        RecyclerView.Adapter<FeaturedShopViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): FeaturedShopViewHolder {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.widget_official_featured_shop, parent, false)

        return FeaturedShopViewHolder(itemView, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(holder: FeaturedShopViewHolder, position: Int) {
        val shop = shopList[position]
        holder.bind(shop)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, shop: Shop)
    }
}