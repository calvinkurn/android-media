package com.tokopedia.recommendation_widget_common.widget.global

import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.ListUpdateCallback

internal class RecommendationWidgetListUpdateCallback(
    private val parentView: RecommendationWidgetView,
    private val visitableList: List<RecommendationVisitable>?,
    private val typeFactory: RecommendationTypeFactory,
): ListUpdateCallback {

    override fun onInserted(position: Int, count: Int) {
        for (i in position until (position + count)) {
            val visitable = visitableList?.get(i)
            try { tryCreateView(visitable) }
            catch (_: Exception) { }
        }
    }

    override fun onRemoved(position: Int, count: Int) {
        for (i in position until (position + count))
            if (parentView.getChildAt(i) != null)
                parentView.removeViewAt(i)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) { }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        parentView.getChildAt(position)?.bind(visitableList?.get(position))
    }

    private fun tryCreateView(visitable: RecommendationVisitable?) {
        visitable ?: return

        val widget = typeFactory.createView(parentView.context, visitable).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }

        parentView.addView(widget)

        widget.bind(visitable)
    }

    @Suppress("UNCHECKED_CAST")
    private fun View?.bind(visitable: RecommendationVisitable?) {
        visitable ?: return

        (this as? IRecommendationWidgetView<RecommendationVisitable>)?.bind(visitable)
    }
}
