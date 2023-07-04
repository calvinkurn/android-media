package com.tokopedia.inbox.universalinbox.view.adapter.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.inbox.universalinbox.view.adapter.UniversalInboxAdapter
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.toPx

class UniversalInboxRecommendationDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (isRecommendation(view, parent)) {
            val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
            if (spanIndex == Int.ZERO) {
                outRect.left = MARGIN.toPx()
            } else {
                outRect.right = MARGIN.toPx()
            }
        }
    }

    private fun isRecommendation(view: View, parent: RecyclerView): Boolean {
        val adapter = parent.adapter as UniversalInboxAdapter
        val position = parent.getChildAdapterPosition(view)
        val viewType = adapter.getItemViewType(position)
        return adapter.getProductRecommendationViewType() ==  viewType
    }

    companion object {
        private const val MARGIN = 14
    }
}
