package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSeamlessReimagineProductCardBinding
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductItemDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductListener
import com.tokopedia.utils.view.binding.viewBinding

class InspirationProductItemReimagineViewHolder(
    itemView: View,
    protected val inspirationProductListener: InspirationProductListener,
): AbstractViewHolder<InspirationProductItemDataView>(itemView) {

    private var binding: SearchInspirationSeamlessReimagineProductCardBinding? by viewBinding()

    override fun bind(element: InspirationProductItemDataView) {
        binding?.searchInspirationSeamlessProductCardReimagine?.run {
            setProductModel(productCardModel(element))

            setThreeDotsClickListener {
                inspirationProductListener.onInspirationProductItemThreeDotsClicked(element)
            }

            setOnLongClickListener {
                inspirationProductListener.onInspirationProductItemThreeDotsClicked(element)
                true
            }

            setOnClickListener {
                inspirationProductListener.onInspirationProductItemClicked(element)
            }

            addOnImpressionListener(element) {
                inspirationProductListener.onInspirationProductItemImpressed(element)
            }
        }
    }

    private fun productCardModel(element: InspirationProductItemDataView) =
        ProductCardModel(
            imageUrl = element.imageUrl,
            isAds = element.isShowAdsLabel,
            name = element.name,
            price = element.priceString,
            slashedPrice = element.originalPrice,
            discountPercentage = element.discountPercentage,
            labelGroupList = labelGroupList(element),
            rating = element.ratingAverage,
            shopBadge = shopBadge(element),
            freeShipping = freeShipping(element),
            hasThreeDots = true,
        )

    private fun labelGroupList(element: InspirationProductItemDataView) =
        element.labelGroupList.map(::labelGroup)

    private fun labelGroup(labelGroupDataView: LabelGroupDataView) =
        ProductCardModel.LabelGroup(
            position = labelGroupDataView.position,
            title = labelGroupDataView.title,
            type = labelGroupDataView.type,
            imageUrl = labelGroupDataView.imageUrl,
        )

    private fun shopBadge(element: InspirationProductItemDataView): ProductCardModel.ShopBadge {
        val shopBadge = element.badgeItemDataViewList.firstOrNull()
        return ProductCardModel.ShopBadge(
            imageUrl = shopBadge?.imageUrl ?: "",
            title = shopBadge?.title ?: ""
        )
    }

    private fun freeShipping(element: InspirationProductItemDataView) =
        ProductCardModel.FreeShipping(
            imageUrl = element.freeOngkirDataView.imageUrl,
        )

    override fun onViewRecycled() {
        binding?.searchInspirationSeamlessProductCardReimagine?.recycle()
    }

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_seamless_reimagine_product_card
    }
}
