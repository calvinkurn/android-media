package com.tokopedia.carouselproductcard

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.helper.StartSnapHelper
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
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

    fun getCurrentPosition(): Int {
        carouselLayoutManager?.let {
            if (it is LinearLayoutManager) {
                return it.findFirstCompletelyVisibleItemPosition()
            }
        }
        return 0
    }
}