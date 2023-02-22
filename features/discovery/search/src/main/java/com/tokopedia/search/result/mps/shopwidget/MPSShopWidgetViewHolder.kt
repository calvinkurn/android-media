package com.tokopedia.search.result.mps.shopwidget

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.databinding.SearchMpsShopWidgetBinding
import com.tokopedia.utils.view.binding.viewBinding

class MPSShopWidgetViewHolder(
    itemView: View
): AbstractViewHolder<MPSShopWidgetDataView>(itemView) {

    private var binding: SearchMpsShopWidgetBinding? by viewBinding()

    override fun bind(dataView: MPSShopWidgetDataView) {
        bindShopLogo(dataView)
        bindShopBadge(dataView)
        bindShopName(dataView)
        bindShopLocation(dataView)
        bindShopButton(dataView)
        bindProductCarousel(dataView)
    }

    private fun bindShopLogo(dataView: MPSShopWidgetDataView) {
        binding?.searchMPSShopLogo?.loadImageCircle(dataView.shopImage)
    }

    private fun bindShopBadge(dataView: MPSShopWidgetDataView) {
        val shopBadgeImageView = binding?.searchMPSShopBadge

        shopBadgeImageView?.shouldShowWithAction(dataView.shopBadge.willShow()) {
            shopBadgeImageView.loadIcon(dataView.shopBadge.imageUrl)
        }
    }

    private fun bindShopName(dataView: MPSShopWidgetDataView) {
        binding?.searchMPSShopName?.text = dataView.shopName
    }

    private fun bindShopLocation(dataView: MPSShopWidgetDataView) {
        binding?.searchMPSShopLocation?.shouldShowWithAction(dataView.shopLocation.isNotEmpty()) {
            binding?.searchMPSShopLocation?.text = dataView.shopLocation
        }

        binding?.searchMPSShopLocationLine?.showWithCondition(
            dataView.willShowLocationFreeOngkirSeparator()
        )

        binding?.searchMPSBebasOngkirLogo?.shouldShowWithAction(dataView.willShowFreeOngkir()) {
            binding?.searchMPSBebasOngkirLogo?.loadIcon(dataView.shopFreeOngkir.imageUrl)
        }
    }

    private fun bindShopButton(dataView: MPSShopWidgetDataView) {
        binding?.searchMPSSeeShopButton?.shouldShowWithAction(dataView.shopButtonTitle.isNotEmpty()) {
            binding?.searchMPSSeeShopButton?.text = dataView.shopButtonTitle
        }
    }

    private fun bindProductCarousel(dataView: MPSShopWidgetDataView) {
        binding?.searchMPSProductCarousel?.bindCarouselProductCardViewGrid(
            productCardModelList = dataView.productList.map(::productCardModel)
        )
    }

    private fun productCardModel(shopProductItem: MPSShopWidgetProductDataView): ProductCardModel =
        ProductCardModel(
            productImageUrl = shopProductItem.productImageUrl,
            productName = shopProductItem.productName,
            slashedPrice = shopProductItem.originalPrice,
            discountPercentage = shopProductItem.discountPercentage,
            formattedPrice = shopProductItem.priceFormat,
            ratingString = shopProductItem.ratingAverage,
            labelGroupList = shopProductItem.labelGroupList.map(::toLabelGroup),
        )

    private fun toLabelGroup(labelGroupDataView: MPSProductLabelGroupDataView) =
        ProductCardModel.LabelGroup(
            position = labelGroupDataView.position,
            title = labelGroupDataView.title,
            type = labelGroupDataView.type,
            imageUrl = labelGroupDataView.url,
        )

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.search.R.layout.search_mps_shop_widget
    }
}
