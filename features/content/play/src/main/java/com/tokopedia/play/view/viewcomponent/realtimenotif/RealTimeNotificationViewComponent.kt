package com.tokopedia.play.view.viewcomponent.realtimenotif

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.util.animation.DefaultTransitionListener
import com.tokopedia.play.view.custom.realtimenotif.RealTimeNotificationBubbleView
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel
import com.tokopedia.play_common.util.extension.awaitPreDraw
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.resume

/**
 * Created by jegul on 12/08/21
 */
class RealTimeNotificationViewComponent(
        container: ViewGroup,
        private val listener: Listener,
) : ViewComponent(container, R.id.view_real_time_notification) {

    private val rtnBubbleView: RealTimeNotificationBubbleView = findViewById(
            R.id.rtn_bubble
    )

    private var lifespanInMs = DEFAULT_LIFESPAN_IN_MS

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var rtnJob: Job? = null
    private val rtnQueue = MutableSharedFlow<RealTimeNotificationUiModel>(
            extraBufferCapacity = 64,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        initRtnQueue()
    }

    fun queueNotification(notification: RealTimeNotificationUiModel) {
        scope.launch { rtnQueue.emit(notification) }
    }

    fun setLifespan(lifespanInMs: Long) {
        this.lifespanInMs = lifespanInMs
    }

    private suspend fun showAnimated() = suspendAnimation {
        rtnBubbleView.show()
    }

    private suspend fun hideAnimated() = suspendAnimation {
        rtnBubbleView.invisible()
    }

    private suspend fun setRealTimeNotification(rtn: RealTimeNotificationUiModel) {
        setNotification(rtn)
        scope.launch {
            rootView.awaitPreDraw()
            val height = rootView.measuredHeight
            if (isShown()) {
                listener.onShowNotification(
                        this@RealTimeNotificationViewComponent,
                        height.toFloat()
                )
            }
        }
    }

    private fun setNotification(notification: RealTimeNotificationUiModel) {
        rtnBubbleView.setText(notification.text)
        rtnBubbleView.setIconUrl(notification.icon)

        val bgColor = try {
            Color.parseColor(notification.bgColor)
        } catch (e: IllegalArgumentException) {
            MethodChecker.getColor(
                    rootView.context,
                    R.color.play_dms_default_real_time_notif_bg
            )
        }
        rtnBubbleView.setBackgroundColor(bgColor)
    }

    private suspend fun suspendAnimation(viewFn: () -> Unit) = suspendCancellableCoroutine<Unit> { cont ->
        val animationListener = object : DefaultTransitionListener() {
            override fun onTransitionEnd(transition: Transition) {
                if (cont.isActive) cont.resume(Unit)
            }
        }

        val transition = Slide(Gravity.START)
                .setDuration(SLIDE_DURATION_IN_MS)
                .addListener(animationListener)

        cont.invokeOnCancellation {
            transition.removeListener(animationListener)
            TransitionManager.endTransitions(rtnBubbleView)
        }
        TransitionManager.beginDelayedTransition(
                rtnBubbleView as ViewGroup,
                transition
        )
        viewFn()
    }

    private fun initRtnQueue() {
        if (rtnJob?.isActive == true) return
        rtnJob = scope.launch {
            rtnQueue.collect {
                setRealTimeNotification(it)

                showAnimated()
                delay(lifespanInMs)

                listener.onHideNotification(this@RealTimeNotificationViewComponent)
                hideAnimated()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        initRtnQueue()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        rtnJob?.cancel()
        listener.onHideNotification(this@RealTimeNotificationViewComponent)
        scope.launch { hideAnimated() }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        job.cancelChildren()
    }

    companion object {

        private const val DEFAULT_LIFESPAN_IN_MS = 1000L
        private const val SLIDE_DURATION_IN_MS = 300L
    }

    interface Listener {

        fun onShowNotification(view: RealTimeNotificationViewComponent, height: Float)
        fun onHideNotification(view: RealTimeNotificationViewComponent)
    }
}