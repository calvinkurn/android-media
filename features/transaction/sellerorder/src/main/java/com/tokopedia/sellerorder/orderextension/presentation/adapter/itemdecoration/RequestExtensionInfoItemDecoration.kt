package com.tokopedia.sellerorder.orderextension.presentation.adapter.itemdecoration

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elyeproj.loaderviewlibrary.LoaderTextView
import com.tokopedia.unifycomponents.toPx

class RequestExtensionInfoItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        private const val VERTICAL_MARGIN_BETWEEN_VIEW = 8f
        private const val VERTICAL_MARGIN_BETWEEN_SHIMMER = 4.7f
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val margin = if (view is LoaderTextView) VERTICAL_MARGIN_BETWEEN_SHIMMER else VERTICAL_MARGIN_BETWEEN_VIEW
        view.setupVerticalMargins(margin.toPx())
    }

    private fun View.setupVerticalMargins(margin: Int) {
        val layoutParams = layoutParams as RecyclerView.LayoutParams
        layoutParams.topMargin = margin
        layoutParams.bottomMargin = margin
    }

    private fun Float.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}
