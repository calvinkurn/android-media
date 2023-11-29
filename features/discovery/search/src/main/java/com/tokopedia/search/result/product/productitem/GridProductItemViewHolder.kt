package com.tokopedia.search.result.product.productitem

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.reimagine.ProductCardGridView
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductCardReimagineGridBinding
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import com.tokopedia.search.result.presentation.view.listener.ProductSafeListener
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.video_widget.VideoPlayer
import com.tokopedia.video_widget.VideoPlayerProvider

class GridProductItemViewHolder(
    itemView: View,
    protected val productListener: ProductListener,
    override val isAutoplayEnabled: Boolean,
    protected val productSafeListener: ProductSafeListener,
): AbstractViewHolder<ProductItemDataView>(itemView),
    VideoPlayerProvider {
    
    private var binding: SearchResultProductCardReimagineGridBinding? by viewBinding()

    override val videoPlayer: VideoPlayer?
        get() = binding?.searchProductCardGridReimagine?.video

    override fun bind(productItemData: ProductItemDataView) {
        binding?.searchProductCardGridReimagine?.run {
            setProductModel(productCardModel(productItemData))

            setThreeDotsClickListener {
                productListener.onThreeDotsClick(productItemData, bindingAdapterPosition)
            }

            setOnLongClickListener {
                productListener.onThreeDotsClick(productItemData, bindingAdapterPosition)
                true
            }

            addOnImpressionListener(productItemData) {
                productListener.onProductImpressed(productItemData, bindingAdapterPosition)
            }

            setOnClickListener(productItemData)
        }
    }

    fun ProductCardGridView.setOnClickListener(productItemData: ProductItemDataView){
        if(productItemData.isImageBlurred) {
            setProductSafeOnClickListener {
                productSafeListener.onSafeProductClickInfo(
                    productItemData,
                    bindingAdapterPosition
                )
            }
        } else {
            setOnClickListener {
                productListener.onItemClicked(productItemData, bindingAdapterPosition)
            }
        }
    }

    private fun productCardModel(element: ProductItemDataView) =
        ProductCardModel(
            imageUrl = element.imageUrl300,
            isAds = element.isAds,
            name = element.productName,
            price = element.priceRange.ifEmpty { element.price },
            slashedPrice = element.originalPrice,
            discountPercentage = element.discountPercentage,
            labelGroupList = labelGroupList(element),
            rating = element.ratingString,
            shopBadge = shopBadge(element),
            freeShipping = freeShipping(element),
            videoUrl = element.customVideoURL,
            hasThreeDots = true,
            isImageBlurred = element.isImageBlurred
        )

    private fun labelGroupList(element: ProductItemDataView) =
        element.labelGroupList?.map(::labelGroup) ?: listOf()

    private fun labelGroup(labelGroupDataView: LabelGroupDataView) =
        ProductCardModel.LabelGroup(
            position = labelGroupDataView.position,
            title = labelGroupDataView.title,
            type = labelGroupDataView.type,
            imageUrl = labelGroupDataView.imageUrl,
        )

    private fun shopBadge(element: ProductItemDataView): ProductCardModel.ShopBadge {
        val shopBadge = element.badgesList?.firstOrNull()
        return ProductCardModel.ShopBadge(
            imageUrl = shopBadge?.imageUrl ?: "",
            title = shopBadge?.title ?: ""
        )
    }

    private fun freeShipping(element: ProductItemDataView) =
        ProductCardModel.FreeShipping(
            imageUrl = element.freeOngkirDataView.imageUrl,
        )

    override fun onViewRecycled() {
        binding?.searchProductCardGridReimagine?.recycle()
    }

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_card_reimagine_grid
    }
}
