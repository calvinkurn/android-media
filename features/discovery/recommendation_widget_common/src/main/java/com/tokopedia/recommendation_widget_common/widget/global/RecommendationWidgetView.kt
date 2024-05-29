package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.forEach
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recommendation_widget_common.viewutil.asLifecycleOwner
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext
import com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import android.R as androidR

/**
 * Created by frenzel on 11/03/23
 */
class RecommendationWidgetView : LinearLayout, AppLogRecTriggerInterface {
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

    private var eligibleToTrack = false
    private var recTriggerObject = RecommendationTriggerObject()

    private fun init() { }

    fun bind(
        model: RecommendationWidgetModel,
        parentRootView: View? = null,
        callback: Callback? = null
    ) {
        val lifecycleOwner = context.asLifecycleOwner() ?: return

        initializeJobs(lifecycleOwner, model, parentRootView, callback)

        recommendationWidgetViewModel?.bind(model)
    }

    private fun initializeJobs(
        lifecycleOwner: LifecycleOwner,
        model: RecommendationWidgetModel,
        parentRootView: View?,
        callback: Callback?
    ) {
        job?.forEach { it.cancel() }
        job?.clear()

        job = mutableListOf<Job>().apply {
            add(
                lifecycleOwner.lifecycleScope.launch {
                    recommendationWidgetViewModel
                        ?.stateFlow
                        ?.map { it.widgetMap[model.id] }
                        ?.distinctUntilChanged()
                        ?.collectLatest { visitableList -> bind(visitableList, model.source, callback) }
                }
            )

            add(
                lifecycleOwner.lifecycleScope.launch {
                    recommendationWidgetViewModel
                        ?.stateFlow
                        ?.map { it.successMessage }
                        ?.distinctUntilChanged()
                        ?.collectLatest { msg -> showSuccessMessage(parentRootView, msg) }
                }
            )

            add(
                lifecycleOwner.lifecycleScope.launch {
                    recommendationWidgetViewModel
                        ?.stateFlow
                        ?.map { it.errorMessage }
                        ?.distinctUntilChanged()
                        ?.collectLatest { msg -> showErrorMessage(parentRootView, msg) }
                }
            )
        }
    }

    private fun bind(
        visitableList: List<RecommendationVisitable>?,
        source: RecommendationWidgetSource?,
        callback: Callback?
    ) {
        setRecTriggerObject(visitableList, source)
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
        callback: Callback?
    ) {
        if (visitableList.isNullOrEmpty()) {
            hide()

            if (visitableList?.isEmpty() == true) {
                callback?.onError()
            }
        } else {
            show()
            callback?.onShow(visitableList)
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

    private fun setRecTriggerObject(
        list: List<RecommendationVisitable>?,
        source: RecommendationWidgetSource?,
    ) {
        val model = list?.find { it is RecommendationVerticalModel && it.widget.recommendationItemList.isNotEmpty() }
        if(model != null) {
            eligibleToTrack = true
            recTriggerObject = RecommendationTriggerObject(
                sessionId = model.appLog.sessionId,
                requestId = model.appLog.requestId,
                moduleName = model.metadata.pageName,
                additionalParam = model.appLogAdditionalParam
            )
        }
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
        fun onShow(visitableList: List<RecommendationVisitable>?) { }
        fun onError() { }
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject {
        return recTriggerObject
    }

    override fun isEligibleToTrack(): Boolean {
        return eligibleToTrack
    }
}
