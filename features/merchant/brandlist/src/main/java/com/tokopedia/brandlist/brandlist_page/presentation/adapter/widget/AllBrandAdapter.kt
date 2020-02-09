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

class AllBrandAdapter(private val context: Context) : RecyclerView.Adapter<AllBrandAdapter.AllBrandViewHolder>() {

    private var allBrands: MutableList<Shop> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllBrandViewHolder {
        return AllBrandViewHolder(LayoutInflater.from(context).inflate(R.layout.brandlist_all_brand_item, parent, false))
    }

    override fun getItemCount(): Int {
        return allBrands.size
    }

    override fun onBindViewHolder(holder: AllBrandViewHolder, position: Int) {
        val brand = allBrands[position]
        holder.brandLogoView?.let {
            loadImageToImageView(brand.logoUrl, it)
        }
        holder.brandImageView?.let {
            loadImageToImageView(brand.imageUrl, it)
        }
        holder.brandNameView?.let {
            it.text = brand.name
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

    fun setAllBrands(allBrandList: List<Shop>) {
        allBrands.addAll(allBrandList)
        notifyDataSetChanged()
    }

    class AllBrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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