package com.tokopedia.search.result.product.semlessproduct.seamlessproduct

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSeamlessProductCardBinding
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.utils.view.binding.viewBinding

class InspirationProductItemViewHolder(
    itemView: View,
    private val inspirationProductListener: InspirationProductListener,
    private val productListener: ProductListener
) : AbstractViewHolder<InspirationProductItemDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_seamless_product_card
        val LAYOUT_WITH_VIEW_STUB = R.layout.search_inspiration_seamless_product_card_with_viewstub

        @LayoutRes
        fun layout(isUsingViewStub: Boolean): Int {
            if (isUsingViewStub) return LAYOUT_WITH_VIEW_STUB
            return LAYOUT
        }
    }

    private var binding: SearchInspirationSeamlessProductCardBinding? by viewBinding()

    override fun bind(productItemData: InspirationProductItemDataView?) {
        if (productItemData == null) return
        val productCardView = binding?.productCardView ?: return
        productCardView.addOnImpressionListener(productItemData) {
            inspirationProductListener.onInspirationProductItemImpressed(productItemData)
        }
        val productCardModel =
            productItemData.toProductCardModel()

        registerLifecycleObserver(productCardModel)

        productCardView.setProductModel(productCardModel)

        productCardView.setOnClickListener {
            inspirationProductListener.onInspirationProductItemClicked(productItemData)
        }

        productCardView.setThreeDotsOnClickListener {
            inspirationProductListener.onInspirationProductItemThreeDotsClicked(productItemData)
        }
    }

    fun InspirationProductItemDataView.toProductCardModel(): ProductCardModel =
        ProductCardModel(
            productName = this.name,
            formattedPrice = this.priceString,
            productImageUrl = this.imageUrl,
            countSoldRating = this.ratingAverage,
            labelGroupList = this.labelGroupDataList.toProductCardLabelGroup(),
            shopLocation = if (this.shopLocation.isNotEmpty()) this.shopLocation else this.shopName,
            shopBadgeList = this.badgeItemDataViewList.toProductCardModelShopBadges(),
            freeOngkir = this.freeOngkirDataView.toProductCardModelFreeOngkir(),
            isTopAds = this.isOrganicAds,
            hasThreeDots = this.seamlessInspirationProductType.hasThreeDots,
            cardInteraction = true,
            discountPercentage = if (this.discountPercentage > 0) "${this.discountPercentage}%"
            else "",
            slashedPrice = if (this.discountPercentage > 0) this.originalPrice else "",
            stockBarPercentage = this.stockBarDataView.percentageValue,
            stockBarLabel = this.stockBarDataView.value,
            stockBarLabelColor = this.stockBarDataView.color,
            isWideContent = false
        )

    private fun List<BadgeItemDataView>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(it.isShown, it.imageUrl)
        } ?: listOf()
    }

    private fun List<LabelGroupDataView>?.toProductCardLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(
                title = it.title,
                position = it.position,
                type = it.type,
                imageUrl = it.imageUrl
            )
        } ?: listOf()
    }

    private fun FreeOngkirDataView.toProductCardModelFreeOngkir(): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }

    private fun registerLifecycleObserver(productCardModel: ProductCardModel) {
        val productCardView = binding?.productCardView ?: return

        productListener.productCardLifecycleObserver?.register(productCardView, productCardModel)
    }

    override fun onViewRecycled() {
        val productCardView = binding?.productCardView ?: return

        productCardView.recycle()
        productListener.productCardLifecycleObserver?.unregister(productCardView)
    }
}
