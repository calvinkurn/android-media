package com.tokopedia.play_common.widget.playBannerCarousel.model

import android.os.CountDownTimer
import android.view.View
import com.google.android.exoplayer2.SimpleExoPlayer
import com.tokopedia.play_common.widget.playBannerCarousel.widget.VideoPlayerView

data class ViewPlayerModel(
        val videoView: VideoPlayerView,
        val videoPlayer: SimpleExoPlayer,
        var position: Int = -1,
        var viewHolderParent: View? = null,
        val timerAutoPlay: CountDownTimer? = null,
        val timerAutoPause: CountDownTimer? = null
)