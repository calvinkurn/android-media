package com.tokopedia.deals.search.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.search.listener.DealsSearchListener
import com.tokopedia.kotlin.extensions.view.loadImage

class BrandViewHolder(itemView: View, private val searchListener: DealsSearchListener): RecyclerView.ViewHolder(itemView) {
    private val imageBrand: ImageView = itemView.findViewById(R.id.iv_brand)
    private val brandName: TextView = itemView.findViewById(R.id.brandName)

    fun bindData(brand: Brand, position: Int) {
        brandName.text = brand.title
        imageBrand.loadImage(brand.featuredThumbnailImage)
        searchListener.onBrandClicked(itemView, brand, position)
    }
}