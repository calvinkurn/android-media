package com.tokopedia.shop.flashsale.common.extension

import android.graphics.drawable.InsetDrawable
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.recyclerview.ItemDividerDecoration

fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference(): Int = snapMode
        override fun getHorizontalSnapPreference(): Int = snapMode
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun RecyclerView.attachDividerItemDecoration() {
    val dividerDrawable = MethodChecker.getDrawable(context ?: return, R.drawable.sfs_shape_item_divider)

    val drawableInset = InsetDrawable(
        dividerDrawable,
        0,
        0,
        0,
        0
    )

    val dividerItemDecoration = ItemDividerDecoration(drawableInset)
    addItemDecoration(dividerItemDecoration)
}