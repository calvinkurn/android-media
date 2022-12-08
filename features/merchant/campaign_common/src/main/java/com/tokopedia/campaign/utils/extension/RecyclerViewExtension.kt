package com.tokopedia.campaign.utils.extension

import android.graphics.Rect
import android.graphics.drawable.InsetDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.R
import com.tokopedia.campaign.utils.decorator.ItemDividerDecoration
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.toPx

private const val NO_INSET = 0
private const val PADDING_BOTTOM_IN_DP = 16

fun RecyclerView.attachDividerItemDecoration(
    drawableResourceId: Int = R.drawable.shape_item_divider,
    insetLeft: Int = NO_INSET,
    insetTop: Int = NO_INSET,
    insetRight: Int = NO_INSET,
    insetBottom: Int = NO_INSET
) {
    val dividerDrawable = MethodChecker.getDrawable(context ?: return, drawableResourceId)

    val drawableInset = InsetDrawable(
        dividerDrawable,
        insetLeft.toPx(),
        insetTop.toPx(),
        insetRight.toPx(),
        insetBottom.toPx()
    )

    val dividerItemDecoration = ItemDividerDecoration(drawableInset)
    addItemDecoration(dividerItemDecoration)
}

fun RecyclerView.applyPaddingToLastItem(paddingBottomInDp: Int = PADDING_BOTTOM_IN_DP) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildLayoutPosition(view)
            val totalItemCount = state.itemCount
            if (totalItemCount > Int.ZERO && position == totalItemCount - Int.ONE) {
                outRect.bottom = paddingBottomInDp.toPx()
            }
        }
    })
}
