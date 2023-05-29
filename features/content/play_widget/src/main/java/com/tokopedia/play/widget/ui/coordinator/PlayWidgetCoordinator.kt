package com.tokopedia.play.widget.ui.coordinator

import android.app.Activity
import android.app.Service
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.analytic.impression.ImpressionHelper
import com.tokopedia.play.widget.analytic.list.DefaultPlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.di.PlayWidgetComponent
import com.tokopedia.play.widget.di.PlayWidgetComponentCreator
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetAnalyticModel
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.WidgetInList
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren

/**
 * Created by jegul on 13/10/20
 */
class PlayWidgetCoordinator constructor(
    lifecycleOwner: LifecycleOwner,
    mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    workCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val autoHandleLifecycleMethod: Boolean = true,
) : LifecycleObserver, PlayWidgetAutoRefreshCoordinator.Listener {

    private val scope = CoroutineScope(mainCoroutineDispatcher)

    private var mWidget: PlayWidgetView? = null
    private var mState: PlayWidgetState = PlayWidgetState(isLoading = true)

    private var mListener: PlayWidgetListener? = null
    private var mAnalyticListener: PlayWidgetAnalyticListener? = null

    private val widgetHolderListener = object : PlayWidgetViewHolder.Listener {

        override fun onWidgetImpressed(view: PlayWidgetView, item: PlayWidgetUiModel, position: Int) {
            mAnalyticListener?.onImpressPlayWidget(view, item, position)
        }
    }

    private val autoPlayCoordinator = PlayWidgetAutoPlayCoordinator(scope, mainCoroutineDispatcher)

    private val autoRefreshCoordinator = PlayWidgetAutoRefreshCoordinator(
            scope,
            mainCoroutineDispatcher,
            workCoroutineDispatcher,
            this
    )

    private val mWidgetInternalListener = object : PlayWidgetInternalListener {
        /**
         * works for medium & small type only
         */
        override fun onWidgetCardsScrollChanged(widgetCardsContainer: RecyclerView) {
            autoPlayCoordinator.onWidgetCardsScrollChanged(widgetCardsContainer)
        }

        override fun onFocusedWidgetsChanged(focusedWidgets: List<WidgetInList>) {
            autoPlayCoordinator.onFocusedWidgetsChanged(focusedWidgets)
        }

        override fun onWidgetDetached(widget: View) {
            autoPlayCoordinator.onWidgetDetached(widget)
        }
    }

    private var impressionHelper = ImpressionHelper()

    private var widgetComponent: PlayWidgetComponent? = null

    private var trackingQueue: TrackingQueue? = null
    private val trackingLifecycleObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> trackingQueue?.sendAll()
            else -> {}
        }
    }

    init {
        val context: Context? = when (lifecycleOwner) {
            is Activity -> lifecycleOwner
            is Fragment -> lifecycleOwner.context
            is Service -> lifecycleOwner
            else -> null
        }

        if (context != null) {
            trackingQueue = TrackingQueue(context)
            widgetComponent = PlayWidgetComponentCreator.getOrCreate(context.applicationContext)
        }

        configureLifecycle(lifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        autoPlayCoordinator.onPause()
        autoRefreshCoordinator.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        val currentModel = mState
        autoRefreshCoordinator.configureAutoRefresh(currentModel.model.config)
        autoPlayCoordinator.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        autoPlayCoordinator.onDestroy()
        scope.coroutineContext.cancelChildren()
    }

    fun onNotVisible() {
        autoPlayCoordinator.onNotVisible()
    }

    fun onVisible() {
        autoPlayCoordinator.onVisible()
    }

    override fun onWidgetShouldRefresh() {
        mWidget?.let { mListener?.onWidgetShouldRefresh(it) }
    }

    fun controlWidget(widget: PlayWidgetView) {
        mWidget = widget
        widget.setAnalyticListener(mAnalyticListener)
        widget.setWidgetInternalListener(mWidgetInternalListener)
        widget.setWidgetListener(mListener)
    }

    fun controlWidget(widgetViewHolder: PlayWidgetViewHolder) {
        controlWidget(widgetViewHolder.itemView as PlayWidgetView)
        widgetViewHolder.setListener(widgetHolderListener)
    }

    fun setListener(listener: PlayWidgetListener?) {
        mListener = listener
        mWidget?.setWidgetListener(mListener)
    }

    fun setAnalyticModel(model: PlayWidgetAnalyticModel?) {
        val widgetComponent = this.widgetComponent
        val trackingQueue = this.trackingQueue
        if (model == null || widgetComponent == null || trackingQueue == null) {
            mAnalyticListener = null
            mWidget?.setAnalyticListener(null)
            return
        }

        val analyticFactory = widgetComponent.getGlobalAnalyticFactory()
        mAnalyticListener = DefaultPlayWidgetInListAnalyticListener(
            analyticFactory.create(model, trackingQueue)
        )
        mWidget?.setAnalyticListener(mAnalyticListener)
    }

    fun setAnalyticListener(listener: PlayWidgetAnalyticListener?) {
        mAnalyticListener = listener
        mWidget?.setAnalyticListener(listener)
    }

    fun connect(widget: PlayWidgetView, state: PlayWidgetState) {
        mState = state
        widget.setState(state)

        autoRefreshCoordinator.configureAutoRefresh(state.model.config)
        autoPlayCoordinator.configureAutoPlay(widget, state.model.config, state.widgetType)
    }

    fun setImpressionHelper(helper: ImpressionHelper) {
        impressionHelper = helper
    }

    fun getImpressionHelper(): ImpressionHelper {
        return impressionHelper
    }

    private fun configureLifecycle(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(trackingLifecycleObserver)

        if (!autoHandleLifecycleMethod) return

        if (lifecycleOwner is Fragment) {
            lifecycleOwner.viewLifecycleOwnerLiveData.observe(lifecycleOwner, Observer {
                it.lifecycle.addObserver(this)
            })
        } else {
            lifecycleOwner.lifecycle.addObserver(this)
        }
    }
}
