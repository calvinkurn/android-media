package com.tokopedia.home_component.widget.balance

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R as home_componentR

/**
 * Created by frenzel
 */
class BalanceSpacingItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val margin = view.context.resources.getDimensionPixelSize(home_componentR.dimen.balance_outer_margin)
        outRect.right = margin
        outRect.left = margin
    }
}
