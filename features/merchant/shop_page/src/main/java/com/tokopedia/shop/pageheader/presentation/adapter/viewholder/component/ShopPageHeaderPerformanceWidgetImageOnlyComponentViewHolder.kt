package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.LayoutShopPerformanceWidgetImageOnlyComponentBinding
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderImageOnlyComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder(
    itemView: View,
    private val shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel,
    private val listener: Listener
) : AbstractViewHolder<ShopPageHeaderImageOnlyComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_performance_widget_image_only_component
        private const val MAX_IMAGE_WIDTH = 100
    }

    interface Listener {
        fun onImpressionShopPerformanceWidgetImageOnlyItem(
            componentModel: ShopPageHeaderImageOnlyComponentUiModel,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
        )
    }
    private val viewBinding: LayoutShopPerformanceWidgetImageOnlyComponentBinding? by viewBinding()
    private val imageView: ImageView? = viewBinding?.ivImage

    override fun bind(model: ShopPageHeaderImageOnlyComponentUiModel) {
        val imageUrl = model.image
        imageView?.loadImage(imageUrl) {
            overrideSize(Resize(MAX_IMAGE_WIDTH.toPx(), imageView.layoutParams.height))
        }
        itemView.addOnImpressionListener(model) {
            listener.onImpressionShopPerformanceWidgetImageOnlyItem(
                model,
                shopPageHeaderWidgetUiModel
            )
        }
    }
}
