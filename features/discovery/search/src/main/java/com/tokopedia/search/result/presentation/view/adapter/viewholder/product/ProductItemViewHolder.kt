package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.video.ProductVideoPlayer
import com.tokopedia.productcard.video.VideoPlayerState
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.model.LabelGroupVariantDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

abstract class ProductItemViewHolder(
        itemView: View,
        protected val productListener: ProductListener,
) : AbstractViewHolder<ProductItemDataView>(itemView), ProductVideoPlayer {

    abstract val productCardView: IProductCardView?

    protected var productCardModel: ProductCardModel? = null

    protected fun ProductItemDataView.toProductCardModel(
        productImage: String,
        isWideContent: Boolean,
    ): ProductCardModel {
        return ProductCardModel(
            productImageUrl = productImage,
            productName = productName,
            discountPercentage = if (discountPercentage > 0) "$discountPercentage%" else "",
            slashedPrice = if (discountPercentage > 0) originalPrice else "",
            formattedPrice = price,
            priceRange = priceRange ?: "",
            shopBadgeList = badgesList.toProductCardModelShopBadges(),
            shopLocation = if (shopCity.isNotEmpty()) shopCity else shopName,
            freeOngkir = freeOngkirDataView.toProductCardModelFreeOngkir(),
            isTopAds = isTopAds || isOrganicAds,
            countSoldRating = ratingString,
            hasThreeDots = true,
            labelGroupList = labelGroupList.toProductCardModelLabelGroup(),
            labelGroupVariantList = labelGroupVariantList.toProductCardModelLabelGroupVariant(),
            isWideContent = isWideContent,
            customVideoURL = customVideoURL
        )
    }

    private fun List<BadgeItemDataView>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(it.isShown, it.imageUrl)
        } ?: listOf()
    }

    private fun FreeOngkirDataView.toProductCardModelFreeOngkir(): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }

    private fun List<LabelGroupDataView>?.toProductCardModelLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type, imageUrl = it.imageUrl)
        } ?: listOf()
    }

    private fun List<LabelGroupVariantDataView>?.toProductCardModelLabelGroupVariant(): List<ProductCardModel.LabelGroupVariant> {
        return this?.map {
            ProductCardModel.LabelGroupVariant(type = it.type, typeVariant = it.typeVariant, title = it.title, hexColor = it.hexColor)
        } ?: listOf()
    }

    protected fun createImageProductViewHintListener(productItemData: ProductItemDataView): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                productListener.onProductImpressed(productItemData, adapterPosition)
            }
        }
    }

    fun registerLifecycleObserver(productCardModel: ProductCardModel) {
        val productCardView = productCardView ?: return

        productListener.productCardLifecycleObserver?.register(productCardView, productCardModel)
    }

    override fun onViewRecycled() {
        val productCardView = this.productCardView ?: return

        productCardView.recycle()
        productListener.productCardLifecycleObserver?.unregister(productCardView)
    }

    override val hasProductVideo: Boolean
        get() = productCardView?.getProductCardVideo()?.hasProductVideo == true

    override fun playVideo(): Flow<VideoPlayerState> {
        return productCardView?.getProductCardVideo()?.playVideo() ?: flowOf(VideoPlayerState.NoVideo)
    }

    override fun stopVideo() {
        productCardView?.getProductCardVideo()?.stopVideo()
    }
}