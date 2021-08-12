package com.tokopedia.buyerorderdetail.presentation.adapter.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.kotlin.extensions.view.orZero

class ProductBundlingItemDecoration(context: Context): RecyclerView.ItemDecoration() {

    private val divider = MethodChecker.getDrawable(context, R.drawable.bg_buyer_order_detail_bundling_dashed_divider)
    private val padding = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.layout_lvl2).toInt()

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = padding
        val right = parent.width - padding
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

}