package com.tokopedia.productcard.carousel

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.productcard.R
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.product_card_carousel_layout.view.*
import kotlinx.android.synthetic.main.product_card_layout_v2_list.view.*

class ProductCardCarouselView: BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.product_card_carousel_layout, this)
    }

    fun setProductModelList(
            productCardModelList: List<ProductCardModel>,
            isScrollable: Boolean = true,
            productCardCarouselOnItemClickListener: ProductCardCarouselListener.OnItemClickListener? = null,
            productCardCarouselOnItemLongClickListener: ProductCardCarouselListener.OnItemLongClickListener? = null,
            productCardCarouselOnItemImpressedListener: ProductCardCarouselListener.OnItemImpressedListener? = null,
            productCardCarouselOnItemAddToCartListener: ProductCardCarouselListener.OnItemAddToCartListener? = null,
            productCardCarouselOnWishlistItemClickListener: ProductCardCarouselListener.OnWishlistItemClickListener? = null) {

        val blankSpaceConfig = createBlankSpaceConfig(productCardModelList)

        val productCardCarouselListenerInfo = ProductCardCarouselListenerInfo().also {
            it.onItemClickListener = productCardCarouselOnItemClickListener
            it.onItemLongClickListener = productCardCarouselOnItemLongClickListener
            it.onItemImpressedListener = productCardCarouselOnItemImpressedListener
            it.onItemAddToCartListener = productCardCarouselOnItemAddToCartListener
            it.onWishlistItemClickListener = productCardCarouselOnWishlistItemClickListener
        }

        productCardCarouselRecyclerView?.adapter = ProductCardCarouselAdapter(
                productCardModelList, isScrollable, productCardCarouselListenerInfo, blankSpaceConfig)

        productCardCarouselRecyclerView?.layoutManager = createProductcardCarouselLayoutManager(isScrollable, productCardModelList.size)
    }

    private fun createBlankSpaceConfig(productCardModelList: List<ProductCardModel>): BlankSpaceConfig {
        val blankSpaceConfig = BlankSpaceConfig()

        productCardModelList.forEach {
            blankSpaceConfig.shopName = blankSpaceConfig.shopName || it.shopName.isNotEmpty()
            blankSpaceConfig.productName = blankSpaceConfig.productName || it.productName.isNotEmpty()
            blankSpaceConfig.discountPercentage = blankSpaceConfig.discountPercentage || (it.discountPercentage.isNotEmpty() && it.discountPercentage.trim() != "0")
            blankSpaceConfig.slashedPrice = blankSpaceConfig.slashedPrice || it.slashedPrice.isNotEmpty()
            blankSpaceConfig.price = blankSpaceConfig.price || it.formattedPrice.isNotEmpty()
            blankSpaceConfig.shopBadge = blankSpaceConfig.shopBadge || it.shopBadgeList.any { shopBadge -> shopBadge.isShown }
            blankSpaceConfig.shopLocation = blankSpaceConfig.shopLocation || it.shopLocation.isNotEmpty()
            blankSpaceConfig.ratingCount = blankSpaceConfig.ratingCount || it.ratingCount > 0
            blankSpaceConfig.reviewCount = blankSpaceConfig.reviewCount || it.reviewCount > 0
            blankSpaceConfig.labelCredibility = blankSpaceConfig.labelCredibility || (it.ratingCount == 0 && it.reviewCount == 0 && it.labelCredibility.title.isNotEmpty())
            blankSpaceConfig.labelOffers = blankSpaceConfig.labelOffers || it.labelOffers.title.isNotEmpty()
            blankSpaceConfig.freeOngkir = blankSpaceConfig.freeOngkir || (it.freeOngkir.isActive && it.freeOngkir.imageUrl.isNotEmpty())
        }

        return blankSpaceConfig
    }

    private fun createProductcardCarouselLayoutManager(isScrollable: Boolean, productCardModelListSize: Int): RecyclerView.LayoutManager {
        return if (isScrollable) {
            GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        } else {
            GridLayoutManager(context, productCardModelListSize, GridLayoutManager.VERTICAL, false)
        }
    }
}