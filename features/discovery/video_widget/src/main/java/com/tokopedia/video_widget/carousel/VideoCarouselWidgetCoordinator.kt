package com.tokopedia.video_widget.carousel

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren

class VideoCarouselWidgetCoordinator(
    lifecycleOwner: LifecycleOwner? = null,
    mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    private val hasExternalAutoPlayController: Boolean = false,
) : LifecycleObserver {
    private val scope = CoroutineScope(mainCoroutineDispatcher)

    private var widget: VideoCarouselView? = null
    private var dataView: VideoCarouselDataView? = null

    private val autoPlayCoordinator = VideoCarouselAutoPlayCoordinator(
        scope,
        mainCoroutineDispatcher,
        hasExternalAutoPlayController,
    )

    private var listener: VideoCarouselItemListener? = null
    private val internalListener = object : VideoCarouselInternalListener {
        override fun playVideo(container: RecyclerView) {
            autoPlayCoordinator.playVideo(container)
        }

        override fun stopVideo() {
            autoPlayCoordinator.stopVideo()
        }

        override fun onWidgetCardsScrollChanged(container: RecyclerView) {
            autoPlayCoordinator.onWidgetCardsScrollChanged(container)
        }

        override fun onWidgetDetached(widget: View) {
            autoPlayCoordinator.onWidgetDetached(widget)
        }
    }

    init {
        lifecycleOwner?.let { configureLifecycle(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        autoPlayCoordinator.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        autoPlayCoordinator.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        autoPlayCoordinator.onDestroy()
        scope.coroutineContext.cancelChildren()
    }

    fun controlWidget(widget: VideoCarouselView, listener: VideoCarouselItemListener) {
        this.widget = widget
        widget.setWidgetInternalListener(internalListener)
        widget.setWidgetListener(listener)
    }

    fun setListener(listener: VideoCarouselItemListener?) {
        this.listener = listener
        widget?.setWidgetListener(listener)
    }

    fun connect(widget: VideoCarouselView, dataView: VideoCarouselDataView) {
        this.dataView = dataView
        widget.setCarouselModel(dataView)
        autoPlayCoordinator.configureAutoPlay(widget, dataView.config)
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
