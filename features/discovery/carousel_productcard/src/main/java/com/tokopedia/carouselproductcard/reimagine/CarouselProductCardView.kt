package com.tokopedia.carouselproductcard.reimagine

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.R
import com.tokopedia.carouselproductcard.helper.StartSnapHelper
import com.tokopedia.kotlin.util.lazyThreadSafetyNone

class CarouselProductCardView: FrameLayout {

    private val recyclerView: RecyclerView? by lazyThreadSafetyNone {
        findViewById(R.id.carouselProductCardReimagineRecyclerView)
    }

    private val adapter: Adapter by lazyThreadSafetyNone {
        Adapter(CarouselProductCardTypeFactoryImpl())
    }

    private val layoutManager: LinearLayoutManager by lazyThreadSafetyNone {
        object: LinearLayoutManager(context, HORIZONTAL, false) {
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

    private val snapHelper: StartSnapHelper by lazyThreadSafetyNone { StartSnapHelper() }

    constructor(context: Context): super(context) { init() }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) { init(attrs) }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        View.inflate(context, R.layout.carousel_product_card_reimagine_layout, this)

        recyclerView?.run {
            layoutManager = this@CarouselProductCardView.layoutManager
            adapter = this@CarouselProductCardView.adapter

            itemAnimator = null

            setHasFixedSize(true)

            snapHelper.attachToRecyclerView(this)

            addItemDecorator()
        }
    }

    private fun RecyclerView.addItemDecorator() {
        if (itemDecorationCount > 0) removeItemDecorationAt(0)

        addItemDecoration(CarouselProductCardDefaultDecoration())
    }

    fun bind(carouselProductCardModel: CarouselProductCardModel) {
        recyclerView?.setRecycledViewPool(carouselProductCardModel.recycledViewPool)

        adapter.submitList(carouselProductCardModel.itemList)
    }

    fun recycle() {

    }
}
