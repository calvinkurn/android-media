package com.tokopedia.video_widget.carousel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.video_widget.R
import com.tokopedia.video_widget.VideoPlayer
import com.tokopedia.video_widget.VideoPlayerProvider
import com.tokopedia.video_widget.databinding.LayoutInspirationCarouselVideoBinding
import com.tokopedia.video_widget.util.networkmonitor.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class InspirationCarouselVideoViewHolder(
    itemView: View,
    private val inspirationVideoCarouselListener: InspirationVideoCarouselListener,
    private val videoCarouselWidgetCoordinator: VideoCarouselWidgetCoordinator,
    private val networkMonitor: NetworkMonitor,
) : AbstractViewHolder<Visitable<*>>(itemView),
    VideoPlayerProvider,
    VideoCarouselItemListener,
    CoroutineScope {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_inspiration_carousel_video
    }

    private var binding: LayoutInspirationCarouselVideoBinding? by viewBinding()

    private val masterJob = SupervisorJob()
    private var wifiMonitorJob : Job? = null
    override val coroutineContext = masterJob + Dispatchers.Main

    private fun monitorWifiConnectionChange() {
        wifiMonitorJob?.cancel()
        wifiMonitorJob = launch {
            networkMonitor.wifiConnectionState.cancellable()
                .collect {
                    onWifiConnectionChange(it)
                }
        }

    }

    private fun  onWifiConnectionChange(isWifiConnected: Boolean) {
        val binding = binding ?: return
        binding.videoCarousel.onWifiConnectionChange(isWifiConnected)
    }

    override fun bind(element: Visitable<*>) {
        if(element !is VideoCarouselDataWrapper) return
        val data = element.getVideoCarouselData() ?: return
        val videoCarousel = binding?.videoCarousel ?: return
        monitorWifiConnectionChange()
        videoCarouselWidgetCoordinator.controlWidget(videoCarousel, this)
        videoCarouselWidgetCoordinator.connect(videoCarousel, data)
    }

    override fun onViewRecycled() {
        masterJob.cancelChildren()
        binding?.videoCarousel?.recycle()
        super.onViewRecycled()
    }

    override val videoPlayer: VideoPlayer?
        get() = binding?.videoCarousel

    override val isAutoplayEnabled: Boolean = true

    override fun onVideoCarouselItemImpressed(videoItem: VideoCarouselDataView.VideoItem) {
        inspirationVideoCarouselListener.onInspirationVideoCarouselProductImpressed(videoItem)
    }

    override fun onVideoCarouselItemClicked(videoItem: VideoCarouselDataView.VideoItem) {
        inspirationVideoCarouselListener.onInspirationVideoCarouselProductClicked(videoItem)
    }
}
