package com.tokopedia.cart.view.compoundview

import android.content.Context
import android.graphics.Region
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.view.customview.CartSwipeRevealLayout
import com.tokopedia.cart.view.customview.CartViewBinderHelper
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.toIntSafely

class CartRecyclerView : RecyclerView {

    companion object {
        private const val SWIPE_LAYOUT_SUB_VIEW_WIDTH = 80
    }

    private var viewBinderHelper: CartViewBinderHelper = CartViewBinderHelper()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setViewBinderHelper(viewBinderHelper: CartViewBinderHelper) {
        this.viewBinderHelper = viewBinderHelper
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (shouldClearFocus() && this.focusedChild != null) {
            clearFocus()
        }

        if (shouldInterceptTouchEvent()) {
            val layouts = viewBinderHelper.getOpenedLayout()
            for (layout in layouts) {
                val region = if (isBundlingSwipeLayout(layout)) {
                    val outerSwipeRevealLayout = getOuterSwipeRevealLayout(layout)
                    val parent = getSwipeRevealLayoutParent(outerSwipeRevealLayout)
                    val start = parent.right - SWIPE_LAYOUT_SUB_VIEW_WIDTH
                        .dpToPx(layout.context.resources.displayMetrics)
                    val end = parent.right
                    val top = parent.top
                    val newSwipeRevealLayoutBottom = top + layout.bottom
                    Region(start, top, end, newSwipeRevealLayoutBottom)
                } else {
                    val parent = getSwipeRevealLayoutParent(layout)
                    val start = parent.right - SWIPE_LAYOUT_SUB_VIEW_WIDTH
                        .dpToPx(layout.context.resources.displayMetrics)
                    val end = parent.right
                    val top = parent.top
                    val bottom = parent.bottom
                    Region(start, top, end, bottom)
                }

                val inRegion = region.contains(e?.x.toIntSafely(), e?.y.toIntSafely())
                if (inRegion) {
                    return super.onInterceptTouchEvent(e)
                }
            }
            viewBinderHelper.closeAll()
            return true
        }
        return super.onInterceptTouchEvent(e)
    }

    private fun shouldClearFocus(): Boolean {
        return true
    }

    private fun shouldInterceptTouchEvent(): Boolean {
        return viewBinderHelper.openCount > Int.ZERO
    }

    private fun isBundlingSwipeLayout(cartSwipeRevealLayout: CartSwipeRevealLayout): Boolean {
        return cartSwipeRevealLayout.parent is ConstraintLayout
    }

    private fun getOuterSwipeRevealLayout(cartSwipeRevealLayout: CartSwipeRevealLayout): CartSwipeRevealLayout {
        val clContainerProductInformation = cartSwipeRevealLayout.parent
        if (clContainerProductInformation !is ConstraintLayout) return cartSwipeRevealLayout

        val llProductInformation = clContainerProductInformation.parent
        if (llProductInformation !is LinearLayout) return cartSwipeRevealLayout

        val outerSwipeRevealLayout = llProductInformation.parent
        if (outerSwipeRevealLayout !is CartSwipeRevealLayout) return cartSwipeRevealLayout

        return outerSwipeRevealLayout
    }

    private fun getSwipeRevealLayoutParent(cartSwipeRevealLayout: CartSwipeRevealLayout): LinearLayout {
        return cartSwipeRevealLayout.parent as LinearLayout
    }
}
