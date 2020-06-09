package com.tokopedia.play_common.widget.playBannerCarousel.model

import android.view.View
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.coroutines.Job

data class ViewPlayerModel(
        val videoPlayer: SimpleExoPlayer? = null,
        var position: Int = -1,
        var viewHolderParent: View? = null,
        var autoPlayJob: Job? = null
)