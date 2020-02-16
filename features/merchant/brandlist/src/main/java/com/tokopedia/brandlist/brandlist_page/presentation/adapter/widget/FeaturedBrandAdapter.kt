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

    companion object {
        const val TAG_SHOP = 0
        const val TAG_POSITION = 1
    }

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
                val shopObj = it.getTag(TAG_SHOP)

                shopObj?.let {

                    val shop: Shop = view.getTag(TAG_SHOP) as Shop
                    val position: Int = view.getTag(TAG_POSITION) as Int

                    listener.clickBrandPilihan((shop.id).toString(), shop.name, shop.imageUrl, (position + 1).toString())

                    RouteManager.route(context, shop.url)
                }
            }
        }

        fun bindData(shop: Shop?, position: Int) {

            itemView.setTag(NewBrandAdapter.TAG_SHOP, shop)
            itemView.setTag(NewBrandAdapter.TAG_POSITION, position)

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