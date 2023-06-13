package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Created by frenzel on 11/03/23
 */
class RecommendationWidgetView : LinearLayout {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private val recommendationWidgetViewModel by recommendationWidgetViewModel()
    private val typeFactory = RecommendationTypeFactoryImpl()

    private fun init() { }

    fun bind(model: RecommendationWidgetModel) {
        val lifecycleOwner = context as? LifecycleOwner ?: return

        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(STARTED) {
                recommendationWidgetViewModel
                    ?.stateFlow
                    ?.map { it.widgetMap[model.id] }
                    ?.distinctUntilChanged()
                    ?.collectLatest(::bind)
            }
        }

        recommendationWidgetViewModel?.bind(model)
    }

    private fun bind(visitableList: List<RecommendationVisitable>?) {
        removeAllViews()

        visitableList?.forEach { visitable ->
            try { tryBindView(visitable) }
            catch (_: Exception) { }
        }
    }

    private fun tryBindView(visitable: RecommendationVisitable) {
        val widget = typeFactory.createView(context, visitable)
        widget.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        addView(widget)

        (widget as? IRecommendationWidgetView<RecommendationVisitable>)?.bind(visitable)
    }
}
