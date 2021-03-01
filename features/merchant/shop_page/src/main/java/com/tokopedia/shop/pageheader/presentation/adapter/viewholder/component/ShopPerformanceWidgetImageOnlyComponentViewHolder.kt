package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageOnlyComponentUiModel

class ShopPerformanceWidgetImageOnlyComponentViewHolder(
        itemView: View
) : AbstractViewHolder<ShopHeaderImageOnlyComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_performance_widget_image_only_component
    }

    private val imageView: ImageView? = itemView.findViewById(R.id.iv_image)

    override fun bind(model: ShopHeaderImageOnlyComponentUiModel) {
        val imageUrl = model.image
        imageView?.loadImage(imageUrl)
    }

}