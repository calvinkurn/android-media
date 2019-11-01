package com.tokopedia.officialstore.official.presentation.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.Shop

class FeaturedShopAdapter(private val context: Context, var shopList: List<Shop> = ArrayList()) :
        RecyclerView.Adapter<FeaturedShopAdapter.FeaturedShopViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FeaturedShopViewHolder {
        return FeaturedShopViewHolder(LayoutInflater.from(context).
                inflate(R.layout.widget_official_featured_shop, p0, false))
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(p0: FeaturedShopViewHolder, p1: Int) {
        val shop = shopList[p1]
        ImageHandler.loadImage(
                context,
                p0.imageView,
                shop.imageUrl,
                R.drawable.ic_loading_image
        )

        p0.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(context, p1, shop)
        }
    }

    class FeaturedShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView? = null

        init {
            imageView = itemView.findViewById(R.id.image_featured_shop)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(context: Context, p0: Int, shop: Shop)
    }
}