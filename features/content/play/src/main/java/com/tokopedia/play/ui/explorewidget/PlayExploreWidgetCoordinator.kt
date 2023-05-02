package com.tokopedia.play.ui.explorewidget

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetAutoPlayCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import kotlinx.coroutines.*

/**
 * @author by astidhiyaa on 02/12/22
 */

class PlayExploreWidgetCoordinator(
    lifecycleOwner: LifecycleOwner? = null,
    mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : LifecycleObserver {

    private val scope = CoroutineScope(mainCoroutineDispatcher)

    private var mListener: PlayWidgetListener? = null
    private var mAnalyticListener: PlayWidgetAnalyticListener? = null

    private val autoplayCoordinator = PlayWidgetAutoPlayCoordinator(scope, mainCoroutineDispatcher)

    init {
        lifecycleOwner?.let { configureLifecycle(it) }
    }

    fun controlWidget(widget: PlayWidgetLargeView) {
        widget.setWidgetListener(mListener)
        widget.setAnalyticListener(mAnalyticListener)
    }

    fun connect(widget: PlayWidgetLargeView, model: PlayWidgetUiModel) {
        widget.setData(model)
        autoplayCoordinator.configureAutoPlayLarge(widget, model.config, PlayWidgetType.Medium)
    }

    fun setListener(listener: PlayWidgetListener?) {
        mListener = listener
    }

    fun setAnalyticListener(listener: PlayWidgetAnalyticListener?) {
        mAnalyticListener = listener
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        autoplayCoordinator.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        autoplayCoordinator.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        autoplayCoordinator.onDestroy()
        scope.coroutineContext.cancelChildren()
    }

    private fun configureLifecycle(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner is Fragment) {
            lifecycleOwner.viewLifecycleOwnerLiveData.observe(
                lifecycleOwner,
                Observer {
                    it.lifecycle.addObserver(this)
                }
            )
        } else {
            lifecycleOwner.lifecycle.addObserver(this)
        }
    }
}
