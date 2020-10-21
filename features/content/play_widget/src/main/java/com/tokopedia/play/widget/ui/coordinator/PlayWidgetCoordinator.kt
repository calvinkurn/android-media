package com.tokopedia.play.widget.ui.coordinator

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigProvider
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import kotlinx.coroutines.*

/**
 * Created by jegul on 13/10/20
 */
class PlayWidgetCoordinator(
        lifecycleOwner: LifecycleOwner? = null,
        mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        workCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : LifecycleObserver, PlayWidgetAutoRefreshCoordinator.Listener {

    private val scope = CoroutineScope(mainCoroutineDispatcher)

    private var mWidget: PlayWidgetView? = null
    private var mModel: PlayWidgetUiModel = PlayWidgetUiModel.Placeholder

    private var mListener: PlayWidgetListener? = null
    private var mAnalyticListener: PlayWidgetAnalyticListener? = null

    private val widgetHolderListener = object : PlayWidgetViewHolder.Listener {

        override fun onWidgetImpressed(view: PlayWidgetView, position: Int) {
            mAnalyticListener?.onImpressPlayWidget(view, position)
        }
    }

    private val autoRefreshCoordinator = PlayWidgetAutoRefreshCoordinator(
            scope,
            mainCoroutineDispatcher,
            workCoroutineDispatcher,
            this
    )

    init {
        lifecycleOwner?.let { configureLifecycle(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        autoRefreshCoordinator.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        val currentModel = mModel
        if (currentModel is PlayWidgetConfigProvider) {
            autoRefreshCoordinator.configureAutoRefresh(currentModel.config)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        scope.coroutineContext.cancelChildren()
    }

    override fun onWidgetShouldRefresh() {
        mWidget?.let { mListener?.onWidgetShouldRefresh(it) }
    }

    fun controlWidget(widget: PlayWidgetView) {
        mWidget = widget
        widget.setAnalyticListener(mAnalyticListener)
    }

    fun controlWidget(widgetViewHolder: PlayWidgetViewHolder) {
        controlWidget(widgetViewHolder.itemView as PlayWidgetView)
        widgetViewHolder.setListener(widgetHolderListener)
    }

    fun setListener(listener: PlayWidgetListener?) {
        mListener = listener
    }

    fun setAnalyticListener(listener: PlayWidgetAnalyticListener?) {
        mAnalyticListener = listener
        mWidget?.setAnalyticListener(listener)
    }

    fun connect(widget: PlayWidgetView, model: PlayWidgetUiModel) {
        mModel = model
        widget.setModel(model)

        if (model is PlayWidgetConfigProvider) {
            autoRefreshCoordinator.configureAutoRefresh(model.config)
        }
    }

    private fun configureLifecycle(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner is Fragment) {
            lifecycleOwner.viewLifecycleOwnerLiveData.observe(lifecycleOwner, Observer {
                it.lifecycle.addObserver(this)
            })
        } else {
            lifecycleOwner.lifecycle.addObserver(this)
        }
    }
}