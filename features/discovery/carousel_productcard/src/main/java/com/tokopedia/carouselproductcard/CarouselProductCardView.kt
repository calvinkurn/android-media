package com.tokopedia.carouselproductcard

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import kotlinx.android.synthetic.main.carousel_product_card_layout.view.*
import android.view.ViewGroup
import android.app.Activity
import android.graphics.Point
import android.support.v7.widget.LinearLayoutManager
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

    /**
     * @param activity is used to determine device width.
     * @param parentView is used to measure view according to its parent.
     * @param productCardModelList collection of product model to define max height.
     * @param isScrollable differentiate between carousel and non carousel recyclerview.
     */
    fun initCarouselProductCardView(
            activity: Activity?,
            parentView: View,
            productCardModelList: List<ProductCardModel>,
            isScrollable: Boolean = true,
            carouselProductCardOnItemClickListener: CarouselProductCardListener.OnItemClickListener? = null,
            carouselProductCardOnItemLongClickListener: CarouselProductCardListener.OnItemLongClickListener? = null,
            carouselProductCardOnItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener? = null,
            carouselProductCardOnItemAddToCartListener: CarouselProductCardListener.OnItemAddToCartListener? = null,
            carouselProductCardOnWishlistItemClickListener: CarouselProductCardListener.OnWishlistItemClickListener? = null) {

        val carouselProductCardListenerInfo = CarouselProductCardListenerInfo().also {
            it.onItemClickListener = carouselProductCardOnItemClickListener
            it.onItemLongClickListener = carouselProductCardOnItemLongClickListener
            it.onItemImpressedListener = carouselProductCardOnItemImpressedListener
            it.onItemAddToCartListener = carouselProductCardOnItemAddToCartListener
            it.onWishlistItemClickListener = carouselProductCardOnWishlistItemClickListener
        }

        activity?.run {
            measureParentView(activity, parentView)
        }
        carouselProductCardRecyclerView?.layoutManager = createProductcardCarouselLayoutManager(isScrollable, productCardModelList.size)
        carouselProductCardRecyclerView?.adapter = CarouselProductCardAdapter(
                productCardModelList, isScrollable, carouselProductCardListenerInfo, getMaxProductCardContentHeight(productCardModelList))
    }

    private fun measureParentView(activity: Activity, parentView: View) {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        parentView.measure(
                MeasureSpec.makeMeasureSpec(size.x, MeasureSpec.EXACTLY),
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