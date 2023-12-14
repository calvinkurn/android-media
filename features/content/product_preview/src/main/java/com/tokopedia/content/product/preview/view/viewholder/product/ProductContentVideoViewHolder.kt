package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.PlayerControlView
import com.tokopedia.content.product.preview.databinding.ItemProductContentVideoBinding
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewPlayerControl
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

class ProductContentVideoViewHolder(
    private val binding: ItemProductContentVideoBinding,
    private val listener: ProductPreviewListener
) : RecyclerView.ViewHolder(binding.root) {

    private var mVideoPlayer: ProductPreviewExoPlayer? = null

    fun bind(content: ContentUiModel) {
        bindVideoPlayer(content)
    }

    private fun bindVideoPlayer(content: ContentUiModel) {
        val videoPlayer =
            mVideoPlayer ?: listener.getVideoPlayer("productContentVideo_" + content.url)
        mVideoPlayer = videoPlayer
        binding.playerProductContentVideo.player = videoPlayer.exoPlayer
        binding.playerControl.player = videoPlayer.exoPlayer
        videoPlayer.start(
            videoUrl = content.url,
            isMute = false,
            playWhenReady = false
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
        binding.apply {
            loaderVideo.show()
            if (mVideoPlayer?.exoPlayer?.currentPosition == 0L) {
                playerProductContentVideo.hide()
            }
        }
    }

    private fun hideLoading() {
        binding.apply {
            loaderVideo.hide()
            playerProductContentVideo.show()
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: ProductPreviewListener) =
            ProductContentVideoViewHolder(
                binding = ItemProductContentVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener = listener
            )
    }
}
