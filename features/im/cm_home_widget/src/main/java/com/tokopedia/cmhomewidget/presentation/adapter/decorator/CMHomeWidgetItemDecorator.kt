package com.tokopedia.cmhomewidget.presentation.adapter.decorator


import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetProductCardShimmerViewHolder
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetProductCardViewHolder
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetViewAllCardShimmerViewHolder
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetViewAllCardViewHolder
import com.tokopedia.unifycomponents.toPx

import javax.inject.Inject

@CMHomeWidgetScope
class CMHomeWidgetItemDecorator @Inject constructor() :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val currentItemViewHolder = parent.getChildViewHolder(view)
        val currentViewType = currentItemViewHolder.itemViewType
        val currentItemPosition = currentItemViewHolder.adapterPosition
        val totalItems = state.itemCount

        setItemSpacing(outRect, currentItemPosition, totalItems)

        setItemWidth(parent, view, currentViewType)
    }

    private fun setItemSpacing(outRect: Rect, currentItemPosition: Int, totalItems: Int) {
        when (currentItemPosition) {
            0 -> {
                outRect.left = START_END_SPACING_DP.toPx()
                outRect.right = MIDDLE_SPACING_DP.toPx()
            }
            totalItems - 1 -> {
                outRect.left = MIDDLE_SPACING_DP.toPx()
                outRect.right = START_END_SPACING_DP.toPx()
            }
            else -> {
                outRect.left = MIDDLE_SPACING_DP.toPx()
                outRect.right = MIDDLE_SPACING_DP.toPx()
            }
        }
    }

    private fun setItemWidth(
        parent: RecyclerView,
        view: View,
        currentViewType: Int
    ) {
        var ratio = CMHomeWidgetProductCardViewHolder.RATIO_WIDTH
        when (currentViewType) {
            CMHomeWidgetProductCardViewHolder.LAYOUT,
            CMHomeWidgetProductCardShimmerViewHolder.LAYOUT -> {
                ratio = CMHomeWidgetProductCardViewHolder.RATIO_WIDTH
            }
            CMHomeWidgetViewAllCardViewHolder.LAYOUT,
            CMHomeWidgetViewAllCardShimmerViewHolder.LAYOUT -> {
                ratio = CMHomeWidgetViewAllCardViewHolder.RATIO_WIDTH
            }
        }
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.width = (parent.measuredWidth * ratio).toInt()
        view.layoutParams = layoutParams
    }

    companion object {
        const val START_END_SPACING_DP = 10
        const val MIDDLE_SPACING_DP = 0
    }
}