package com.tokopedia.statistic.view.viewhelper

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel

/**
 * Created By @ilhamsuaib on 08/09/21
 */

class StatisticItemDecoration : RecyclerView.ItemDecoration() {

    companion object {
        private const val LAST_ONE = 1
        private const val BOTTOM_MARGIN_IN_DP = 12
        private const val LAST_TWO_WIDGETS = 2
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter as? BaseListAdapter<*, *>
        val lastIndex = parent.adapter?.itemCount.orZero().minus(LAST_ONE)
        val isLastItem: Boolean = parent.getChildAdapterPosition(view) == lastIndex
        val widgets = adapter?.data
        if (!widgets.isNullOrEmpty()) {
            try {
                val last2widget = widgets.takeLast(LAST_TWO_WIDGETS)
                if (last2widget.all { it is CardWidgetUiModel }) {
                    val lasTwoItemIndex = parent.adapter?.itemCount.orZero().minus(LAST_TWO_WIDGETS)
                    val isLastTwoItem: Boolean =
                        parent.getChildAdapterPosition(view) == lasTwoItemIndex
                    if (isLastTwoItem) {
                        setBottomMargin(view.context, outRect)
                    }
                }
                if (isLastItem) {
                    setBottomMargin(view.context, outRect)
                }
            } catch (e: IndexOutOfBoundsException) {
                if (isLastItem) {
                    setBottomMargin(view.context, outRect)
                }
            }
        }
    }

    private fun setBottomMargin(context: Context, outRect: Rect) {
        outRect.bottom = context.dpToPx(BOTTOM_MARGIN_IN_DP).toInt()
    }
}