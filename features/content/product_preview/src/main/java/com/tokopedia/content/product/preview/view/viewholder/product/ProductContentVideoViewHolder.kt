package com.tokopedia.content.product.preview.view.viewholder.product

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.exoplayer2.ui.PlayerControlView
import com.tokopedia.content.product.preview.databinding.ItemProductContentVideoBinding
import com.tokopedia.content.product.preview.view.components.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.ProductPreviewPlayerControl
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.model.ProductVideoModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

class ProductContentVideoViewHolder(
    private val binding: ItemProductContentVideoBinding,
    private val listener: ProductPreviewListener,
) : ViewHolder(binding.root) {

    private var mVideoPlayer: ProductPreviewExoPlayer? = null

    fun bind(element: ProductVideoModel) {
        bindVideoPlayer(element)
    }

    private fun bindVideoPlayer(element: ProductVideoModel) {
        val videoPlayer = mVideoPlayer ?: listener.getVideoPlayer(element.id)
        mVideoPlayer = videoPlayer
        binding.playerProductContentVideo.player = videoPlayer.exoPlayer
        binding.playerControl.player = videoPlayer.exoPlayer
        videoPlayer.start(
            videoUrl = element.videoUrl,
            isMute = false,
            playWhenReady = false,
        )

        binding.playerControl.setListener(object : ProductPreviewPlayerControl.Listener {
            override fun onScrubbing(
                view: PlayerControlView,
                currPosition: Long,
                totalDuration: Long
            ) {
                binding.videoTimeView.setCurrentPosition(currPosition)
                binding.videoTimeView.setTotalDuration(totalDuration)
                binding.videoTimeView.show()
            }

            override fun onStopScrubbing(
                view: PlayerControlView,
                currPosition: Long,
                totalDuration: Long
            ) {
                binding.videoTimeView.hide()
            }

        })
        videoPlayer.setVideoListener(object : ProductPreviewExoPlayer.VideoStateListener {
            override fun onBuffering() {
                showLoading()
                binding.iconPlay.hide()
            }

            override fun onVideoReadyToPlay(isPlaying: Boolean) {
                hideLoading()
                binding.iconPlay.showWithCondition(!isPlaying)
            }
        })
    }

    private fun showLoading() {
        binding.loaderFeedVideo.show()
        if (mVideoPlayer?.exoPlayer?.currentPosition == 0L) {
            binding.playerProductContentVideo.hide()
        }
    }

    private fun hideLoading() {
        binding.loaderFeedVideo.hide()
        binding.playerProductContentVideo.show()
    }

}
