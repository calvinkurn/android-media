package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.text.TextUtils
import android.view.View
import androidx.annotation.Nullable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlin.math.roundToInt

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

        val productCardLabelPromoModel = createProductCardLabelPromo(productItem)
        val productCardLabelCredibilityModel = createProductCardLabelCredibility(productItem)
        val productCardLabelOffersModel = createProductCardLabelOffers(productItem)
        val productCardShopBadgesList = createProductCardShopBadges(productItem)
        val productCardFreeOngkir = createProductCardFreeOngkir(productItem)

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
                shopBadgeList = productCardShopBadgesList,
                shopLocation = productItem.shopCity,
                ratingCount = getStarCount(productItem),
                reviewCount = productItem.countReview,
                labelCredibility = productCardLabelCredibilityModel,
                labelOffers = productCardLabelOffersModel,
                freeOngkir = productCardFreeOngkir,
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

    private fun createProductCardLabelPromo(productItem: ProductItemViewModel): ProductCardModel.Label {
        val promoLabelViewModel =
                getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_PROMO)
                        ?: return ProductCardModel.Label()

        return ProductCardModel.Label(promoLabelViewModel.title, promoLabelViewModel.type)
    }

    private fun createProductCardLabelCredibility(productItem: ProductItemViewModel): ProductCardModel.Label {
        val credibilityLabelViewModel =
                getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY)
                        ?: return ProductCardModel.Label()

        val isLabelCredibilityShown = isLabelCredibilityShown(productItem)

        val title = if (isLabelCredibilityShown) credibilityLabelViewModel.title else ""
        val type = if (isLabelCredibilityShown) credibilityLabelViewModel.type else ""

        return ProductCardModel.Label(title, type)
    }

    private fun isLabelCredibilityShown(productItem: ProductItemViewModel): Boolean {
        return productItem.rating == 0 && productItem.countReview == 0
    }

    private fun createProductCardLabelOffers(productItem: ProductItemViewModel): ProductCardModel.Label {
        val offersLabelViewModel =
                getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_OFFERS)
                        ?: return ProductCardModel.Label()

        return ProductCardModel.Label(offersLabelViewModel.title, offersLabelViewModel.type)
    }

    private fun createProductCardShopBadges(productItem: ProductItemViewModel): List<ProductCardModel.ShopBadge> {
        val shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>()

        productItem.badgesList.forEach {
            shopBadgeList.add(ProductCardModel.ShopBadge(it.isShown, it.imageUrl))
        }

        return shopBadgeList
    }

    private fun createProductCardFreeOngkir(productItem: ProductItemViewModel): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(
                productItem.freeOngkirViewModel.isActive,
                productItem.freeOngkirViewModel.imageUrl
        )
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
            (productItem.rating / 20f).roundToInt()
        else
            productItem.rating
    }
}