package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product

import android.view.View
import androidx.annotation.Nullable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.data.productModel.LabelGroupsItem
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardView


const val LABEL_GROUP_POSITION_PROMO = "promo"
const val LABEL_GROUP_POSITION_CREDIBILITY = "credibility"
const val LABEL_GROUP_POSITION_OFFERS = "offers"

abstract class ProductCardViewHolder(itemView: View,
                                     var productListener: ProductCardListener) : AbstractViewHolder<ProductsItem>(itemView) {

    protected val context = itemView.context!!

    override fun bind(productItem: ProductsItem?) {
        if (productItem == null) return
        val promoLabelViewModel: LabelGroupsItem? = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_PROMO)
        val credibilityLabelViewModel: LabelGroupsItem? = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY)
        val offersLabelViewModel = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_OFFERS)
        getProductCardView()?.setProductModel(
            ProductCardModel(
                    slashedPrice = productItem.originalPrice,
                    productName = productItem.name,
                    formattedPrice = productItem.price,
                    productImageUrl = if (isUsingBigImageUrl()) productItem.imageURL700 else productItem.imageURL,
                    isTopAds = productItem.isTopAds,
                    discountPercentage = productItem.discountPercentage.toString(),
                    reviewCount = productItem.countReview,
                    ratingCount = productItem.rating,
                    shopLocation = productItem.shop.location,
                    isWishlistVisible = true,
                    isWishlisted = productItem.wishlist,
                    shopBadgeList = productItem.badges.map {
                        ProductCardModel.ShopBadge(imageUrl = it.imageURL ?: "", isShown = it.show)
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                            isActive = productItem.freeOngkir?.isActive ?: false,
                            imageUrl = productItem.freeOngkir?.imageUrl ?: ""
                    ),
                    shopName = productItem.shop.name,
                    labelPromo = ProductCardModel.Label(promoLabelViewModel?.title ?: "", promoLabelViewModel?.type ?: ""),
                    labelCredibility = ProductCardModel.Label(credibilityLabelViewModel?.title ?: "", credibilityLabelViewModel?.type ?: ""),
                    labelOffers = ProductCardModel.Label(offersLabelViewModel?.title ?: "", offersLabelViewModel?.type ?: "")
            )
        )
        initProductCardContainer(productItem)
        finishBindViewHolder()
        getProductCardView()?.setButtonWishlistOnClickListener{
            if (productItem.isWishListEnabled) {
                productListener.onWishlistButtonClicked(productItem,adapterPosition)
            }
        }
    }

    protected abstract fun getProductCardView(): ProductCardView?

    protected abstract fun isUsingBigImageUrl(): Boolean


    protected fun initProductCardContainer(productItem: ProductsItem) {
        getProductCardView()?.setOnLongClickListener {
            true
        }

        getProductCardView()?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }
    }

    @Nullable
    protected fun getFirstLabelGroupOfPosition(productItem: ProductsItem, position: String): LabelGroupsItem? {
        val labelGroupOfPosition = getLabelGroupOfPosition(productItem, position)

        return if (labelGroupOfPosition != null && labelGroupOfPosition.isNotEmpty()) labelGroupOfPosition[0] else null
    }

    @Nullable
    protected fun getLabelGroupOfPosition(productItem: ProductsItem, position: String): List<LabelGroupsItem?> {
        return productItem.labelGroups.filter { labelGroup -> labelGroup?.position == position }
    }

    private fun finishBindViewHolder() {
        getProductCardView()?.realignLayout()
    }


}