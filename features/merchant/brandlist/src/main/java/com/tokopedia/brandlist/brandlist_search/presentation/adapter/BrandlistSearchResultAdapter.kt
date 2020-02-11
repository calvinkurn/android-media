package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.data.model.Brand
import com.tokopedia.brandlist.common.ImageAssets
import com.tokopedia.kotlin.extensions.view.inflateLayout

class BrandlistSearchResultAdapter(): RecyclerView.Adapter<BrandlistSearchResultAdapter.BrandlistSearchResultViewHolder>() {

    private var searchResult: MutableList<Brand>  = mutableListOf()

    fun updateSearchResultData(searchResultList: List<Brand>){
        searchResult = searchResultList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandlistSearchResultViewHolder {
        return BrandlistSearchResultViewHolder(parent.inflateLayout(R.layout.item_search_result))
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    override fun onBindViewHolder(holder: BrandlistSearchResultViewHolder, position: Int) {
        holder.bindData(searchResult[position], position)
    }

    inner class BrandlistSearchResultViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val context: Context
        val imgBrand: ImageView
        val imgNotFound: ImageView
        val txtBrandName: TextView
        val sectionSearchNotFound: LinearLayout

        init {
            context = itemView.context
            imgBrand = itemView.findViewById(R.id.img_brand)
            txtBrandName = itemView.findViewById(R.id.txt_title)
            sectionSearchNotFound = itemView.findViewById(R.id.view_brand_search_not_found)
            imgNotFound = itemView.findViewById(R.id.img_brand_not_found)
        }

        fun bindData(brand: Brand, position: Int) {
            txtBrandName.setText(brand.name)
            ImageHandler.loadImage(context, imgBrand, brand.logoUrl, null)

            if (searchResult.size < 1) {
                sectionSearchNotFound.visibility = View.VISIBLE
                ImageHandler.loadImage(context, imgNotFound, ImageAssets.BRAND_NOT_FOUND, null)
            }
        }

    }

}
