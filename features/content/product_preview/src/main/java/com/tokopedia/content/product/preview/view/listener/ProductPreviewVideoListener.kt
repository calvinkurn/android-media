package com.tokopedia.content.product.preview.view.listener

import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer

interface ProductPreviewVideoListener {
    fun onImpressedVideo()
    fun getVideoPlayer(id: String): ProductPreviewExoPlayer
    fun pauseVideo(id: String)
    fun resumeVideo(id: String)
    fun onScrubbing()
    fun onStopScrubbing()
}
