package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.forEach
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recommendation_widget_common.viewutil.asLifecycleOwner
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext
import com.tokopedia.recommendation_widget_common.viewutil.launchRepeatOnStarted
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import android.R as androidR

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
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private val recommendationWidgetViewModel by recommendationWidgetViewModel()
    private val typeFactory = RecommendationTypeFactoryImpl()
    private var job: MutableList<Job>? = mutableListOf()

    private fun init() { }

    fun bind(
        model: RecommendationWidgetModel,
        parentRootView: View? = null,
        callback: Callback? = null,
    ) {
        val lifecycleOwner = context.asLifecycleOwner() ?: return

        initializeJobs(lifecycleOwner, model, parentRootView, callback)

        recommendationWidgetViewModel?.bind(model)
    }

    private fun initializeJobs(
        lifecycleOwner: LifecycleOwner,
        model: RecommendationWidgetModel,
        parentRootView: View?,
        callback: Callback?,
    ) {
        job?.forEach { it.cancel() }
        job?.clear()

        job = mutableListOf<Job>().apply {
            add(
                lifecycleOwner.launchRepeatOnStarted {
                    recommendationWidgetViewModel
                        ?.stateFlow
                        ?.map { it.widgetMap[model.id] }
                        ?.distinctUntilChanged()
                        ?.collectLatest { visitableList -> bind(visitableList, callback) }
                }
            )

            add(
                lifecycleOwner.launchRepeatOnStarted {
                    recommendationWidgetViewModel
                        ?.stateFlow
                        ?.map { it.successMessage }
                        ?.distinctUntilChanged()
                        ?.collectLatest { msg -> showSuccessMessage(parentRootView, msg) }
                }
            )

            add(
                lifecycleOwner.launchRepeatOnStarted {
                    recommendationWidgetViewModel
                        ?.stateFlow
                        ?.map { it.errorMessage }
                        ?.distinctUntilChanged()
                        ?.collectLatest { msg -> showErrorMessage(parentRootView, msg) }
                }
            )
        }
    }

    private fun bind(visitableList: List<RecommendationVisitable>?, callback: Callback?) {
        val diffUtilCallback = RecommendationWidgetViewDiffUtilCallback(
            parentView = this,
            visitableList = visitableList,
            typeFactory = typeFactory
        )

        val listUpdateCallback = RecommendationWidgetListUpdateCallback(
            parentView = this,
            visitableList = visitableList,
            typeFactory = typeFactory
        )

        DiffUtil
            .calculateDiff(diffUtilCallback, false)
            .dispatchUpdatesTo(listUpdateCallback)

        renderView(visitableList, callback)
    }

    private fun renderView(
        visitableList: List<RecommendationVisitable>?,
        callback: Callback?,
    ) {
        if (visitableList.isNullOrEmpty()) {
            hide()

            if (visitableList?.isEmpty() == true)
                callback?.onError()
        } else {
            show()
            callback?.onShow()
        }
    }

    private fun showSuccessMessage(parentRootView: View?, successMessage: String?) {
        val toasterView = parentRootView ?: rootView() ?: return
        successMessage ?: return

        showToastMessage(toasterView, successMessage, Toaster.TYPE_NORMAL)
    }

    private fun rootView(): View? =
        context?.getActivityFromContext()?.findViewById(androidR.id.content)

    private fun showErrorMessage(parentRootView: View?, errorMessage: String?) {
        val toasterView = parentRootView ?: rootView() ?: return
        errorMessage ?: return

        showToastMessage(toasterView, errorMessage, Toaster.TYPE_ERROR)
    }

    private fun showToastMessage(view: View, message: String, type: Int) {
        if (message.isNotBlank()) {
            Toaster.build(view, message, Toaster.LENGTH_LONG, type).show()
        }

        recommendationWidgetViewModel?.dismissMessage()
    }

    fun recycle() {
        job?.forEach { it.cancel() }
        job?.clear()
        job = null

        forEach { view ->
            if (view is IRecommendationWidgetView<*>) {
                view.recycle()
            }
        }
    }

    interface Callback {
        fun onShow() { }
        fun onError() { }
    }
}
