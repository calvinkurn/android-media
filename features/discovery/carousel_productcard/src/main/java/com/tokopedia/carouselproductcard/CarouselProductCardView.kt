package com.tokopedia.carouselproductcard

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnATCNonVariantClickListener
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnAddVariantClickListener
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnItemAddToCartListener
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnItemClickListener
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnItemImpressedListener
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnItemThreeDotsClickListener
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnSeeMoreClickListener
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnSeeOtherProductClickListener
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnViewAllCardClickListener
import com.tokopedia.carouselproductcard.helper.StartSnapHelper
import com.tokopedia.productcard.ProductCardLifecycleObserver
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.productcard.utils.getMaxHeightForListView
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CarouselProductCardView : BaseCustomView, CoroutineScope, CarouselProductCardInternalListener {

    private var carouselLayoutManager: RecyclerView.LayoutManager? = null
    private val defaultRecyclerViewDecorator = CarouselProductCardDefaultDecorator()
    private var carouselProductCardAdapter: CarouselProductCardAdapter? = null
    private val snapHelper = StartSnapHelper()
    private var isUseDefaultItemDecorator = true
    private val masterJob = SupervisorJob()
    override var productCardLifecycleObserver: ProductCardLifecycleObserver? = null

    override val coroutineContext = masterJob + Dispatchers.Main
    private val carouselProductCardRecyclerView: RecyclerView by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(R.id.carouselProductCardRecyclerView)
    }

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

        addItemDecorator()
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

    private fun addItemDecorator(customItemDecoration: ItemDecoration? = null) {
        if (carouselProductCardRecyclerView.itemDecorationCount > 0)
            carouselProductCardRecyclerView.removeItemDecorationAt(0)
        if(customItemDecoration != null) {
            carouselProductCardRecyclerView.addItemDecoration(customItemDecoration)
        } else if (isUseDefaultItemDecorator) {
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
        carouselProductCardOnItemATCNonVariantClickListener: OnATCNonVariantClickListener? = null,
        carouselProductCardOnItemAddVariantClickListener: OnAddVariantClickListener? = null,
        carouselProductCardOnItemSeeOtherProductClickListener: OnSeeOtherProductClickListener? = null,
        carouselSeeMoreClickListener: OnSeeMoreClickListener? = null,
        finishCalculate: (() -> Unit)? = null,
        carouselViewAllCardClickListener: OnViewAllCardClickListener? = null,
        carouselViewAllCardData: CarouselViewAllCardData? = null,
        customItemDecoration: ItemDecoration? = null,
    ) {
        if (productCardModelList.isEmpty()) return

        initBindCarousel(true, recyclerViewPool, customItemDecoration)

        val carouselProductCardListenerInfo = createCarouselProductCardListenerInfo(
            carouselProductCardOnItemClickListener,
            carouselProductCardOnItemImpressedListener,
            carouselProductCardOnItemAddToCartListener,
            carouselProductCardOnItemThreeDotsClickListener,
            carouselProductCardOnItemATCNonVariantClickListener,
            carouselProductCardOnItemAddVariantClickListener,
            carouselProductCardOnItemSeeOtherProductClickListener,
            carouselSeeMoreClickListener,
            carouselViewAllCardClickListener,
        )

        launch {
            try {
                tryBindCarousel(
                    productCardModelList,
                    carouselProductCardListenerInfo,
                    showSeeMoreCard, scrollToPosition,
                    true,
                    finishCalculate,
                    carouselViewAllCardData
                )
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private fun initBindCarousel(
        isGrid: Boolean,
        recyclerViewPool: RecyclerView.RecycledViewPool?,
        itemDecoration: ItemDecoration?
    ) {
        initLayoutManager()

        if (isGrid) initGridAdapter()
        else initListAdapter()
        initRecyclerView(recyclerViewPool, itemDecoration)
    }

    private fun createCarouselProductCardListenerInfo(
            carouselProductCardOnItemClickListener: OnItemClickListener? = null,
            carouselProductCardOnItemImpressedListener: OnItemImpressedListener? = null,
            carouselProductCardOnItemAddToCartListener: OnItemAddToCartListener? = null,
            carouselProductCardOnItemThreeDotsClickListener: OnItemThreeDotsClickListener? = null,
            carouselProductCardATCNonVariantClickListener: OnATCNonVariantClickListener? = null,
            carouselProductCardAddVariantClickListener: OnAddVariantClickListener? = null,
            carouselProductCardOnItemSeeOtherProductClickListener: OnSeeOtherProductClickListener? = null,
            carouselSeeMoreClickListener: OnSeeMoreClickListener? = null,
            carouselViewAllCardClickListener: OnViewAllCardClickListener? = null,
    ): CarouselProductCardListenerInfo {

        val carouselProductCardListenerInfo = CarouselProductCardListenerInfo()

        carouselProductCardListenerInfo.onItemClickListener = carouselProductCardOnItemClickListener
        carouselProductCardListenerInfo.onItemImpressedListener = carouselProductCardOnItemImpressedListener
        carouselProductCardListenerInfo.onItemAddToCartListener = carouselProductCardOnItemAddToCartListener
        carouselProductCardListenerInfo.onItemThreeDotsClickListener = carouselProductCardOnItemThreeDotsClickListener
        carouselProductCardListenerInfo.onSeeMoreClickListener = carouselSeeMoreClickListener
        carouselProductCardListenerInfo.onATCNonVariantClickListener = carouselProductCardATCNonVariantClickListener
        carouselProductCardListenerInfo.onAddVariantClickListener = carouselProductCardAddVariantClickListener
        carouselProductCardListenerInfo.onSeeOtherProductClickListener = carouselProductCardOnItemSeeOtherProductClickListener
        carouselProductCardListenerInfo.onViewAllCardClickListener = carouselViewAllCardClickListener

        return carouselProductCardListenerInfo
    }

    private fun initLayoutManager() {
        carouselLayoutManager = createProductCardCarouselLayoutManager()
    }

    private fun initGridAdapter() {
        carouselProductCardAdapter = CarouselProductCardGridAdapter(this)
    }

    private fun initListAdapter() {
        carouselProductCardAdapter = CarouselProductCardListAdapter(this)
    }

    private suspend fun tryBindCarousel(
            productCardModelList: List<ProductCardModel>,
            carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
            showSeeMoreCard: Boolean = false,
            scrollToPosition: Int = 0,
            isGrid: Boolean,
            finishCalculate: (() -> Unit)? = null,
            carouselViewAllCardData: CarouselViewAllCardData? = null,
    ) {
        carouselProductCardRecyclerView?.setHeightBasedOnProductCardMaxHeight(productCardModelList, isGrid)
        submitList(
            productCardModelList,
            showSeeMoreCard,
            carouselProductCardListenerInfo,
            carouselViewAllCardData,
        )
        scrollCarousel(scrollToPosition)
        finishCalculate?.invoke()
    }

    private fun createProductCardCarouselLayoutManager(): RecyclerView.LayoutManager {
        return object: LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
            override fun requestChildRectangleOnScreen(
                    parent: RecyclerView,
                    child: View,
                    rect: Rect,
                    immediate: Boolean,
                    focusedChildVisible: Boolean
            ): Boolean {
                return if ((child as? ViewGroup)?.focusedChild is CardView) {
                    false
                } else super.requestChildRectangleOnScreen(
                        parent,
                        child,
                        rect,
                        immediate,
                        focusedChildVisible
                )
            }
        }
    }

    private fun initRecyclerView(recyclerViewPool: RecyclerView.RecycledViewPool?, itemDecoration: ItemDecoration?) {
        carouselProductCardRecyclerView.layoutManager = carouselLayoutManager
        carouselProductCardRecyclerView.itemAnimator = null
        carouselProductCardRecyclerView.setHasFixedSize(true)
        carouselProductCardRecyclerView.adapter = carouselProductCardAdapter?.asRecyclerViewAdapter()
        addItemDecorator(itemDecoration)

        recyclerViewPool?.let { carouselProductCardRecyclerView.setRecycledViewPool(it) }

        if (carouselProductCardRecyclerView.onFlingListener == null) {
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

    private fun submitList(
            productCardModelList: List<ProductCardModel>,
            showSeeMoreCard: Boolean = false,
            carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
            carouselViewAllCardData: CarouselViewAllCardData? = null,
    ) {
        val carouselList = productCardModelList.map {
            CarouselProductCardModel(
                    productCardModel = it,
                    carouselProductCardListenerInfo = carouselProductCardListenerInfo,
            )
        }.toMutableList<BaseCarouselCardModel>()

        if (showSeeMoreCard)
            carouselList.add(CarouselSeeMoreCardModel(carouselProductCardListenerInfo))

        carouselViewAllCardData?.let {
            carouselList.add(
                CarouselViewAllCardModel(it, carouselProductCardListenerInfo)
            )
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
            scrollToPositionWithOffset(scrollToPosition, context.applicationContext.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_16))
        }
    }

    fun bindCarouselProductCardViewList(
            productCardModelList: List<ProductCardModel>,
            carouselProductCardOnItemClickListener: OnItemClickListener? = null,
            carouselProductCardOnItemImpressedListener: OnItemImpressedListener? = null,
            carouselProductCardOnItemAddToCartListener: OnItemAddToCartListener? = null,
            carouselProductCardOnItemThreeDotsClickListener: OnItemThreeDotsClickListener? = null,
            carouselProductCardOnItemATCNonVariantClickListener: OnATCNonVariantClickListener? = null,
            carouselProductCardOnItemAddVariantClickListener: OnAddVariantClickListener? = null,
            carouselProductCardOnItemSeeOtherProductClickListener: OnSeeOtherProductClickListener? = null,
            carouselSeeMoreClickListener: OnSeeMoreClickListener? = null,
            recyclerViewPool: RecyclerView.RecycledViewPool? = null,
            showSeeMoreCard: Boolean = false,
            scrollToPosition: Int = 0,
            customItemDecoration: ItemDecoration? = null,
    ) {
        if (productCardModelList.isEmpty()) return

        initBindCarousel(false, recyclerViewPool, customItemDecoration)

        val carouselProductCardListenerInfo = createCarouselProductCardListenerInfo(
                carouselProductCardOnItemClickListener,
                carouselProductCardOnItemImpressedListener,
                carouselProductCardOnItemAddToCartListener,
                carouselProductCardOnItemThreeDotsClickListener,
                carouselProductCardOnItemATCNonVariantClickListener,
                carouselProductCardOnItemAddVariantClickListener,
                carouselProductCardOnItemSeeOtherProductClickListener,
                carouselSeeMoreClickListener,
        )

        launch {
            try {
                tryBindCarousel(productCardModelList, carouselProductCardListenerInfo, showSeeMoreCard, scrollToPosition, false)
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
