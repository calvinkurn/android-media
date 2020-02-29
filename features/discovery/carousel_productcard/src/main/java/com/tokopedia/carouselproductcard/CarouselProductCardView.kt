package com.tokopedia.carouselproductcard

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.helper.StartSnapHelper
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.productcard.v2.BlankSpaceConfig
import kotlinx.android.synthetic.main.carousel_product_card_layout.view.*
import kotlinx.coroutines.*

class CarouselProductCardView : BaseCustomView, CoroutineScope {


    private var carouselLayoutManager: RecyclerView.LayoutManager? = null
    private val defaultRecyclerViewDecorator = CarouselProductCardDefaultDecorator()
    private val carouselAdapter = CarouselProductCardAdapter()
    private val snapHelper = StartSnapHelper()
    private var isUseDefaultItemDecorator = true
    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    constructor(context: Context): super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }
    
    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.carousel_product_card_layout, this)

        defineCustomAttributes(attrs)

        addDefaultItemDecorator()
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CarouselProductCardView, 0, 0)

            try {
                tryDefineCustomAttributes(styledAttributes)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun tryDefineCustomAttributes(styledAttributes: TypedArray) {
        isUseDefaultItemDecorator = styledAttributes.getBoolean(R.styleable.CarouselProductCardView_useDefaultItemDecorator, true)
    }

    private fun addDefaultItemDecorator() {
        if (isUseDefaultItemDecorator) {
            if (carouselProductCardRecyclerView.itemDecorationCount > 0)
                carouselProductCardRecyclerView.removeItemDecorationAt(0)

            carouselProductCardRecyclerView.addItemDecoration(defaultRecyclerViewDecorator)
        }
    }

    fun bindCarouselProductCardViewGrid(
            productCardModelList: List<ProductCardModel>,
            carouselProductCardOnItemClickListener: CarouselProductCardListener.OnItemClickListener? = null,
            carouselProductCardOnItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener? = null,
            carouselProductCardOnItemAddToCartListener: CarouselProductCardListener.OnItemAddToCartListener? = null,
            recyclerViewPool: RecyclerView.RecycledViewPool? = null,
            scrollToPosition: Int = 0
    ) {
        if (productCardModelList.isEmpty()) return

        val carouselProductCardListenerInfo = CarouselProductCardListenerInfo().also {
            it.onItemClickListener = carouselProductCardOnItemClickListener
            it.onItemImpressedListener = carouselProductCardOnItemImpressedListener
            it.onItemAddToCartListener = carouselProductCardOnItemAddToCartListener
        }

        launch {
            try {
                tryBindCarousel(productCardModelList, carouselProductCardListenerInfo, recyclerViewPool, scrollToPosition)
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private suspend fun tryBindCarousel(
            productCardModelList: List<ProductCardModel>,
            carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
            recyclerViewPool: RecyclerView.RecycledViewPool? = null,
            scrollToPosition: Int = 0
    ) {
        initLayoutManager()
        initRecyclerView(productCardModelList, recyclerViewPool)
        submitList(productCardModelList, carouselProductCardListenerInfo)
        scrollCarousel(scrollToPosition)
    }

    private fun initLayoutManager() {
        carouselLayoutManager = createProductCardCarouselLayoutManager()
    }

    private fun createProductCardCarouselLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private suspend fun initRecyclerView(
            productCardModelList: List<ProductCardModel>,
            recyclerViewPool: RecyclerView.RecycledViewPool?
    ) {
        carouselProductCardRecyclerView?.layoutManager = carouselLayoutManager
        carouselProductCardRecyclerView?.itemAnimator = null
        carouselProductCardRecyclerView?.setHasFixedSize(true)
        carouselProductCardRecyclerView?.adapter = carouselAdapter
        carouselProductCardRecyclerView?.setHeightBasedOnProductCardMaxHeight(productCardModelList)

        recyclerViewPool?.let { carouselProductCardRecyclerView?.setRecycledViewPool(it) }

        if (carouselProductCardRecyclerView?.onFlingListener == null) {
            snapHelper.attachToRecyclerView(carouselProductCardRecyclerView)
        }
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
            productCardModelList: List<ProductCardModel>
    ) {
        val productCardWidth = context.resources.getDimensionPixelSize(R.dimen.carousel_product_card_width)
        val productCardHeight = productCardModelList.getMaxHeightForGridView(context, Dispatchers.Default, productCardWidth)

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private fun submitList(
            productCardModelList: List<ProductCardModel>,
            carouselProductCardListenerInfo: CarouselProductCardListenerInfo
    ) {
        carouselAdapter.submitList(productCardModelList.map {
            CarouselProductCardModel(
                    productCardModel = it,
                    carouselProductCardListenerInfo = carouselProductCardListenerInfo
            )
        })
    }

    private fun scrollCarousel(scrollToPosition: Int) {
        post {
            carouselLayoutManager.scrollToPositionWithOffset(scrollToPosition)
        }
    }

    private fun RecyclerView.LayoutManager?.scrollToPositionWithOffset(scrollToPosition: Int) {
        if (this is LinearLayoutManager) {
            scrollToPositionWithOffset(scrollToPosition, context.applicationContext.resources.getDimensionPixelOffset(R.dimen.dp_16))
        }
    }

    fun recycle() {
        cancelJobs()
        carouselAdapter.submitList(null)
    }

    private fun cancelJobs() {
        if (isActive && !masterJob.isCancelled){
            masterJob.children.map { it.cancel() }
        }
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
    @Deprecated("use bindCarouselProductCardViewGrid")
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
            carouselProductCardOnWishlistItemClickListener: CarouselProductCardListener.OnWishlistItemClickListener? = null,
            recyclerViewPool: RecyclerView.RecycledViewPool? = null,
            viewHolderPosition: Int = 0,
            carouselCardSavedStatePosition: SparseIntArray? = null) {

        if (productCardModelList.isEmpty()) return

        val carouselProductCardListenerInfo = CarouselProductCardListenerInfo().also {
            it.onItemClickListener = carouselProductCardOnItemClickListener
            it.onItemLongClickListener = carouselProductCardOnItemLongClickListener
            it.onItemImpressedListener = carouselProductCardOnItemImpressedListener
            it.onItemAddToCartListener = carouselProductCardOnItemAddToCartListener
            it.onWishlistItemClickListener = carouselProductCardOnWishlistItemClickListener
        }


        carouselLayoutManager = createProductCardCarouselLayoutManager()
        carouselCardSavedStatePosition?.let { sparseIntArray ->
            carouselLayoutManager.run {
                if (this is LinearLayoutManager) {
                    val position = sparseIntArray.get(viewHolderPosition, 0)
                    scrollToPositionWithOffset(position,
                            context.applicationContext.resources.getDimensionPixelOffset(
                                    R.dimen.dp_16
                            ))
                }
            }
        }

        val carouselAdapter = CarouselProductCardAdapter()
        setupCarouselProductCardRecyclerView(carouselAdapter)

        recyclerViewPool?.let { carouselProductCardRecyclerView?.setRecycledViewPool(it) }

        submitProductCardCarouselData(carouselAdapter, productCardModelList, carouselProductCardListenerInfo, computeBlankSpaceConfig(productCardModelList))
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
            if (it.discountPercentage.isNotEmpty()) blankSpaceConfig.discountPercentage = true
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

    fun getCurrentPosition(): Int {
        carouselLayoutManager?.let {
            if (it is LinearLayoutManager) {
                return it.findFirstCompletelyVisibleItemPosition()
            }
        }
        return 0
    }

    /**
     * Use this function to update wishlist
     */
    fun updateWishlist(position: Int, isWishlist: Boolean) {
        (carouselProductCardRecyclerView.adapter as CarouselProductCardAdapter).updateWishlist(position, isWishlist)
    }
}