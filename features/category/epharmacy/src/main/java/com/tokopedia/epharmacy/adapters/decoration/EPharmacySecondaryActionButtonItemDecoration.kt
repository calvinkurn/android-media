package com.tokopedia.epharmacy.adapters.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.epharmacy.R as epharmacyR

class EPharmacySecondaryActionButtonItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val divider = MethodChecker.getDrawable(context, epharmacyR.drawable.epharmacy_secondary_action_button_divider)
    private val margin = context.resources.getDimension(unifycomponentsR.dimen.layout_lvl2).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val isLastItem: Boolean = parent.getChildAdapterPosition(view) == parent.adapter?.itemCount.orZero() - 1
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        layoutParams.topMargin = margin
        if (!isLastItem) {
            layoutParams.bottomMargin = margin
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
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
