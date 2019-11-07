package com.tokopedia.officialstore.official.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.Shop

class FeaturedShopAdapter(private val context: Context, var shopList: List<Shop> = ArrayList()) :
        RecyclerView.Adapter<FeaturedShopAdapter.FeaturedShopViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): FeaturedShopViewHolder {
        return FeaturedShopViewHolder(LayoutInflater.from(context).
                inflate(R.layout.widget_official_featured_shop, parent, false))
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(holder: FeaturedShopViewHolder, position: Int) {
        val shop = shopList[position]
        holder.imageView?.let {
            Glide.with(context)
                .load(shop.imageUrl)
                .dontAnimate()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(it)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position, shop)
        }
    }

    class FeaturedShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView? = null

        init {
            imageView = itemView.findViewById(R.id.image_featured_shop)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, shop: Shop)
    }
}