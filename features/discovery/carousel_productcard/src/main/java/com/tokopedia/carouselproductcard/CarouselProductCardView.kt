package com.tokopedia.carouselproductcard

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.CarouselProductCardListener.*
import com.tokopedia.carouselproductcard.helper.StartSnapHelper
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.productcard.utils.getMaxHeightForListView
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.carousel_product_card_layout.view.*
import kotlinx.coroutines.*

class CarouselProductCardView : BaseCustomView, CoroutineScope {

    private var carouselLayoutManager: RecyclerView.LayoutManager? = null
    private val defaultRecyclerViewDecorator = CarouselProductCardDefaultDecorator()
    private var carouselProductCardAdapter: CarouselProductCardAdapter? = null
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
            recyclerViewPool: RecyclerView.RecycledViewPool? = null,
            scrollToPosition: Int = 0,
            showSeeMoreCard: Boolean = false,
            carouselProductCardOnItemClickListener: OnItemClickListener? = null,
            carouselProductCardOnItemImpressedListener: OnItemImpressedListener? = null,
            carouselProductCardOnItemAddToCartListener: OnItemAddToCartListener? = null,
            carouselProductCardOnItemThreeDotsClickListener: OnItemThreeDotsClickListener? = null,
            carouselSeeMoreClickListener: OnSeeMoreClickListener? = null,
            finishCalculate: (() -> Unit)? = null
    ) {
        if (productCardModelList.isEmpty()) return

        initBindCarousel(true)

        val carouselProductCardListenerInfo = createCarouselProductCardListenerInfo(
                carouselProductCardOnItemClickListener,
                carouselProductCardOnItemImpressedListener,
                carouselProductCardOnItemAddToCartListener,
                carouselProductCardOnItemThreeDotsClickListener,
                carouselSeeMoreClickListener
        )

        launch {
            try {
                tryBindCarousel(productCardModelList, carouselProductCardListenerInfo, recyclerViewPool, showSeeMoreCard, scrollToPosition, true, finishCalculate)
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private fun initBindCarousel(isGrid: Boolean) {
        initLayoutManager()

        if (isGrid) initGridAdapter()
        else initListAdapter()
    }

    private fun createCarouselProductCardListenerInfo(
            carouselProductCardOnItemClickListener: OnItemClickListener? = null,
            carouselProductCardOnItemImpressedListener: OnItemImpressedListener? = null,
            carouselProductCardOnItemAddToCartListener: OnItemAddToCartListener? = null,
            carouselProductCardOnItemThreeDotsClickListener: OnItemThreeDotsClickListener? = null,
            carouselSeeMoreClickListener: OnSeeMoreClickListener? = null)
    : CarouselProductCardListenerInfo {

        val carouselProductCardListenerInfo = CarouselProductCardListenerInfo()

        carouselProductCardListenerInfo.onItemClickListener = carouselProductCardOnItemClickListener
        carouselProductCardListenerInfo.onItemImpressedListener = carouselProductCardOnItemImpressedListener
        carouselProductCardListenerInfo.onItemAddToCartListener = carouselProductCardOnItemAddToCartListener
        carouselProductCardListenerInfo.onItemThreeDotsClickListener = carouselProductCardOnItemThreeDotsClickListener
        carouselProductCardListenerInfo.onSeeMoreClickListener = carouselSeeMoreClickListener

        return carouselProductCardListenerInfo
    }

    private fun initLayoutManager() {
        carouselLayoutManager = createProductCardCarouselLayoutManager()
    }

    private fun initGridAdapter() {
        carouselProductCardAdapter = CarouselProductCardGridAdapter()
    }

    private fun initListAdapter() {
        carouselProductCardAdapter = CarouselProductCardListAdapter()
    }

    private suspend fun tryBindCarousel(
            productCardModelList: List<ProductCardModel>,
            carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
            recyclerViewPool: RecyclerView.RecycledViewPool? = null,
            showSeeMoreCard: Boolean = false,
            scrollToPosition: Int = 0,
            isGrid: Boolean,
            finishCalculate: (() -> Unit)? = null
    ) {
        initRecyclerView(productCardModelList, recyclerViewPool, isGrid)
        submitList(productCardModelList, showSeeMoreCard, carouselProductCardListenerInfo)
        scrollCarousel(scrollToPosition)
        finishCalculate?.invoke()
    }

    private fun createProductCardCarouselLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private suspend fun initRecyclerView(
            productCardModelList: List<ProductCardModel>,
            recyclerViewPool: RecyclerView.RecycledViewPool?,
            isGrid: Boolean
    ) {
        carouselProductCardRecyclerView?.layoutManager = carouselLayoutManager
        carouselProductCardRecyclerView?.itemAnimator = null
        carouselProductCardRecyclerView?.setHasFixedSize(true)
        carouselProductCardRecyclerView?.adapter = carouselProductCardAdapter?.asRecyclerViewAdapter()
        carouselProductCardRecyclerView?.setHeightBasedOnProductCardMaxHeight(productCardModelList, isGrid)

        recyclerViewPool?.let { carouselProductCardRecyclerView?.setRecycledViewPool(it) }

        if (carouselProductCardRecyclerView?.onFlingListener == null) {
            snapHelper.attachToRecyclerView(carouselProductCardRecyclerView)
        }
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
            productCardModelList: List<ProductCardModel>,
            isGrid: Boolean
    ) {
        val productCardHeight = getProductCardMaxHeight(productCardModelList, isGrid)

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardModel>, isGrid: Boolean): Int {
        return if (isGrid) {
            val productCardWidth = context.resources.getDimensionPixelSize(com.tokopedia.productcard.R.dimen.carousel_product_card_grid_width)
            productCardModelList.getMaxHeightForGridView(context, Dispatchers.Default, productCardWidth)
        }
        else {
            productCardModelList.getMaxHeightForListView(context, Dispatchers.Default)
        }
    }

    private fun submitList(productCardModelList: List<ProductCardModel>,
                           showSeeMoreCard: Boolean = false,
                           carouselProductCardListenerInfo: CarouselProductCardListenerInfo) {
        val carouselList: MutableList<BaseCarouselCardModel> = productCardModelList.map {
            CarouselProductCardModel(
                    productCardModel = it,
                    carouselProductCardListenerInfo = carouselProductCardListenerInfo
            )
        }.toMutableList()
        if(showSeeMoreCard){
            carouselList.add(CarouselSeeMoreCardModel(carouselProductCardListenerInfo
            ))
        }
        carouselProductCardAdapter?.submitCarouselProductCardModelList(carouselList)
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

    fun bindCarouselProductCardViewList(
            productCardModelList: List<ProductCardModel>,
            carouselProductCardOnItemClickListener: OnItemClickListener? = null,
            carouselProductCardOnItemImpressedListener: OnItemImpressedListener? = null,
            carouselProductCardOnItemAddToCartListener: OnItemAddToCartListener? = null,
            carouselProductCardOnItemThreeDotsClickListener: OnItemThreeDotsClickListener? = null,
            carouselSeeMoreClickListener: OnSeeMoreClickListener? = null,
            recyclerViewPool: RecyclerView.RecycledViewPool? = null,
            showSeeMoreCard: Boolean = false,
            scrollToPosition: Int = 0
    ) {
        if (productCardModelList.isEmpty()) return

        initBindCarousel(false)

        val carouselProductCardListenerInfo = createCarouselProductCardListenerInfo(
                carouselProductCardOnItemClickListener,
                carouselProductCardOnItemImpressedListener,
                carouselProductCardOnItemAddToCartListener,
                carouselProductCardOnItemThreeDotsClickListener,
                carouselSeeMoreClickListener
        )

        launch {
            try {
                tryBindCarousel(productCardModelList, carouselProductCardListenerInfo, recyclerViewPool, showSeeMoreCard, scrollToPosition, false)
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    @Deprecated("Not needed anymore.")
    fun setCarouselProductCardListeners(
            carouselProductCardOnItemClickListener: OnItemClickListener? = null,
            carouselProductCardOnItemImpressedListener: OnItemImpressedListener? = null,
            carouselProductCardOnItemAddToCartListener: OnItemAddToCartListener? = null,
            carouselProductCardOnItemThreeDotsClickListener: OnItemThreeDotsClickListener? = null
    ) {
    }

    fun recycle() {
        cancelJobs()
        carouselProductCardAdapter?.submitCarouselProductCardModelList(null)
    }

    private fun cancelJobs() {
        if (isActive && !masterJob.isCancelled) {
            masterJob.children.map { it.cancel() }
        }
    }

    fun getCurrentPosition(): Int {
        carouselLayoutManager?.let {
            if (it is LinearLayoutManager) {
                return it.findFirstCompletelyVisibleItemPosition()
            }
        }
        return 0
    }
}