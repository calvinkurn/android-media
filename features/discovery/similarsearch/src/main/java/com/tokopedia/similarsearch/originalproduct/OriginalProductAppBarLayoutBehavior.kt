package com.tokopedia.similarsearch.originalproduct

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ScrollingView
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.similarsearch.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

internal class OriginalProductAppBarLayoutBehavior: CoordinatorLayout.Behavior<AppBarLayout> {

    companion object {
        private const val VERTICAL_SCROLL_OFFSET_THRESHOLD = 800
    }

    private var context: Context? = null

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        this.context = context
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, directTargetChild: View, target: View, axes: Int, type: Int): Boolean =
            axes == ViewCompat.SCROLL_AXIS_VERTICAL
                    && target is ScrollingView

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)

        if (target !is ScrollingView) return

        if (target.computeVerticalScrollOffset() > VERTICAL_SCROLL_OFFSET_THRESHOLD) {
            child.collapse()
        }
        else {
            child.expand()
        }
    }

    private fun AppBarLayout.collapse() {
        findViewById<AppCompatImageView>(R.id.imageProduct)?.scaleWidthHeight(com.tokopedia.design.R.dimen.dp_80)
        findViewById<Typography>(R.id.textViewProductName)?.maxLines = 1
        findViewById<UnifyButton>(R.id.buttonAddToCart)?.gone()
        findViewById<UnifyButton>(R.id.buttonBuy)?.gone()
        findViewById<UnifyButton>(R.id.buttonAddToCartCollapsed)?.visible()
    }

    private fun AppCompatImageView.scaleWidthHeight(@DimenRes id: Int) {
        val currentWidth = layoutParams.width
        val currentHeight = layoutParams.height

        layoutParams.width = getDimensionPixelSize(id, currentWidth)
        layoutParams.height = getDimensionPixelSize(id, currentHeight)
    }

    private fun getDimensionPixelSize(@DimenRes id: Int, defaultValue: Int = 0): Int {
        return try { context?.resources?.getDimensionPixelSize(id) ?: defaultValue } catch (throwable: Throwable) { defaultValue }
    }

    private fun AppBarLayout.expand() {
        findViewById<AppCompatImageView>(R.id.imageProduct)?.scaleWidthHeight(com.tokopedia.design.R.dimen.dp_96)
        findViewById<Typography>(R.id.textViewProductName)?.maxLines = 2
        findViewById<UnifyButton>(R.id.buttonAddToCart)?.visible()
        findViewById<UnifyButton>(R.id.buttonBuy)?.visible()
        findViewById<UnifyButton>(R.id.buttonAddToCartCollapsed)?.gone()
    }
}