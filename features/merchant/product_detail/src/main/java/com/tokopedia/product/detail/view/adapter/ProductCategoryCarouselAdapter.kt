package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CategoryCarousel
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 13/07/21
 */
class ProductCategoryCarouselAdapter(private val listener: DynamicProductDetailListener)
    : RecyclerView.Adapter<ProductCategoryCarouselAdapter.ItemCarouselImageViewHolder>() {

    private var categoryData: List<CategoryCarousel> = listOf()
    private var componentTrackDataModel: ComponentTrackDataModel? = null

    fun setData(data: List<CategoryCarousel>, componentTrackDataModel: ComponentTrackDataModel?) {
        categoryData = data
        this.componentTrackDataModel = componentTrackDataModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCarouselImageViewHolder {
        return ItemCarouselImageViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category_image_view_holder, parent, false), listener, componentTrackDataModel)
    }

    override fun onBindViewHolder(holder: ItemCarouselImageViewHolder, position: Int) {
        holder.bind(categoryData[position])
    }

    override fun getItemCount(): Int = categoryData.size

    inner class ItemCarouselImageViewHolder(view: View,
                                            val listener: DynamicProductDetailListener,
                                            val componentTrackDataModel: ComponentTrackDataModel?) : RecyclerView.ViewHolder(view) {

        private val categoryImg: ImageUnify? = itemView.findViewById(R.id.product_category_widget_img)
        private val categoryTitle: Typography? = itemView.findViewById(R.id.product_category_widget_title)

        fun bind(data: CategoryCarousel) = with(itemView) {
            categoryImg?.loadImage(data.icon)
            categoryTitle?.text = data.title

            itemView.setOnClickListener {
                if (data.applink.isNotEmpty()) {
                    listener.onCategoryCarouselImageClicked(data.applink, data.title, componentTrackDataModel)
                }
            }
        }
    }
}