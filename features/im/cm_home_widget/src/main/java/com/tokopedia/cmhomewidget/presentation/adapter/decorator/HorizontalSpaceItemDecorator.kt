package com.tokopedia.cmhomewidget.presentation.adapter.decorator

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetProductCardShimmerViewHolder
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetProductCardViewHolder
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetViewAllCardShimmerViewHolder
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetViewAllCardViewHolder
import com.tokopedia.unifycomponents.toPx
import timber.log.Timber


class HorizontalSpaceItemDecorator(
    private val startEndSpacingInDP: Int,
    private val middleSpacingInDP: Int
) :
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

        setViewAllCardHeight(parent, view, currentItemPosition, currentViewType)
    }

    private fun setItemSpacing(outRect: Rect, currentItemPosition: Int, totalItems: Int) {
        when (currentItemPosition) {
            0 -> {
                outRect.left = startEndSpacingInDP.toPx()
                outRect.right = middleSpacingInDP.toPx()
            }
            totalItems - 1 -> {
                outRect.left = middleSpacingInDP.toPx()
                outRect.right = startEndSpacingInDP.toPx()
            }
            else -> {
                outRect.left = middleSpacingInDP.toPx()
                outRect.right = middleSpacingInDP.toPx()
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
            CMHomeWidgetProductCardViewHolder.LAYOUT -> {
                ratio = CMHomeWidgetProductCardViewHolder.RATIO_WIDTH
            }
            CMHomeWidgetProductCardShimmerViewHolder.LAYOUT -> {
                ratio = CMHomeWidgetProductCardShimmerViewHolder.RATIO_WIDTH
            }
            CMHomeWidgetViewAllCardViewHolder.LAYOUT -> {
                ratio = CMHomeWidgetViewAllCardViewHolder.RATIO_WIDTH
            }
            CMHomeWidgetViewAllCardShimmerViewHolder.LAYOUT -> {
                ratio = CMHomeWidgetViewAllCardShimmerViewHolder.RATIO_WIDTH
            }
        }
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.width = (parent.measuredWidth * ratio).toInt()
        view.layoutParams = layoutParams
    }

    private fun setViewAllCardHeight(
        parent: RecyclerView,
        view: View,
        currentItemPosition: Int,
        currentViewType: Int
    ) {
        if (currentItemPosition > 0) {
            when (currentViewType) {
                CMHomeWidgetViewAllCardViewHolder.LAYOUT,
                CMHomeWidgetViewAllCardShimmerViewHolder.LAYOUT -> {
                    val layoutParams: ViewGroup.LayoutParams = view.layoutParams
                    layoutParams.height =
                        parent.measuredHeight - parent.paddingTop - parent.paddingBottom
                    view.layoutParams = layoutParams
                }
            }
        }
    }
}