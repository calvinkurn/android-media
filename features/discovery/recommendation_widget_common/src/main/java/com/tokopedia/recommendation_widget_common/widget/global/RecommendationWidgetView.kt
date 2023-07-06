package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.coroutines.Job
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
    private var job: Job? = null

    private fun init() { }

    fun bind(model: RecommendationWidgetModel) {
        val lifecycleOwner = context as? LifecycleOwner ?: return

        job?.cancel()
        job = lifecycleOwner.lifecycleScope.launch {
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
        val diffUtilCallback = RecommendationWidgetViewDiffUtilCallback(
            parentView = this,
            visitableList = visitableList,
            typeFactory = typeFactory
        )

        val listUpdateCallback = RecommendationWidgetListUpdateCallback(
            parentView = this,
            visitableList = visitableList,
            typeFactory = typeFactory,
        )

        DiffUtil
            .calculateDiff(diffUtilCallback, false)
            .dispatchUpdatesTo(listUpdateCallback)

        if (visitableList.isNullOrEmpty()) hide()
        else show()
    }

    fun recycle() {
        job?.cancel()
        job = null

        forEach { view ->
            if (view is IRecommendationWidgetView<*>)
                view.recycle()
        }
    }
}
