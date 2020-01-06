package com.tokopedia.carouselproductcard

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.carouselproductcard.common.CarouselProductPool
import com.tokopedia.carouselproductcard.helper.CarouselProductCardDefaultDecorator
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import kotlinx.android.synthetic.main.carousel_product_card_layout.view.*
import com.tokopedia.carouselproductcard.helper.StartSnapHelper
import com.tokopedia.carouselproductcard.model.CarouselProductCardModel
import com.tokopedia.productcard.v2.BlankSpaceConfig

class CarouselProductCardView: BaseCustomView {

    private var carouselLayoutManager: RecyclerView.LayoutManager? = null
    private val defaultRecyclerViewDecorator = CarouselProductCardDefaultDecorator()

    /**
     * If you're working with recyclerview, give it dedicated carouselProductPool for better performance
     * because the calculation process of product card height can be expensive
     */
    var carouselProductPool: CarouselProductPool = CarouselProductPool()

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
        if (carouselProductCardRecyclerView.itemDecorationCount > 0)
            carouselProductCardRecyclerView.removeItemDecorationAt(0)
        carouselProductCardRecyclerView.addItemDecoration(defaultRecyclerViewDecorator)
    }

    /**
     * @param activity is used to determine device width.
     * @param parentView is used to measure view according to its parent.
     * @param productCardModelList collection of product model to define max height.
     * @param isScrollable differentiate between carousel and non carousel recyclerview.
     * @param activity mandatory if isScrollable is false, to measure device width.
     * @param parentView mandatory if isScrollable is false, to measure device width.
     * @param deviceWidth alternative if you provide your own view total width.
     * @param recyclerviewDecorator alternative if you provide your own recyclerview decorator.
     * @param carouselModelId alternative if you want to share pool. Just pass any unique id
     * to differentiate your item in pool
     */
    fun bindCarouselProductCardView(
            activity: Activity? = null,
            deviceWidth: Int = 0,
            parentView: View? = null,
            carouselModelId: String? = null,
            productCardModelList: List<ProductCardModel>,
            isScrollable: Boolean = true,
            carouselProductCardOnItemClickListener: CarouselProductCardListener.OnItemClickListener? = null,
            carouselProductCardOnItemLongClickListener: CarouselProductCardListener.OnItemLongClickListener? = null,
            carouselProductCardOnItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener? = null,
            carouselProductCardOnItemAddToCartListener: CarouselProductCardListener.OnItemAddToCartListener? = null,
            carouselProductCardOnWishlistItemClickListener: CarouselProductCardListener.OnWishlistItemClickListener? = null) {

        if (productCardModelList.isEmpty()) return

        val carouselProductCardListenerInfo = CarouselProductCardListenerInfo().also {
            it.onItemClickListener = carouselProductCardOnItemClickListener
            it.onItemLongClickListener = carouselProductCardOnItemLongClickListener
            it.onItemImpressedListener = carouselProductCardOnItemImpressedListener
            it.onItemAddToCartListener = carouselProductCardOnItemAddToCartListener
            it.onWishlistItemClickListener = carouselProductCardOnWishlistItemClickListener
        }

        val knownPosition = carouselProductPool.carouselAdaptersPositions[carouselModelId]

        carouselLayoutManager = createProductcardCarouselLayoutManager(isScrollable, productCardModelList.size)
        val newCarouselAdapter = CarouselProductCardAdapter()
        setupCarouselProductCardRecyclerView(newCarouselAdapter)
        submitProductCardCarouselData(newCarouselAdapter, productCardModelList, carouselProductCardListenerInfo, computeBlankSpaceConfig(productCardModelList))

        carouselLayoutManager?.run {
            if (this is LinearLayoutManager) {
                scrollToPositionWithOffset(knownPosition?:0,
                        context.applicationContext.resources.getDimensionPixelOffset(
                                R.dimen.dp_16
                        ))
            }
        }
    }

    private fun submitProductCardCarouselData(newCarouselAdapter: CarouselProductCardAdapter,
                                              productCardModelList: List<ProductCardModel>,
                                              carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
                                              blankSpaceConfig: BlankSpaceConfig) {
        newCarouselAdapter.submitList(productCardModelList.map {
            CarouselProductCardModel(it, carouselProductCardListenerInfo, blankSpaceConfig)
        })
    }

    private fun setupCarouselProductCardRecyclerView(newCarouselAdapter: CarouselProductCardAdapter) {
        val snapHelper = StartSnapHelper()
        if (carouselProductCardRecyclerView.onFlingListener == null) {
            snapHelper.attachToRecyclerView(carouselProductCardRecyclerView)
        }
        carouselProductCardRecyclerView?.layoutManager = carouselLayoutManager
        carouselProductCardRecyclerView.itemAnimator = null
        carouselProductCardRecyclerView.setHasFixedSize(true)
        carouselProductCardRecyclerView?.adapter = newCarouselAdapter
    }

    private fun computeBlankSpaceConfig(productCardModelList: List<ProductCardModel>): BlankSpaceConfig {
        val blankSpaceConfig = BlankSpaceConfig(
                twoLinesProductName = true
        )

        productCardModelList.forEach {
            if (it.freeOngkir.isActive) blankSpaceConfig.freeOngkir = true
            if (it.shopName.isNotEmpty()) blankSpaceConfig.shopName = true
            if (it.productName.isNotEmpty()) blankSpaceConfig.productName = true
            if (it.labelPromo.title.isNotEmpty()) blankSpaceConfig.labelPromo = true
            if (it.slashedPrice.isNotEmpty()) blankSpaceConfig.slashedPrice = true
            if (it.formattedPrice.isNotEmpty()) blankSpaceConfig.price = true
            if (it.shopBadgeList.isNotEmpty()) blankSpaceConfig.shopBadge = true
            if (it.shopLocation.isNotEmpty()) blankSpaceConfig.shopLocation = true
            if (it.ratingCount != 0) blankSpaceConfig.ratingCount = true
            if (it.reviewCount != 0) blankSpaceConfig.reviewCount = true
            if (it.labelCredibility.title.isNotEmpty()) blankSpaceConfig.labelCredibility = true
            if (it.labelOffers.title.isNotEmpty()) blankSpaceConfig.labelOffers = true
            if (it.isTopAds) blankSpaceConfig.topAdsIcon = true
        }
        return blankSpaceConfig
    }

    /**
     * Use this function onViewRecycled to retains scroll position
     */
    fun onViewRecycled(carouselModelId: String) {
        if (carouselModelId.isEmpty()) return
        carouselLayoutManager?.let {
            if (it is LinearLayoutManager) {
                val positionState = it.findFirstCompletelyVisibleItemPosition()
                carouselProductPool.carouselAdaptersPositions[carouselModelId] = positionState
            }
        }
    }

    /**
     * Invalidate recyclerview to produce new adapter
     */
    fun invalidateRecyclerView() {
        carouselProductPool.carouselAdapters.clear()
        carouselLayoutManager = null
    }

    /**
     * Use this function to update wishlist
     */
    fun updateWishlist(position: Int, isWishlist: Boolean) {
        (carouselProductCardRecyclerView.adapter as CarouselProductCardAdapter).updateWishlist(position, isWishlist)
    }

    fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration){
        if(carouselProductCardRecyclerView?.itemDecorationCount == 0) carouselProductCardRecyclerView?.addItemDecoration(itemDecoration)
    }

    fun setSnapHelper(snapHelper: SnapHelper){
        snapHelper.attachToRecyclerView(carouselProductCardRecyclerView)
    }

    private fun createProductcardCarouselLayoutManager(isScrollable: Boolean, productCardModelListSize: Int): RecyclerView.LayoutManager {
        return if (isScrollable) {
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            GridLayoutManager(context, productCardModelListSize, GridLayoutManager.VERTICAL, false)
        }
    }
}