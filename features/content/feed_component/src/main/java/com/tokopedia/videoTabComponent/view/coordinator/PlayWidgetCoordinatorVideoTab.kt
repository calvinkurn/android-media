package com.tokopedia.videoTabComponent.view.coordinator

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.analytic.impression.ImpressionHelper
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren

class PlayWidgetCoordinatorVideoTab(
        lifecycleOwner: LifecycleOwner? = null,
        mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
) : LifecycleObserver {

    private val scope = CoroutineScope(mainCoroutineDispatcher)

    private var mListener: PlayWidgetListener? = null
    private var mAnalyticListener: PlayWidgetAnalyticListener? = null

    private val impressionHelper = ImpressionHelper()

    init {
        lifecycleOwner?.let { configureLifecycle(it) }
    }

    fun controlWidget(widget: PlayWidgetMediumView) {
        widget.setWidgetListener(mListener)
        widget.setAnalyticListener(mAnalyticListener)
    }

    fun controlWidget(widget: PlayWidgetLargeView) {
        widget.setWidgetListener(mListener)
        widget.setAnalyticListener(mAnalyticListener)
    }

    fun controlWidget(widget: PlayWidgetJumboView) {
        widget.setWidgetListener(mListener)
        widget.setAnalyticListener(mAnalyticListener)
    }

    fun connect(widget: PlayWidgetMediumView, model: PlayWidgetUiModel) {
        widget.setData(model)
    }

    fun connect(widget: PlayWidgetLargeView, model: PlayWidgetUiModel) {
        widget.setData(model)
    }

    fun connect(widget: PlayWidgetJumboView, model: PlayWidgetUiModel) {
        widget.setData(model)
    }

    fun setListener(listener: PlayWidgetListener?) {
        mListener = listener
    }

    fun setAnalyticListener(listener: PlayWidgetAnalyticListener?) {
        mAnalyticListener = listener
    }

    fun getImpressionHelper(): ImpressionHelper {
        return impressionHelper
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        scope.coroutineContext.cancelChildren()
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