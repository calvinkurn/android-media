package com.tokopedia.product.detail.view.viewholder

import android.view.View
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
class ItemCarouselImageViewHolder(view: View,
                                  val listener: DynamicProductDetailListener,
                                  val componentTrackDataModel: ComponentTrackDataModel?) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_category_image_view_holder
    }

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