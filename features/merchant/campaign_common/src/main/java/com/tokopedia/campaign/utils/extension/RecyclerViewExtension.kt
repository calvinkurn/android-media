package com.tokopedia.campaign.utils.extension

import android.graphics.drawable.InsetDrawable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.R
import com.tokopedia.campaign.utils.decorator.ItemDividerDecoration
import com.tokopedia.unifycomponents.toPx

private const val NO_INSET = 0

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