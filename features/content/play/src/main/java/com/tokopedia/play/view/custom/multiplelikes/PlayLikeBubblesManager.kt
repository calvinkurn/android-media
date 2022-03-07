package com.tokopedia.play.view.custom.multiplelikes

import android.graphics.Bitmap
import com.tokopedia.play.view.uimodel.PlayLikeBubbleUiModel
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class PlayLikeBubblesManager(
    private val scope: CoroutineScope,
) {

    private var mView: PlayLikeBubblesView? = null
    private val bubbles = mutableListOf<Bubble>()

    private var timeInterval = getTimeByFps(FULL_FPS)

    private var timerJob: Job? = null

    private val viewWidth: Int
        get() = mView?.width ?: 0

    private val viewHeight: Int
        get() = mView?.height ?: 0

    fun setFps(fps: Int) {
        this.timeInterval = getTimeByFps(fps)
    }

    fun start() {
        initTimer()
    }

    private fun initTimer() {
        if (timerJob?.isActive == true) return
        timerJob = scope.launch {
            while(isActive) {
                delay(timeInterval)
                val time = System.currentTimeMillis()
                val newBubbles = synchronized(bubbles) {
                    bubbles.filter {
                        !it.onTimeChanged(time, mView?.width ?: 0, mView?.height ?: 0)
                    }
                }
                bubbles.clear()
                bubbles.addAll(newBubbles)
                mView?.setBubbles(bubbles)
                mView?.postInvalidate()
            }
        }
    }

    fun shot(
        likeAmount: Int,
        shotPerBatch: Int,
        prioritize: Boolean = false,
        delayPerBatchInMs: Long = 0L,
        reduceOpacity: Boolean = false,
        bubbleList: List<PlayLikeBubbleUiModel> = emptyList(),
    ) {
        scope.launch {
            for (i in 1..likeAmount) {
                if (delayPerBatchInMs > 0) delay(delayPerBatchInMs)
                for(j in 1..shotPerBatch) {
                    if (j != 1) delay(DEFAULT_DELAY)
                    val chosenBubble = bubbleList.random()
                    shotInternal(
                        chosenBubble.icon,
                        chosenBubble.colorList.random(),
                        reduceOpacity,
                        prioritize
                    )
                }
            }
        }
    }

    private fun shotInternal(
        icon: Bitmap,
        color: Int,
        reduceOpacity: Boolean,
        prioritize: Boolean,
    ) {
        synchronized(bubbles) {
            if (bubbles.size > MAX_BUBBLES_ON_SCREEN) {
                if (prioritize) bubbles.removeFirst()
                else return@synchronized
            }
            bubbles.add(
                Bubble(
                    icon = icon,
                    color = color,
                    reduceOpacity = reduceOpacity,
                    parentWidth = viewWidth,
                    parentHeight = viewHeight,
                )
            )
        }
    }

    fun setView(view: PlayLikeBubblesView?) {
        mView = view
    }

    fun stop() {
        timerJob?.cancel()
        synchronized(bubbles) { bubbles.clear() }
    }

    private fun getTimeByFps(fps: Int): Long {
        return TimeUnit.SECONDS.toMillis(1) / fps
    }

    companion object {
        private const val DEFAULT_DELAY = 300L

        private const val MAX_BUBBLES_ON_SCREEN = 30

        private const val FULL_FPS = 60
    }
}