package com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget

import android.annotation.SuppressLint
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
import com.tokopedia.brandlist.brandlist_page.data.model.Shop
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener

class FeaturedBrandAdapter(
        private val context: Context,
        private val listener: BrandlistPageTrackingListener) : RecyclerView.Adapter<FeaturedBrandAdapter.FeaturedBrandViewHolder>() {

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

    @SuppressLint("NewApi")
    inner class FeaturedBrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val context: Context = itemView.context
        var brandImageView: ImageView? = null

        init {
            brandImageView = itemView.findViewById(R.id.iv_featured_brand)

            itemView.setOnClickListener {

                val view = it
                val shopObj = it.getTag(R.id.brand)

                shopObj?.let {

                    val shop: Shop = view.getTag(R.id.brand) as Shop
                    val position: Int = view.getTag(R.id.position) as Int

                    listener.clickBrandPilihan((shop.id).toString(), shop.name, shop.imageUrl, (position + 1).toString())

                    RouteManager.route(context, shop.url)
                }
            }
        }

        fun bindData(shop: Shop?, position: Int) {

            itemView.setTag(R.id.brand, shop)
            itemView.setTag(R.id.position, position)

            shop?.let {
                brandImageView?.let {
                    loadImageToImageView(shop.imageUrl, it)
                }
            }
        }

        private fun loadImageToImageView(imageUrl: String, brandImageView: ImageView) {
            Glide.with(context)
                    .load(imageUrl)
                    .dontAnimate()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(brandImageView)
        }
    }
}