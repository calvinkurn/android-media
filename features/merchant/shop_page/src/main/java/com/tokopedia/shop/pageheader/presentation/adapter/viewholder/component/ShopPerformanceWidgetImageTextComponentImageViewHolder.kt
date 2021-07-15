package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageTextComponentUiModel

class ShopPerformanceWidgetImageTextComponentImageViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object{
        val LAYOUT_RES = R.layout.layout_shop_performance_widget_image_text_image_component
    }

    private val imageView: ImageView = itemView.findViewById(R.id.image_view)

    fun bind(data: ShopHeaderImageTextComponentUiModel.Images.Data?) {
        imageView.loadImage(data?.image)
    }

}