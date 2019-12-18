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

        val knownAdapter = carouselProductPool.carouselAdapters[carouselModelId]
        val knownPosition = carouselProductPool.carouselAdaptersPositions[carouselModelId]

        if (knownAdapter == null && productCardModelList.isNotEmpty()) {
            measureThisParentView(parentView, deviceWidth, activity)
            carouselLayoutManager = createProductcardCarouselLayoutManager(isScrollable, productCardModelList.size)
            val newCarouselAdapter = createProductCardCarouselAdapter(isScrollable, productCardModelList, carouselModelId)
            setupCarouselProductCardRecyclerView(newCarouselAdapter)
            submitProductCardCarouselData(newCarouselAdapter, productCardModelList, carouselProductCardListenerInfo)
        } else {
            carouselProductCardRecyclerView.swapAdapter(knownAdapter, false)
            knownAdapter?.notifyDataSetChanged()
            knownAdapter?.let {
                submitProductCardCarouselData(knownAdapter, productCardModelList, carouselProductCardListenerInfo)
            }
        }

        carouselLayoutManager?.run {
            if (this is LinearLayoutManager) {
                scrollToPositionWithOffset(knownPosition?:0,
                        context.applicationContext.resources.getDimensionPixelOffset(
                                R.dimen.dp_16
                        ))
            }
        }
    }

    private fun submitProductCardCarouselData(newCarouselAdapter: CarouselProductCardAdapter, productCardModelList: List<ProductCardModel>, carouselProductCardListenerInfo: CarouselProductCardListenerInfo) {
        newCarouselAdapter.submitList(productCardModelList.map {
            CarouselProductCardModel(it, carouselProductCardListenerInfo)
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

    private fun createProductCardCarouselAdapter(isScrollable: Boolean, productCardModelList: List<ProductCardModel>, carouselModelId: String?): CarouselProductCardAdapter {
        val newCarouselAdapter = CarouselProductCardAdapter(getMaxProductCardContentHeight(productCardModelList))

        carouselModelId?.let { id ->
            carouselProductPool.carouselAdapters.put(id, newCarouselAdapter)
        }
        return newCarouselAdapter
    }

    private fun measureThisParentView(parentView: View?, deviceWidth: Int, activity: Activity?) {
        parentView?.run {
            if (deviceWidth > 0) {
                measureParentView(deviceWidth, this)
            } else if (activity != null) {
                val display = activity.windowManager.defaultDisplay
                val size = Point()
                display.getSize(size)
                measureParentView(size.x, this)
            }
        }
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

    private fun measureParentView(maxWidth: Int, parentView: View) {
        parentView.measure(
                MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY),
                MeasureSpec.UNSPECIFIED
        )
    }

    private fun getMaxProductCardContentHeight(productCardModelList: List<ProductCardModel>) : Int {
        var contentHeight = 0
        carouselProductCardRecyclerView?.let {
            /**
             * Inflate product card for measuring purpose
             */
            val productCardViewHolder = LayoutInflater
                    .from(it.context)
                    .inflate(CarouselProductCardViewHolder.LAYOUT, it.parent as ViewGroup, false)
            val targetLayoutManager = it.layoutManager
            val eachSpanSize = if(targetLayoutManager is GridLayoutManager)
                getSizeForEachSpan(measuredWidth, (targetLayoutManager as GridLayoutManager).spanCount) else
                it.context.resources.getDimensionPixelOffset(R.dimen.product_card_size)

            /**
             * Compare product card in list
             */
            productCardModelList.forEach {
                val sampleProductCard = productCardViewHolder.findViewById<ProductCardViewSmallGrid>(R.id.carouselProductCardItem)
                sampleProductCard.setProductModel(
                        it
                )
                /**
                 * Measure product card after setProductModel
                 * to ensure product card have final height
                 *
                 * MeasureSpec.EXACTLY is used to ensure this product
                 * card have width EXACTLY as given eachSpanSize
                 */
                productCardViewHolder.measure(
                        MeasureSpec.makeMeasureSpec(eachSpanSize, View.MeasureSpec.EXACTLY),
                        MeasureSpec.UNSPECIFIED)
                val measuredContentHeight = sampleProductCard?.measuredHeight?:0
                if (contentHeight < measuredContentHeight) {
                    contentHeight = measuredContentHeight
                }

            }
        }
        return contentHeight
    }

    fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration){
        if(carouselProductCardRecyclerView?.itemDecorationCount == 0) carouselProductCardRecyclerView?.addItemDecoration(itemDecoration)
    }

    fun setSnapHelper(snapHelper: SnapHelper){
        snapHelper.attachToRecyclerView(carouselProductCardRecyclerView)
    }

    private fun getSizeForEachSpan(maxWidth: Int, spanCount: Int) : Int {
        return maxWidth/spanCount
    }

    private fun createProductcardCarouselLayoutManager(isScrollable: Boolean, productCardModelListSize: Int): RecyclerView.LayoutManager {
        return if (isScrollable) {
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            GridLayoutManager(context, productCardModelListSize, GridLayoutManager.VERTICAL, false)
        }
    }
}