package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.Nullable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener

const val LABEL_GROUP_POSITION_PROMO = "promo"
const val LABEL_GROUP_POSITION_CREDIBILITY = "credibility"
const val LABEL_GROUP_POSITION_OFFERS = "offers"

abstract class ProductItemViewHolder(
    itemView: View,
    protected val productListener: ProductListener
) : AbstractViewHolder<ProductItemViewModel>(itemView) {

    protected val context = itemView.context!!

    override fun bind(productItem: ProductItemViewModel?) {
        if (productItem == null) return

        val promoLabelViewModel : LabelGroupViewModel? = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_PROMO)
        val productCardLabelPromoModel = ProductCardModel.Label(promoLabelViewModel?.title ?: "", promoLabelViewModel?.type ?: "")

        val credibilityLabelViewModel : LabelGroupViewModel? = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY)
        val productCardLabelCredibilityModel =
                if (productItem.rating == 0 && productItem.countReview == 0)
                    ProductCardModel.Label(credibilityLabelViewModel?.title ?: "", credibilityLabelViewModel?.type ?: "")
                else
                    ProductCardModel.Label()

        val offersLabelViewModel = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_OFFERS)
        val productCardLabelOffersModel = ProductCardModel.Label(offersLabelViewModel?.title ?: "", offersLabelViewModel?.type ?: "")

        val shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>()
        productItem.badgesList.forEach {
            shopBadgeList.add(ProductCardModel.ShopBadge(it.isShown, it.imageUrl))
        }

        val productCardModel = ProductCardModel(
                productImageUrl = if (isUsingBigImageUrl()) productItem.imageUrl700 else productItem.imageUrl,
                isWishlistVisible = productItem.isWishlistButtonEnabled,
                isWishlisted = productItem.isWishlisted,
                labelPromo = productCardLabelPromoModel,
                shopName = if (isShopNameShown(productItem)) productItem.shopName else "",
                productName = productItem.productName,
                discountPercentage = if (isLabelDiscountVisible(productItem)) "${productItem.discountPercentage}%" else "",
                slashedPrice = if (isLabelDiscountVisible(productItem)) productItem.originalPrice else "",
                formattedPrice = getPriceText(productItem),
                shopBadgeList = shopBadgeList,
                shopLocation = productItem.shopCity,
                ratingCount = getStarCount(productItem),
                reviewCount = productItem.countReview,
                labelCredibility = productCardLabelCredibilityModel,
                labelOffers = productCardLabelOffersModel,
                isTopAds = productItem.isTopAds
        )

        getProductCardView()?.setProductModel(productCardModel)

        getProductCardView()?.setOnLongClickListener {
            productListener.onLongClick(productItem, adapterPosition)
            true
        }

        getProductCardView()?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }

        getProductCardView()?.setImageProductViewHintListener(productItem, createImageProductViewHintListener(productItem))

        getProductCardView()?.setButtonWishlistOnClickListener {
            if (productItem.isWishlistButtonEnabled) {
                productListener.onWishlistButtonClicked(productItem)
            }
        }
    }

    protected abstract fun getProductCardView(): ProductCardView?

    protected abstract fun isUsingBigImageUrl(): Boolean

    protected fun createImageProductViewHintListener(productItem: ProductItemViewModel): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                productListener.onProductImpressed(productItem, adapterPosition)
            }
        }
    }

    @Nullable
    protected fun getFirstLabelGroupOfPosition(productItem: ProductItemViewModel, position: String): LabelGroupViewModel? {
        val labelGroupOfPosition = getLabelGroupOfPosition(productItem, position)

        return if(labelGroupOfPosition != null && labelGroupOfPosition.isNotEmpty()) labelGroupOfPosition[0] else null
    }

    @Nullable
    protected fun getLabelGroupOfPosition(productItem: ProductItemViewModel, position: String): List<LabelGroupViewModel>? {
        return productItem.labelGroupList.filter { labelGroup -> labelGroup.position == position }
    }

    protected fun isShopNameShown(productItem: ProductItemViewModel): Boolean {
        return productItem.isShopOfficialStore
    }

    protected fun isLabelDiscountVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.discountPercentage > 0
    }

    protected fun getPriceText(productItem: ProductItemViewModel) : String {
        return if(!TextUtils.isEmpty(productItem.priceRange)) productItem.priceRange
        else productItem.price
    }

    protected fun getStarCount(productItem: ProductItemViewModel): Int {
        return if (productItem.isTopAds)
            Math.round(productItem.rating / 20f)
        else
            Math.round(productItem.rating.toFloat())
    }
}