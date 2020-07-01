package com.tokopedia.play_common.widget.playBannerCarousel.model

import android.view.View
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.play_common.widget.playBannerCarousel.widget.VideoPlayerView
import kotlinx.coroutines.Job

data class ViewPlayerModel(
        val videoView: VideoPlayerView,
        val videoPlayer: SimpleExoPlayer,
        var position: Int = -1,
        var viewHolderParent: View? = null,
        var autoPlayJob: Job? = null
)