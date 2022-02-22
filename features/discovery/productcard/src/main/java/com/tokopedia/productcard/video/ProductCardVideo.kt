package com.tokopedia.productcard.video

import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardVideoView
import com.tokopedia.productcard.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class ProductCardVideo(
    private val productCardView: View,
) : ExoPlayerListener, ProductVideoPlayer {
    private val videoProduct by lazy(LazyThreadSafetyMode.NONE) {
        productCardView.findViewById<ProductCardVideoView>(R.id.videoProduct)
    }

    private var videoURL = ""
    private var videoPlayerStateFlow : MutableStateFlow<VideoPlayerState>? = null
    private val helper: ProductCardViewHelper by lazy(LazyThreadSafetyMode.NONE) {
        ProductCardViewHelper.Builder(productCardView.context, videoProduct)
            .setExoPlayerEventsListener(this)
            .create()
    }

    fun setProductModel(productCardModel: ProductCardModel) {
        videoURL = productCardModel.customVideoURL
    }

    fun clear() {
        helper.onViewDetach()
    }

    override fun onPlayerIdle() {
        videoProduct?.hide()
    }

    override fun onPlayerBuffering() {
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Buffering)
    }

    override fun onPlayerPlaying() {
        videoProduct?.show()
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Playing)
    }

    override fun onPlayerPaused() {
        videoProduct?.hide()
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Paused)
    }

    override fun onPlayerEnded() {
        videoProduct?.hide()
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Ended)
    }

    override fun onPlayerError(errorString: String?) {
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Error(errorString ?: ""))
    }

    override val hasProductVideo: Boolean
        get() = videoURL.isNotBlank()

    override fun playVideo(): Flow<VideoPlayerState> {
        if(!hasProductVideo) return flowOf(VideoPlayerState.NoVideo)
        videoPlayerStateFlow = MutableStateFlow(VideoPlayerState.Starting)
        helper.play(videoURL)
        return videoPlayerStateFlow as Flow<VideoPlayerState>
    }

    override fun stopVideo() {
        helper.stop()
    }
}