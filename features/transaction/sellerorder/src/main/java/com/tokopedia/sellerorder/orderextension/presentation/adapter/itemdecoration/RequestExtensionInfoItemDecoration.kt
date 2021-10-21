package com.tokopedia.sellerorder.orderextension.presentation.adapter.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class RequestExtensionInfoItemDecoration : RecyclerView.ItemDecoration() {

    private val margin = 8.toPx()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        view.setupVerticalMargins()
    }

    private fun View.setupVerticalMargins() {
        val layoutParams = layoutParams as RecyclerView.LayoutParams
        layoutParams.topMargin = margin
        layoutParams.bottomMargin = margin
    }
}
