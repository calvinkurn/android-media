package com.tokopedia.play.view.viewcomponent.realtimenotif

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.util.animation.DefaultAnimatorListener
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

    private val rtnQueue = MutableSharedFlow<RealTimeNotificationUiModel>(
            extraBufferCapacity = 64,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val showListener = object : DefaultAnimatorListener() {
        override fun onAnimationStart(animation: Animator) {
            listener.onShowNotification(
                    this@RealTimeNotificationViewComponent,
                    rtnBubbleView.measuredHeight.toFloat()
            )
        }
    }

    private val hideListener = object : DefaultAnimatorListener() {
        override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
            listener.onHideNotification(this@RealTimeNotificationViewComponent)
        }
    }

    private val animatorSet = AnimatorSet()

    init {
        initRtnQueue()
    }

    fun queueNotification(notification: RealTimeNotificationUiModel) {
        scope.launch { rtnQueue.emit(notification) }
    }

    fun setLifespan(lifespanInMs: Long) {
        this.lifespanInMs = lifespanInMs
    }

    fun getRtnHeight(): Int {
        return rtnBubbleView.measuredHeight
    }

    private suspend fun setRealTimeNotification(rtn: RealTimeNotificationUiModel) {
        setNotification(rtn)
        rtnBubbleView.awaitPreDraw()
        val height = rtnBubbleView.measuredHeight
        if (isShown()) {
            listener.onShowNotification(
                    this@RealTimeNotificationViewComponent,
                    height.toFloat()
            )
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

    private suspend fun runAnimation() = suspendCancellableCoroutine<Unit> { cont ->
        val animatorListener = object : DefaultAnimatorListener() {
            override fun onAnimationCancel(animation: Animator) {
                rtnBubbleView.hide()
                listener.onHideNotification(this@RealTimeNotificationViewComponent)
            }

            override fun onAnimationEnd(animation: Animator) {
                rtnBubbleView.hide()
                if (cont.isActive) cont.resume(Unit)
            }

            override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                rtnBubbleView.show()
            }
        }

        val showAnimation = ObjectAnimator.ofFloat(rtnBubbleView, "translationX", -1f * rtnBubbleView.measuredWidth, 0f).apply {
            duration = SLIDE_DURATION_IN_MS
            addListener(showListener)
        }

        val delayAnimation = ValueAnimator.ofInt(0).apply {
            duration = lifespanInMs
        }

        val hideAnimation = ObjectAnimator.ofFloat(rtnBubbleView, "translationX", 0f, -1f * rtnBubbleView.measuredWidth).apply {
            duration = SLIDE_DURATION_IN_MS
            addListener(hideListener)
        }

        animatorSet.playSequentially(showAnimation, delayAnimation, hideAnimation)
        animatorSet.removeAllListeners()
        animatorSet.addListener(animatorListener)

        cont.invokeOnCancellation {
            animatorSet.cancel()
        }
        animatorSet.start()
    }

    private fun initRtnQueue() {
        scope.launch {
            rtnQueue.collect {
                setRealTimeNotification(it)
                runAnimation()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        animatorSet.cancel()
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