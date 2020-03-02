package com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget

import android.annotation.SuppressLint
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

class NewBrandAdapter(
        private val context: Context,
        private val listener: BrandlistPageTrackingListener) : RecyclerView.Adapter<NewBrandAdapter.NewBrandViewHolder>() {

    private var newBrands: List<Shop> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewBrandViewHolder {
        return NewBrandViewHolder(LayoutInflater.from(context).inflate(R.layout.brandlist_popular_brand_item, parent, false))
    }

    override fun getItemCount(): Int {
        return newBrands.size
    }

    override fun onBindViewHolder(holder: NewBrandViewHolder, position: Int) {
        holder.bindData(newBrands[position], position)
    }

    fun setNewBrands(newBrandList: List<Shop>) {
        newBrands = newBrandList
        notifyDataSetChanged()
    }

    @SuppressLint("NewApi")
    inner class NewBrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context = itemView.context
        var brandLogoView: ImageView? = null
        var brandImageView: ImageView? = null
        var brandNameView: TextView? = null

        init {
            brandLogoView = itemView.findViewById(R.id.iv_brand_logo)
            brandImageView = itemView.findViewById(R.id.iv_brand_image)
            brandNameView = itemView.findViewById(R.id.tv_brand_name)

            itemView.setOnClickListener {

                val view = it
                val shopObj = it.getTag(R.id.brand)

                shopObj?.let {

                    val shop: Shop = view.getTag(R.id.brand) as Shop
                    val position: Int = view.getTag(R.id.position) as Int

                    listener.clickBrandBaruTokopedia(
                            (shop.id).toString(),
                            shop.name, shop.imageUrl,
                            (position + 1).toString())

                    RouteManager.route(context, shop.url)
                }
            }
        }

        fun bindData(shop: Shop?, position: Int) {

            itemView.setTag(R.id.brand, shop)
            itemView.setTag(R.id.position, position)

            shop?.let {
                listener.impressionBrandBaru(
                        (shop.id).toString(),
                        (position + 1).toString(),
                        shop.name, shop.imageUrl)

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