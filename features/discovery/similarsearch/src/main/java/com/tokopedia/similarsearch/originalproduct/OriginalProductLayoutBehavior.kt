package com.tokopedia.similarsearch.originalproduct

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ScrollingView
import androidx.core.view.ViewCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.similarsearch.R
import com.tokopedia.unifycomponents.UnifyButton

internal class OriginalProductLayoutBehavior: CoordinatorLayout.Behavior<CardView> {

    private var context: Context? = null

    constructor(): super()

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        this.context = context
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: CardView, directTargetChild: View, target: View, axes: Int, type: Int): Boolean =
            axes == ViewCompat.SCROLL_AXIS_VERTICAL
                    && target is ScrollingView

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: CardView, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)

        Log.v("onNestedScroll",
                "dxConsumed: $dxConsumed, " +
                        "dyConsumed: $dyConsumed; " +
                        "dxUnconsumed: $dxUnconsumed, " +
                        "dyUnconsumed: $dyUnconsumed; " +
                        "target compute vertical offset: ${(target as ScrollingView).computeVerticalScrollOffset()}" +
                        "target compute vertical extent: ${(target as ScrollingView).computeVerticalScrollExtent()}" +
                        "target compute vertical range: ${(target as ScrollingView).computeVerticalScrollRange()}")

        if (target.computeVerticalScrollOffset() > 800) {
            child.findViewById<UnifyButton>(R.id.buttonAddToCart).gone()
            child.findViewById<UnifyButton>(R.id.buttonBuy).gone()
        }
        else {
            child.findViewById<UnifyButton>(R.id.buttonAddToCart).visible()
            child.findViewById<UnifyButton>(R.id.buttonBuy).visible()
        }
    }
}