package com.tokopedia.topads.sdk.decoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topads.sdk.R

class TdnCarouselItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == Int.ZERO) {
            outRect.left = view.context.resources.getDimensionPixelSize(R.dimen.sdk_dp_16)
        }

        outRect.right = 8f.toPx().toInt()
    }
}
