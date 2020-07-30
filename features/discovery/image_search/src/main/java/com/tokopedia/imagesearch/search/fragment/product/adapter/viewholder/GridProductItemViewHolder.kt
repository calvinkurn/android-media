package com.tokopedia.imagesearch.search.fragment.product.adapter.viewholder

import androidx.annotation.LayoutRes
import android.text.TextUtils
import android.view.View
import androidx.annotation.Nullable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.imagesearch.R
import com.tokopedia.imagesearch.domain.viewmodel.LabelGroup
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.ProductListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.image_search_result_product_item.view.*
import kotlin.math.roundToInt

const val LABEL_GROUP_POSITION_PROMO = "promo"
const val LABEL_GROUP_POSITION_CREDIBILITY = "credibility"
const val LABEL_GROUP_POSITION_OFFERS = "offers"

class GridProductItemViewHolder(itemView: View,
                                protected val productListener: ProductListener) : AbstractViewHolder<ProductItem>(itemView) {

    protected val context = itemView.context!!

    override fun bind(productItem: ProductItem) {

        val productCardLabelPromoModel = createProductCardLabelPromo(productItem)
        val productCardLabelCredibilityModel = createProductCardLabelCredibility(productItem)
        val productCardLabelOffersModel = createProductCardLabelOffers(productItem)
        val productCardShopBadgesList = createProductCardShopBadges(productItem)
        val productCardFreeOngkir = createProductCardFreeOngkir(productItem)

        val productCardModel = ProductCardModel(
                productImageUrl = productItem.imageUrl,
                isWishlistVisible = productItem.isWishlistButtonEnabled,
                isWishlisted = productItem.isWishlisted,
                shopName = if (isShopNameShown(productItem)) productItem.shopName else "",
                productName = productItem.productName,
                discountPercentage = if (isLabelDiscountVisible(productItem)) "${productItem.discountPercentage}%" else "",
                slashedPrice = if (isLabelDiscountVisible(productItem)) productItem.originalPrice else "",
                formattedPrice = getPriceText(productItem),
                shopBadgeList = productCardShopBadgesList,
                shopLocation = productItem.shopCity,
                ratingCount = getStarCount(productItem),
                reviewCount = productItem.countReview,
                isTopAds = productItem.isTopAds,
                labelPromo = productCardLabelPromoModel,
                labelCredibility = productCardLabelCredibilityModel,
                labelOffers = productCardLabelOffersModel,
                freeOngkir = productCardFreeOngkir
        )
        itemView.productCardView?.setProductModel(productCardModel)

        itemView.productCardView?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }

        itemView.productCardView?.setImageProductViewHintListener(productItem, createImageProductViewHintListener(productItem))

        itemView.productCardView?.setButtonWishlistOnClickListener {
            if (productItem.isWishlistButtonEnabled) {
                productListener.onWishlistButtonClicked(productItem)
            }
        }
    }

    private fun createProductCardShopBadges(productItem: ProductItem): List<ProductCardModel.ShopBadge> {
        val shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>()

        productItem.badgesList.forEach {
            shopBadgeList.add(ProductCardModel.ShopBadge(it.isShown, it.imageUrl))
        }

        return shopBadgeList
    }

    protected fun createImageProductViewHintListener(productItem: ProductItem): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                productListener.onProductImpressed(productItem, adapterPosition)
            }
        }
    }

    protected fun isShopNameShown(productItem: ProductItem): Boolean {
        return productItem.isOfficial
    }

    protected fun isLabelDiscountVisible(productItem: ProductItem): Boolean {
        return productItem.discountPercentage > 0
    }

    protected fun getPriceText(productItem: ProductItem) : String {
        return if(!TextUtils.isEmpty(productItem.priceRange)) productItem.priceRange
        else productItem.price
    }

    protected fun getStarCount(productItem: ProductItem): Int {
        return if (productItem.isTopAds)
            (productItem.rating / 20f).roundToInt()
        else
            productItem.rating
    }

    private fun createProductCardLabelPromo(productItem: ProductItem): ProductCardModel.Label {
        val promoLabelViewModel =
                getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_PROMO)
                        ?: return ProductCardModel.Label()

        return ProductCardModel.Label(promoLabelViewModel.title, promoLabelViewModel.type)
    }

    private fun createProductCardLabelCredibility(productItem: ProductItem): ProductCardModel.Label {
        val credibilityLabelViewModel =
                getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY)
                        ?: return ProductCardModel.Label()

        val isLabelCredibilityShown = isLabelCredibilityShown(productItem)

        val title = if (isLabelCredibilityShown) credibilityLabelViewModel.title else ""
        val type = if (isLabelCredibilityShown) credibilityLabelViewModel.type else ""

        return ProductCardModel.Label(title, type)
    }

    private fun isLabelCredibilityShown(productItem: ProductItem): Boolean {
        return productItem.rating == 0 && productItem.countReview == 0
    }

    private fun createProductCardLabelOffers(productItem: ProductItem): ProductCardModel.Label {
        val offersLabelViewModel =
                getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_OFFERS)
                        ?: return ProductCardModel.Label()

        return ProductCardModel.Label(offersLabelViewModel.title, offersLabelViewModel.type)
    }

    @Nullable
    protected fun getFirstLabelGroupOfPosition(productItem: ProductItem, position: String): LabelGroup? {
        val labelGroupOfPosition = getLabelGroupOfPosition(productItem, position)

        return if(labelGroupOfPosition != null && labelGroupOfPosition.isNotEmpty()) labelGroupOfPosition[0] else null
    }

    @Nullable
    protected fun getLabelGroupOfPosition(productItem: ProductItem, position: String): List<LabelGroup>? {
        return productItem.labelGroupList.filter { labelGroup -> labelGroup.position == position }
    }

    private fun createProductCardFreeOngkir(productItem: ProductItem): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(
                productItem.freeOngkir.isActive,
                productItem.freeOngkir.imageUrl
        )
    }

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.image_search_result_product_item
    }
}