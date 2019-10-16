package com.tokopedia.carouselproductcard

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.carousel_product_card_layout.view.*

class CarouselProductCardView: BaseCustomView {

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
        View.inflate(context, R.layout.carousel_product_card_layout, this)
    }

    fun setProductModelList(
            productCardModelList: List<ProductCardModel>,
            isScrollable: Boolean = true,
            carouselProductCardOnItemClickListener: CarouselProductCardListener.OnItemClickListener? = null,
            carouselProductCardOnItemLongClickListener: CarouselProductCardListener.OnItemLongClickListener? = null,
            carouselProductCardOnItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener? = null,
            carouselProductCardOnItemAddToCartListener: CarouselProductCardListener.OnItemAddToCartListener? = null,
            carouselProductCardOnWishlistItemClickListener: CarouselProductCardListener.OnWishlistItemClickListener? = null) {

        val blankSpaceConfig = createBlankSpaceConfig(productCardModelList)

        val carouselProductCardListenerInfo = CarouselProductCardListenerInfo().also {
            it.onItemClickListener = carouselProductCardOnItemClickListener
            it.onItemLongClickListener = carouselProductCardOnItemLongClickListener
            it.onItemImpressedListener = carouselProductCardOnItemImpressedListener
            it.onItemAddToCartListener = carouselProductCardOnItemAddToCartListener
            it.onWishlistItemClickListener = carouselProductCardOnWishlistItemClickListener
        }

        carouselProductCardRecyclerView?.adapter = CarouselProductCardAdapter(
                productCardModelList, isScrollable, carouselProductCardListenerInfo, blankSpaceConfig)

        carouselProductCardRecyclerView?.layoutManager = createProductcardCarouselLayoutManager(isScrollable, productCardModelList.size)
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
            blankSpaceConfig.twoLinesProductName = true
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