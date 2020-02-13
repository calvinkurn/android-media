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
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.data.model.Shop

class PopularBrandAdapter(private val context: Context) :
        RecyclerView.Adapter<PopularBrandAdapter.PopularBrandViewHolder>() {

    private var popularBrands: List<Shop> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularBrandViewHolder {
        return PopularBrandViewHolder(LayoutInflater.from(context).inflate(R.layout.brandlist_popular_brand_item, parent, false))
    }

    override fun getItemCount(): Int {
        return popularBrands.size
    }

    override fun onBindViewHolder(holder: PopularBrandViewHolder, position: Int) {
        val popularBrand = popularBrands[position]
        holder.brandLogoView?.let {
            loadImageToImageView(popularBrand.logoUrl, it)
        }
        holder.brandImageView?.let {
            loadImageToImageView(popularBrand.imageUrl, it)
        }
        holder.brandNameView?.let {
            it.text = popularBrand.name
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

    fun setPopularBrands(popularBrandList: List<Shop>) {
        popularBrands = popularBrandList
        notifyDataSetChanged()
    }

    class PopularBrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var brandLogoView: ImageView? = null
        var brandImageView: ImageView? = null
        var brandNameView: TextView? = null

        init {
            brandLogoView = itemView.findViewById(R.id.iv_brand_logo)
            brandImageView = itemView.findViewById(R.id.iv_brand_image)
            brandNameView = itemView.findViewById(R.id.tv_brand_name)
        }
    }
}