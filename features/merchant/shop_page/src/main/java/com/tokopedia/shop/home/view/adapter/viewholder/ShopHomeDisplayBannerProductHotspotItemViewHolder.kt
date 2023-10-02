package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.common.view.customview.bannerhotspot.ImageHotspotView
import com.tokopedia.shop.common.view.model.ImageHotspotData
import com.tokopedia.shop.databinding.ShopHomeDisplayBannerProductHotspotItemBinding
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerProductHotspotUiModel


class ShopHomeDisplayBannerProductHotspotItemViewHolder(
    viewBinding: ShopHomeDisplayBannerProductHotspotItemBinding,
    private val widgetUiModel: ShopWidgetDisplayBannerProductHotspotUiModel?,
    private val listener: ShopHomeDisplayBannerProductHotspotViewHolder.Listener?,
    private val ratio: String?
) : RecyclerView.ViewHolder(viewBinding.root), ImageHotspotView.Listener {

    private val imageBannerHotspot: ImageHotspotView = viewBinding.imageBannerHotspot

    fun bind(uiModel: ShopWidgetDisplayBannerProductHotspotUiModel.Data) {
        setupImageBannerHotspotData(uiModel)
        addImpressionListener(uiModel)
    }

    private fun addImpressionListener(itemModel: ShopWidgetDisplayBannerProductHotspotUiModel.Data) {
        itemView.addOnImpressionListener(itemModel){
            widgetUiModel?.let {
                listener?.onImpressionBannerHotspotImage(
                    widgetUiModel,
                    itemModel,
                    bindingAdapterPosition
                )
            }
        }
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
            ),
            listenerBubbleView = this,
            ratio = ratio
        )
    }

    override fun onBubbleViewClicked(
        hotspotData: ImageHotspotData.HotspotData,
        view: View,
        index: Int
    ) {
        widgetUiModel?.let {
            it.data.firstOrNull()?.let { bannerItemUiModel ->
                listener?.onClickProductBannerHotspot(
                    it,
                    bannerItemUiModel,
                    Int.ZERO,
                    index
                )
            }
        }
    }

}
