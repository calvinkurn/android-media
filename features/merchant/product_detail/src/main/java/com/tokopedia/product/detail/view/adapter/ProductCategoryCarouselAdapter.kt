package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.common.data.model.pdplayout.CategoryCarousel
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ItemCarouselImageViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductVideoDetailViewHolder

/**
 * Created by Yehezkiel on 13/07/21
 */
class ProductCategoryCarouselAdapter(private val listener: DynamicProductDetailListener,) : RecyclerView.Adapter<ItemCarouselImageViewHolder>() {

    private var categoryData: List<CategoryCarousel> = listOf()
    private var componentTrackDataModel: ComponentTrackDataModel? = null

    fun setData(data: List<CategoryCarousel>, componentTrackDataModel: ComponentTrackDataModel?) {
        categoryData = data
        this.componentTrackDataModel = componentTrackDataModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCarouselImageViewHolder {
        return ItemCarouselImageViewHolder(LayoutInflater.from(parent.context)
                .inflate(ProductVideoDetailViewHolder.LAYOUT, parent, false), listener, componentTrackDataModel)
    }

    override fun onBindViewHolder(holder: ItemCarouselImageViewHolder, position: Int) {
        holder.bind(categoryData[position])
    }

    override fun getItemCount(): Int = categoryData.size

}