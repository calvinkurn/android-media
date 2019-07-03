package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.android.synthetic.main.search_product_card_small_grid.view.*

class SmallGridProductItemViewHolder(
    itemView: View,
    productListener: ProductListener
) : ProductItemViewHolder(itemView, productListener) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_product_card_small_grid
    }

    // TODO:: Just for testing, remove this later
    override fun bind(productItem: ProductItemViewModel?) {
        if (productItem == null) return

        initProductCardContainer(productItem)
        initProductImage(productItem)
        initWishlistButton(productItem)
        initPromoLabel(productItem)
        initShopImage(productItem)
        initShopName(productItem)
        initTitleTextView(productItem)
        initSlashedPriceSection(productItem)
        initPriceTextView(productItem)
        initShopBadge(productItem)
        initLocationTextView(productItem)
        initCredibilitySection(productItem)
        initOffersLabel(productItem)
        initTopAdsIcon(productItem)

        finishBindViewHolder()
    }

    override fun getProductCardView(): ProductCardView? {
        return itemView.productCardView ?: null
    }

    override fun isUsingBigImageUrl(): Boolean {
        return false
    }

    // TODO:: Just for testing, remove this later
    protected fun initShopImage(productItem: ProductItemViewModel) {
        itemView.productCardView?.setImageShopVisible(true)
        itemView.productCardView?.setImageShopUrl(productItem.imageUrl)
    }
}