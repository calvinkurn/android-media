package com.tokopedia.product.manage.common.view.ongoingpromotion.adapter.divider

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.product.manage.R

class OngoingPromotionDividerDecoration(context: Context): RecyclerView.ItemDecoration() {

    private val divider = ContextCompat.getDrawable(context, R.drawable.divider_product_manage_thin_no_margin)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (index in 0 until parent.childCount) {
            var left = 0
            val right = parent.width
            val top = parent.getChildAt(index).bottom
            val bottom = top + divider?.intrinsicHeight.toZeroIfNull()
            if (index == 0) {
                left = 0
            }
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }

}