package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.view.customview.ImageHotspotView
import com.tokopedia.shop.common.view.model.ImageHotspotData
import com.tokopedia.shop.databinding.ShopHomeDisplayBannerProductHotspotItemBinding
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerProductHotspotUiModel


class ShopHomeDisplayBannerProductHotspotItemViewHolder(
    viewBinding: ShopHomeDisplayBannerProductHotspotItemBinding,
    private val widgetUiModel: ShopWidgetDisplayBannerProductHotspotUiModel?,
    private val listener: ShopHomeDisplayBannerProductHotspotViewHolder.Listener?
) : RecyclerView.ViewHolder(viewBinding.root), ImageHotspotView.Listener {

    private val imageBannerHotspot: ImageHotspotView = viewBinding.imageBannerHotspot

    fun bind(uiModel: ShopWidgetDisplayBannerProductHotspotUiModel.Data) {
        setupImageBannerHotspotData(uiModel)
    }

    private fun setupImageBannerHotspotData(uiModel: ShopWidgetDisplayBannerProductHotspotUiModel.Data) {
        imageBannerHotspot.setData(
            ImageHotspotData(
                imageBannerUrl = uiModel.imageUrl,
                listHotspot = uiModel.listProductHotspot.map { productHotspot ->
                    ImageHotspotData.HotspotData(
                        x = productHotspot.hotspotCoordinate.x,
                        y = productHotspot.hotspotCoordinate.y,
                        productImage = productHotspot.imageUrl,
                        productName = productHotspot.name,
                        productPrice = productHotspot.displayedPrice,
                    )
                },
            ), listenerBubbleView = this
        )
    }

    override fun onBubbleViewClicked(
        hotspotData: ImageHotspotData.HotspotData,
        view: View,
        index: Int
    ) {
        widgetUiModel?.let {
            listener?.onHotspotBubbleClicked(it, bindingAdapterPosition, index)
        }
    }

}
