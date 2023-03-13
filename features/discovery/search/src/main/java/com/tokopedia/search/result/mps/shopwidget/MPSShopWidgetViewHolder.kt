package com.tokopedia.search.result.mps.shopwidget

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.databinding.SearchMpsShopWidgetBinding
import com.tokopedia.utils.view.binding.viewBinding

class MPSShopWidgetViewHolder(
    itemView: View,
    private val recycledViewPool: RecycledViewPool,
): AbstractViewHolder<MPSShopWidgetDataView>(itemView) {

    private var binding: SearchMpsShopWidgetBinding? by viewBinding()

    override fun bind(dataView: MPSShopWidgetDataView) {
        bindShopLogo(dataView)
        bindShopBadge(dataView)
        bindShopName(dataView)
        bindShopLocation(dataView)
        bindShopButton(dataView)
        bindShopTicker(dataView)
        bindProductCarousel(dataView)
    }

    private fun bindShopLogo(dataView: MPSShopWidgetDataView) {
        binding?.searchMPSShopLogo?.loadImageCircle(dataView.imageUrl)
    }

    private fun bindShopBadge(dataView: MPSShopWidgetDataView) {
        val shopBadgeImageView = binding?.searchMPSShopBadge
        val badgeToShow = dataView.badgeList.find { it.willShow() }
        val shouldShowBadge = badgeToShow != null

        shopBadgeImageView?.shouldShowWithAction(shouldShowBadge) {
            shopBadgeImageView.loadIcon(badgeToShow?.imageUrl)
        }
    }

    private fun bindShopName(dataView: MPSShopWidgetDataView) {
        binding?.searchMPSShopName?.text = dataView.name
    }

    private fun bindShopLocation(dataView: MPSShopWidgetDataView) {
        binding?.searchMPSShopLocation?.shouldShowWithAction(dataView.location.isNotEmpty()) {
            binding?.searchMPSShopLocation?.text = dataView.location
        }

        binding?.searchMPSShopLocationLine?.showWithCondition(
            dataView.willShowLocationFreeOngkirSeparator()
        )

        binding?.searchMPSBebasOngkirLogo?.shouldShowWithAction(dataView.willShowFreeOngkir()) {
            binding?.searchMPSBebasOngkirLogo?.loadIcon(dataView.shopFreeOngkir.imageUrl)
        }
    }

    private fun bindShopButton(dataView: MPSShopWidgetDataView) {
        val shopButton = dataView.buttonList.firstOrNull()
        val searchMPSSeeShopButton = binding?.searchMPSSeeShopButton ?: return

        searchMPSSeeShopButton.shouldShowWithAction(shopButton != null) {
            searchMPSSeeShopButton.text = shopButton?.text
        }
    }

    private fun bindShopTicker(dataView: MPSShopWidgetDataView) {
        val ticker = dataView.ticker
        val searchMPSShopTicker = binding?.searchMPSShopTicker ?: return

        searchMPSShopTicker.shouldShowWithAction(ticker.willShow()) {
            searchMPSShopTicker.setHtmlDescription(ticker.message)
        }
    }

    private fun bindProductCarousel(dataView: MPSShopWidgetDataView) {
        binding?.searchMPSProductCarousel?.bindCarouselProductCardViewGrid(
            recyclerViewPool = recycledViewPool,
            productCardModelList = dataView.productList.map(::productCardModel)
        )
    }

    private fun productCardModel(shopProductItem: MPSShopWidgetProductDataView): ProductCardModel =
        ProductCardModel(
            productImageUrl = shopProductItem.imageUrl,
            productName = shopProductItem.name,
            slashedPrice = shopProductItem.originalPrice,
            discountPercentage = shopProductItem.discountPercentage,
            formattedPrice = shopProductItem.priceFormat,
            ratingString = shopProductItem.ratingAverage,
            labelGroupList = shopProductItem.labelGroupList.map(::toLabelGroup),
            hasAddToCartButton = true,
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
