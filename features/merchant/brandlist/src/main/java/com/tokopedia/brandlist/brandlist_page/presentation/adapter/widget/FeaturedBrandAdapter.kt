package com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.applink.RouteManager
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.analytic.BrandlistTracking
import com.tokopedia.brandlist.brandlist_page.data.model.Shop
import com.tokopedia.brandlist.common.listener.BrandlistPageTracking

class FeaturedBrandAdapter(
        private val context: Context,
        private val listener: BrandlistPageTracking) : RecyclerView.Adapter<FeaturedBrandAdapter.FeaturedBrandViewHolder>() {

    private var featuredBrands: List<Shop> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedBrandViewHolder {
        return FeaturedBrandViewHolder(LayoutInflater.from(context).inflate(R.layout.brandlist_featured_brand_item, parent, false))
    }

    override fun getItemCount(): Int {
        return featuredBrands.size
    }

    override fun onBindViewHolder(holder: FeaturedBrandViewHolder, position: Int) {
        holder.bindData(featuredBrands[position], position)
    }

    fun setFeaturedBrands(featuredBrandList: List<Shop>) {
        featuredBrands = featuredBrandList
        notifyDataSetChanged()
    }

    inner class FeaturedBrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context: Context
        var brandImg: ImageView

        init {
            context = itemView.context
            brandImg = itemView.findViewById(R.id.iv_featured_brand)
        }

        fun bindData(shop: Shop, position: Int) {

            Glide.with(context)
                    .load(shop.imageUrl)
                    .dontAnimate()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(brandImg)
            brandImg.setOnClickListener{
                listener.clickBrandPilihan((shop.id).toString(),
                        shop.name, shop.imageUrl, (position + 1).toString())
                RouteManager.route(context, shop.url)
            }
        }
    }
}