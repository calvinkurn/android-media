package com.tokopedia.video_widget.carousel

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
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
import kotlinx.coroutines.launch
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class InspirationCarouselVideoViewHolder(
    itemView: View,
    private val inspirationVideoCarouselListener: InspirationVideoCarouselListener,
    private val videoCarouselWidgetCoordinator: VideoCarouselWidgetCoordinator,
    private val networkMonitor: NetworkMonitor,
    private val isReimagine: Boolean = false,
    isSneakPeekEnabled: Boolean = false,
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
        setSeparator()
        setMarginContainerView()
        videoCarouselWidgetCoordinator.controlWidget(videoCarousel, this)
        videoCarouselWidgetCoordinator.connect(videoCarousel, data, isReimagine)
    }

    private fun setSeparator(){
        binding?.viewSeparatorTop?.showWithCondition(!isReimagine)
        binding?.viewSeparatorBottom?.showWithCondition(!isReimagine)
    }

    private fun setMarginContainerView() {
        val videoCarousel = binding?.inspirationCarousel ?: return
        val context = videoCarousel.context
        if(isReimagine) {
            val marginBottom = context.getMarginBottom().orZero()
            videoCarousel.setMargin(0,0,0,marginBottom)
        } else {
            val marginTop = context.getMarginTop().orZero()
            val marginBottom = context.getMarginBottom().orZero()
            videoCarousel.setMargin(0, marginTop, 0, marginBottom)
        }
    }

    override fun onViewRecycled() {
        masterJob.cancelChildren()
        binding?.videoCarousel?.recycle()
        super.onViewRecycled()
    }

    override val videoPlayer: VideoPlayer?
        get() = binding?.videoCarousel

    override val isAutoplayEnabled: Boolean = isSneakPeekEnabled

    override fun onVideoCarouselItemImpressed(videoItem: VideoCarouselDataView.VideoItem) {
        inspirationVideoCarouselListener.onInspirationVideoCarouselProductImpressed(videoItem)
    }

    override fun onVideoCarouselItemClicked(videoItem: VideoCarouselDataView.VideoItem) {
        inspirationVideoCarouselListener.onInspirationVideoCarouselProductClicked(videoItem)
    }

    private fun Context.getMarginBottom(): Int? =
        this.resources?.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_8)

    private fun Context.getMarginTop(): Int? =
        this.resources?.getDimensionPixelSize(R.dimen.video_carousel_margin_top)
}
