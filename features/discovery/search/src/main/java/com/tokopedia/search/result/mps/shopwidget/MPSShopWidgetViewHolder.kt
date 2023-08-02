package com.tokopedia.search.result.mps.shopwidget

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselViewAllCardData
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.databinding.SearchMpsShopWidgetBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class MPSShopWidgetViewHolder(
    itemView: View,
    private val recycledViewPool: RecycledViewPool,
    private val listener: MPSShopWidgetListener,
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

        shopBadgeImageView?.addOnImpressionListener(dataView) {
            listener.onShopImpressed(dataView)
        }
    }

    private fun bindShopName(dataView: MPSShopWidgetDataView) {
        binding?.searchMPSShopName?.text = dataView.name
    }

    private fun bindShopLocation(dataView: MPSShopWidgetDataView) {
        val locationLabel = dataView.subtitle.ifEmpty { dataView.city }
        binding?.searchMPSShopLocation?.shouldShowWithAction(locationLabel.isNotEmpty()) {
            binding?.searchMPSShopLocation?.apply {
                // adjust margin to cater TokoNow layout requirement
                val topMargin = if (dataView.subtitle.isNotEmpty()) 2.toPx() else 4.toPx()
                setMargin(marginLeft, topMargin, marginRight, marginBottom)
                text = locationLabel
            }
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
            searchMPSSeeShopButton.setOnClickListener {
                listener.onSeeShopClicked(dataView)
            }
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
        val productList = dataView.productList

        binding?.searchMPSProductCarousel?.bindCarouselProductCardViewGrid(
            recyclerViewPool = recycledViewPool,
            productCardModelList = productList.map(::productCardModel),
            carouselProductCardOnItemImpressedListener =
                onItemImpressedListener(dataView, productList),
            carouselViewAllCardData =
                dataView.viewAllCard.toCarouselViewAllCard(),
            carouselProductCardOnItemClickListener =
                onItemClickListener(dataView, productList),
            carouselProductCardOnItemAddToCartListener =
                onItemAddToCartListener(dataView, productList),
            carouselProductCardOnItemSeeOtherProductClickListener =
                onSeeOtherProductClickListener(dataView, productList),
            carouselViewAllCardClickListener =
                onViewAllCardClickListener(dataView),
        )
    }

    private fun productCardModel(product: MPSShopWidgetProductDataView): ProductCardModel {
        val primaryButton = product.primaryButton()
        val secondaryButton = product.secondaryButton()

        return ProductCardModel(
            productImageUrl = product.imageUrl,
            productName = product.name,
            discountPercentage =
                if (product.discountPercentage > 0) "${product.discountPercentage}%" else "",
            slashedPrice =
                if (product.discountPercentage > 0) product.originalPrice else "",
            formattedPrice = product.priceFormat,
            countSoldRating = product.ratingAverage.takeUnless { it == "0" } ?: "",
            labelGroupList = product.labelGroupList.map(::toLabelGroup),
            hasAddToCartButton = primaryButton != null,
            addToCardText = primaryButton?.text ?: "",
            seeOtherProductText = secondaryButton?.text ?: "",
        )
    }

    private fun MPSShopWidgetViewAllCardDataView.toCarouselViewAllCard(): CarouselViewAllCardData? {
        return if (text.isNotBlank()) CarouselViewAllCardData(title = text) else null
    }

    private fun toLabelGroup(labelGroupDataView: MPSProductLabelGroupDataView) =
        ProductCardModel.LabelGroup(
            position = labelGroupDataView.position,
            title = labelGroupDataView.title,
            type = labelGroupDataView.type,
            imageUrl = labelGroupDataView.url,
        )

    private fun onItemImpressedListener(
        dataView: MPSShopWidgetDataView,
        productList: List<MPSShopWidgetProductDataView>,
    ) = object : CarouselProductCardListener.OnItemImpressedListener {
        override fun onItemImpressed(
            productCardModel: ProductCardModel,
            carouselProductCardPosition: Int,
        ) {
            listener.onProductItemImpressed(
                dataView,
                productList.getOrNull(carouselProductCardPosition),
            )
        }

        override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? =
            productList.getOrNull(carouselProductCardPosition)?.impressHolder
    }

    private fun onItemClickListener(
        dataView: MPSShopWidgetDataView,
        productList: List<MPSShopWidgetProductDataView>,
    ) = object : CarouselProductCardListener.OnItemClickListener {
        override fun onItemClick(
            productCardModel: ProductCardModel,
            carouselProductCardPosition: Int,
        ) {
            listener.onProductItemClicked(
                dataView,
                productList.getOrNull(carouselProductCardPosition)
            )
        }

    }

    private fun onItemAddToCartListener(
        dataView: MPSShopWidgetDataView,
        productList: List<MPSShopWidgetProductDataView>,
    ) = object : CarouselProductCardListener.OnItemAddToCartListener {
        override fun onItemAddToCart(
            productCardModel: ProductCardModel,
            carouselProductCardPosition: Int,
        ) {
            listener.onProductItemAddToCart(
                dataView,
                productList.getOrNull(carouselProductCardPosition)
            )
        }
    }

    private fun onSeeOtherProductClickListener(
        dataView: MPSShopWidgetDataView,
        productList: List<MPSShopWidgetProductDataView>,
    ) = object : CarouselProductCardListener.OnSeeOtherProductClickListener {
        override fun onSeeOtherProductClick(
            productCardModel: ProductCardModel,
            carouselProductCardPosition: Int,
        ) {
            listener.onProductItemSeeOtherProductClick(
                dataView,
                productList.getOrNull(carouselProductCardPosition)
            )
        }
    }

    private fun onViewAllCardClickListener(dataView: MPSShopWidgetDataView) =
        object : CarouselProductCardListener.OnViewAllCardClickListener {
            override fun onViewAllCardClick() {
                listener.onSeeAllCardClicked(dataView)
            }
        }

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.search.R.layout.search_mps_shop_widget
    }
}
