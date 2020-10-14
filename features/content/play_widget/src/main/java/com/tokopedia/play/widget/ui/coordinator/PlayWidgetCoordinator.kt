package com.tokopedia.play.widget.ui.coordinator

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
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
        private val timerCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : LifecycleObserver {

    private val scope = CoroutineScope(mainCoroutineDispatcher)
    private var timerJob: Job? = null

    private var mWidget: PlayWidgetView? = null
    private var mModel: PlayWidgetUiModel = PlayWidgetUiModel.Placeholder

    private var mListener: PlayWidgetListener? = null

    init {
        lifecycleOwner?.let { configureLifecycle(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        stopTimer()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        val currentModel = mModel
        if (currentModel is PlayWidgetConfigProvider) {
            configureAutoRefresh(currentModel.config)
        }
    }

    fun controlWidget(widget: PlayWidgetView) {
        mWidget = widget
    }

    fun setListener(listener: PlayWidgetListener?) {
        mListener = listener
    }

    fun connect(widget: PlayWidgetView, model: PlayWidgetUiModel) {
        mModel = model
        widget.setModel(model)

        if (model is PlayWidgetConfigProvider) {
            configureAutoRefresh(model.config)
        }
    }

    private fun configureAutoRefresh(config: PlayWidgetConfigUiModel) {
        stopTimer()
        if (config.autoRefresh) {
            timerJob = scope.launch {
                initTimer(config.autoRefreshTimer) {
                    mWidget?.let { mListener?.onWidgetShouldRefresh(it) }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    private suspend fun initTimer(durationInSecs: Long, handler: () -> Unit) {
        withContext(timerCoroutineDispatcher) {
            delay(durationInSecs * 1000L)
        }
        handler()
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