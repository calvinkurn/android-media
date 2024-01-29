@file:SuppressLint("DEPRECATION", "DeprecatedMethod")

package com.tokopedia.recommendation_widget_common.widget.foryou.play

import android.annotation.SuppressLint
import android.graphics.Rect
import android.preference.PreferenceManager
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.widget.pref.PlayWidgetPreference
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import kotlin.math.max
import kotlin.math.min

class PlayVideoWidgetManager(
    private val recyclerView: RecyclerView?,
    private val lifecycleOwner: LifecycleOwner,
    private val config: ConfigVideoWidget = ConfigVideoWidget()
) {

    private val sharedPref by lazy(LazyThreadSafetyMode.NONE) {
        PreferenceManager.getDefaultSharedPreferences(recyclerView?.context?.applicationContext)
    }

    private val widgets = mutableSetOf<PlayVideoWidgetView>()

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val layoutManager = recyclerView.layoutManager
            setupVideoAutoplay(recyclerView, newState, layoutManager)
        }
    }

    private val screenRect: Rect
        get() {
            return Rect(0, 0, getScreenWidth(), getScreenHeight())
        }

    private val View.globalVisibleRect: Rect
        get() {
            val rect = Rect()
            getGlobalVisibleRect(rect)
            return rect
        }

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> pause()
                    Lifecycle.Event.ON_RESUME -> resume()
                    Lifecycle.Event.ON_DESTROY -> {
                        release()
                    }

                    else -> {}
                }
            }
        })

        recyclerView?.addOnScrollListener(scrollListener)

        recyclerView?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                recyclerView.removeOnScrollListener(scrollListener)
            }
        })
    }

    fun bind(videoWidget: PlayVideoWidgetView?) {
        if (videoWidget == null) return

        widgets.add(videoWidget)
    }

    fun pause() {
        widgets.forEach { it.pauseVideo() }
    }

    fun resume() {
        recyclerView?.let {
            setupVideoAutoplay(it, it.scrollState, it.layoutManager)
        }
    }

    fun release() {
        widgets.forEach { it.releaseVideo() }
    }

    private fun setupVideoAutoplay(
        parent: RecyclerView,
        state: Int,
        layoutManager: RecyclerView.LayoutManager?
    ) {
        if (layoutManager is StaggeredGridLayoutManager) {
            setupVideoAutoplay(parent, state, layoutManager)
        }
    }

    private fun setupVideoAutoplay(
        parent: RecyclerView,
        state: Int,
        layoutManager: StaggeredGridLayoutManager
    ) {
        if (!lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) return
        if (state != RecyclerView.SCROLL_STATE_IDLE) return
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPositions(null).firstOrNull()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPositions(null).firstOrNull()
        val visibleVideoWidgets =
            (firstVisiblePosition.orZero()..lastVisiblePosition.orZero()).mapNotNull {
                val view = layoutManager.findViewByPosition(it)
                if (view is PlayVideoWidgetView) {
                    view
                } else {
                    null
                }
            }
        val playableVideoWidgets =
            visibleVideoWidgets.filter { isVideoWidgetConsideredVisible(parent, it) }.toSet()

        val otherVideoWidgets = widgets - playableVideoWidgets

        playableVideoWidgets.forEach { playVideoWidgetView ->
            if (DeviceConnectionInfo.isConnectWifi(parent.context) &&
                isEnableAutoPlay(playVideoWidgetView.getPlayWidgetUiModel().isAutoPlay)
            ) {
                playVideoWidgetView.startVideo()
            } else {
                playVideoWidgetView.pauseVideo()
            }
        }
        otherVideoWidgets.forEach { it.pauseVideo() }
    }

    private fun isEnableAutoPlay(isAutoPlayFromBE: Boolean): Boolean {
        val isAutoPlayFromSettings = sharedPref.getBoolean(PlayWidgetPreference.KEY_PLAY_WIDGET_AUTOPLAY, true)
        return if (isAutoPlayFromSettings) isAutoPlayFromBE else false
    }

    private fun isVideoWidgetConsideredVisible(
        parent: RecyclerView,
        videoWidgetView: PlayVideoWidgetView
    ): Boolean {
        val visiblePercentage = videoWidgetView.getVisiblePortion(parent.globalVisibleRect)
        val videoWidgetArea = videoWidgetView.width * videoWidgetView.height
        val visibleArea =
            visiblePercentage[0] * videoWidgetView.width * visiblePercentage[1] * videoWidgetView.height

        return visibleArea >= videoWidgetArea * config.visiblePercentageBeforeAutoplay
    }

    private fun View.getVisiblePortion(boundsRect: Rect = screenRect): FloatArray {
        val hitRect = this.globalVisibleRect
        val visibleHeight = min(hitRect.bottom, boundsRect.bottom) - max(hitRect.top, boundsRect.top)
        val visibleWidth = min(hitRect.right, boundsRect.right) - max(hitRect.left, boundsRect.left)
        val visibleHeightPortion = if (height <= 0) {
            0f
        } else {
            visibleHeight.toFloat() / height
        }

        val visibleWidthPortion = if (width <= 0) {
            0f
        } else {
            visibleWidth.toFloat() / width
        }

        return floatArrayOf(visibleWidthPortion, visibleHeightPortion)
    }

    data class ConfigVideoWidget(
        val visiblePercentageBeforeAutoplay: Float = 0.7f
    )
}

