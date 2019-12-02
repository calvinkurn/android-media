package com.tokopedia.similarsearch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.v2.ProductCardView

internal class OriginalProductLayoutBehavior: CoordinatorLayout.Behavior<ProductCardView> {

    constructor()

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    override fun layoutDependsOn(parent: CoordinatorLayout, child: ProductCardView, dependency: View): Boolean {
        return dependency is RecyclerView
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ProductCardView, dependency: View): Boolean {
        Log.v("Behavior", "Dependency Y: ${dependency.y}")
        Log.v("Behavior", "Dependency Height: ${dependency.height}")
        Log.v("Behavior", "Child Height: ${child.height}")
        Log.v("Behavior", "Child X: ${child.x}")
        Log.v("Behavior", "Child Y: ${child.y}")
        return false
    }
}