package com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.applink.RouteManager
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.data.model.Shop
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener

class PopularBrandAdapter(
        private val context: Context,
        private val listener: BrandlistPageTrackingListener) : RecyclerView.Adapter<PopularBrandAdapter.PopularBrandViewHolder>() {

    private var popularBrands: List<Shop> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularBrandViewHolder {
        return PopularBrandViewHolder(LayoutInflater.from(context).inflate(R.layout.brandlist_popular_brand_item, parent, false))
    }

    override fun getItemCount(): Int {
        return popularBrands.size
    }

    override fun onBindViewHolder(holder: PopularBrandViewHolder, position: Int) {
        holder.bindData(popularBrands[position], position)
    }

    fun setPopularBrands(popularBrandList: List<Shop>) {
        popularBrands = popularBrandList
        notifyDataSetChanged()
    }

    inner class PopularBrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var context: Context
        var brandLogoView: ImageView? = null
        var brandImageView: ImageView? = null
        var brandNameView: TextView? = null

        init {
            context = itemView.context
            brandLogoView = itemView.findViewById(R.id.iv_brand_logo)
            brandImageView = itemView.findViewById(R.id.iv_brand_image)
            brandNameView = itemView.findViewById(R.id.tv_brand_name)
        }

        fun bindData(shop: Shop, position: Int) {
            listener.impressionBrandPopular(
                    (shop.id).toString(),
                    (position + 1).toString(),
                    shop.name,
                    shop.imageUrl)
            itemView.setOnClickListener {
                listener.clickBrandPopular(
                        (shop.id).toString(),
                        (position + 1).toString(),
                        shop.name,
                        shop.imageUrl)
                RouteManager.route(context, shop.url)
            }
            brandLogoView?.let {
                loadImageToImageView(shop.logoUrl, it)
            }
            brandImageView?.let {
                loadImageToImageView(shop.exclusiveLogoUrl, it)
            }
            brandNameView?.let {
                it.text = shop.name
            }
        }

        private fun loadImageToImageView(imageUrl: String, brandView: ImageView) {
            Glide.with(context)
                    .load(imageUrl)
                    .dontAnimate()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(brandView)
        }
    }
}