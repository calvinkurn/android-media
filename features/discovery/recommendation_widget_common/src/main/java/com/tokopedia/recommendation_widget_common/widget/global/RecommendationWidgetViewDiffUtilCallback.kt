package com.tokopedia.recommendation_widget_common.widget.global

import android.view.View
import androidx.recyclerview.widget.DiffUtil

internal class RecommendationWidgetViewDiffUtilCallback(
    private val parentView: RecommendationWidgetView,
    private val visitableList: List<RecommendationVisitable>?,
    private val typeFactory: RecommendationTypeFactory,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = parentView.childCount

    override fun getNewListSize(): Int = visitableList?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        currentChildHasSameLayoutId(
            parentView.getChildAt(oldItemPosition),
            visitableList?.get(newItemPosition)
        )

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        false

    private fun currentChildHasSameLayoutId(child: View?, visitable: RecommendationVisitable?) =
        child is IRecommendationWidgetView<*>
            && visitable != null
            && child.layoutId == visitable.type(typeFactory)
}
