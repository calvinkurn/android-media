package com.tokopedia.imagesearch.search.fragment.product.adapter.viewholder

import androidx.annotation.LayoutRes
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.imagesearch.R
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem
import com.tokopedia.imagesearch.search.fragment.product.adapter.listener.ProductListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.image_search_result_product_item.view.*
import kotlin.math.roundToInt

/**
 * Created by henrypriyono on 10/11/17.
 */

class GridProductItemViewHolder(itemView: View,
                                protected val productListener: ProductListener,
                                protected val searchQuery: String) : AbstractViewHolder<ProductItem>(itemView) {

    protected val context = itemView.context!!

    override fun bind(productItem: ProductItem) {

        val productCardShopBadgesList = createProductCardShopBadges(productItem)

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
                isTopAds = productItem.isTopAds
        )
        itemView.productCardView?.setProductModel(productCardModel)

        itemView.productCardView?.setOnLongClickListener {
            productListener.onLongClick(productItem, adapterPosition)
            true
        }

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

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.image_search_result_product_item
    }
}