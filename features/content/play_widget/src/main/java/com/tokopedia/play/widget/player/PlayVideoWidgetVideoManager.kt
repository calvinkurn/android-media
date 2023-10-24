package com.tokopedia.play.widget.player

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.PlayVideoWidgetView
import com.tokopedia.play_common.util.extension.getVisiblePortion

/**
 * Created by kenny.hadisaputra on 23/10/23
 */
class PlayVideoWidgetVideoManager(
    private val recyclerView: RecyclerView,
    private val lifecycleOwner: LifecycleOwner,
    private val config: Config = Config(),
) {

    private val widgets = mutableSetOf<PlayVideoWidgetView>()

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val layoutManager = recyclerView.layoutManager
            setupVideoAutoplay(newState, layoutManager)
        }
    }

    private val layoutChangeListener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        setupVideoAutoplay(recyclerView.scrollState, recyclerView.layoutManager)
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
        recyclerView.addOnLayoutChangeListener(layoutChangeListener)

        recyclerView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                recyclerView.removeOnScrollListener(scrollListener)
                recyclerView.removeOnLayoutChangeListener(layoutChangeListener)
            }
        })
    }

    fun bind(videoWidget: PlayVideoWidgetView) {
        widgets.add(videoWidget)
    }

    fun pause() {
        widgets.forEach { it.pauseVideo() }
    }

    fun resume() {
        setupVideoAutoplay(recyclerView.scrollState, recyclerView.layoutManager)
    }

    fun release() {
        widgets.forEach { it.releaseVideo() }
    }

    private fun setupVideoAutoplay(
        state: Int,
        layoutManager: RecyclerView.LayoutManager?,
    ) {
        when (layoutManager) {
            null -> error("LayoutManager has not been set")
            is LinearLayoutManager -> setupVideoAutoplay(state, layoutManager)
            else -> error("Only LinearLayoutManager and its descendant(s) are supported")
        }
    }

    private fun setupVideoAutoplay(
        state: Int,
        layoutManager: LinearLayoutManager,
    ) {
        if (!lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) return
        if (state != RecyclerView.SCROLL_STATE_IDLE) return
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        val visibleVideoWidgets = (firstVisiblePosition..lastVisiblePosition).mapNotNull {
            val view = layoutManager.findViewByPosition(it)
            if (view !is PlayVideoWidgetView) null
            else view
        }
        val playableVideoWidgets = visibleVideoWidgets.filter(::isVideoWidgetConsideredVisible)
            .take(config.autoPlayAmount)
            .toSet()
        val otherVideoWidgets = widgets - playableVideoWidgets

        playableVideoWidgets.forEach { it.resumeVideo() }
        otherVideoWidgets.forEach { it.pauseVideo() }
    }

    private fun isVideoWidgetConsideredVisible(videoWidgetView: PlayVideoWidgetView): Boolean {
        val visiblePercentage = videoWidgetView.getVisiblePortion()
        val videoWidgetArea = videoWidgetView.width * videoWidgetView.height
        val visibleArea = visiblePercentage[0] * videoWidgetView.width * visiblePercentage[1] * videoWidgetView.height

        return visibleArea >= videoWidgetArea * config.visiblePercentageBeforeAutoplay
    }

    data class Config(
        val autoPlayAmount: Int = 2,
        val visiblePercentageBeforeAutoplay: Float = 0.7f,
    )
}
