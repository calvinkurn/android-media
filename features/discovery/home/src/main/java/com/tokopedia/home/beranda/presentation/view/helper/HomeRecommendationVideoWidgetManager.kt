package com.tokopedia.home.beranda.presentation.view.helper

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.play_common.util.extension.getVisiblePortion
import com.tokopedia.play_common.util.extension.globalVisibleRect

class HomeRecommendationVideoWidgetManager(
    private val recyclerView: RecyclerView,
    private val lifecycleOwner: LifecycleOwner,
    private val config: ConfigVideoWidget
) {

    private val widgets = mutableSetOf<PlayVideoWidgetView>()

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val layoutManager = recyclerView.layoutManager
            setupVideoAutoplay(recyclerView, newState, layoutManager)
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> pause()
                    Lifecycle.Event.ON_RESUME -> resume()
                    Lifecycle.Event.ON_DESTROY -> release()
                    else -> {}
                }
            }
        })

        recyclerView.addOnScrollListener(scrollListener)

        recyclerView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                recyclerView.removeOnScrollListener(scrollListener)
            }
        })
    }

    fun bind(videoWidget: PlayVideoWidgetView) {
        widgets.add(videoWidget)
    }

    fun showCoverWhenVideoFinished(videoWidget: PlayVideoWidgetView) {
        val videoWidgetIndex = widgets
    }

    fun pause() {
        widgets.forEach { it.pauseVideo() }
    }

    fun resume() {
        setupVideoAutoplay(recyclerView, recyclerView.scrollState, recyclerView.layoutManager)
    }

    fun release() {
        widgets.forEach { it.releaseVideo() }
    }

    private fun setupVideoAutoplay(
        parent: RecyclerView,
        state: Int,
        layoutManager: RecyclerView.LayoutManager?
    ) {
        if (layoutManager is LinearLayoutManager) {
            setupVideoAutoplay(parent, state, layoutManager)
        }
    }

    private fun setupVideoAutoplay(
        parent: RecyclerView,
        state: Int,
        layoutManager: LinearLayoutManager
    ) {
        if (!lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) return
        if (state != RecyclerView.SCROLL_STATE_IDLE) return
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        val visibleVideoWidgets = (firstVisiblePosition..lastVisiblePosition).mapNotNull {
            val view = layoutManager.findViewByPosition(it)
            if (view !is PlayVideoWidgetView) {
                null
            } else {
                view
            }
        }
        val playableVideoWidgets =
            visibleVideoWidgets.filter { isVideoWidgetConsideredVisible(parent, it) }
                .take(config.autoPlayAmount)
                .toSet()
        val otherVideoWidgets = widgets - playableVideoWidgets

        playableVideoWidgets.forEach {
            if (DeviceConnectionInfo.isConnectWifi(parent.context)) {
                it.startVideo()
            } else {
                it.pauseVideo()
            }
        }
        otherVideoWidgets.forEach { it.pauseVideo() }
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

    data class ConfigVideoWidget(
        val autoPlayAmount: Int,
        val visiblePercentageBeforeAutoplay: Float = 0.7f
    )
}
