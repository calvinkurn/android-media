package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.PlayerControlView
import com.tokopedia.content.product.preview.databinding.ItemVideoProductPreviewBinding
import com.tokopedia.content.product.preview.utils.PRODUCT_CONTENT_VIDEO_KEY_REF
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewPlayerControl
import com.tokopedia.content.product.preview.view.listener.ProductPreviewVideoListener
import com.tokopedia.content.product.preview.view.uimodel.product.ProductMediaUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

class ProductMediaVideoViewHolder(
    private val binding: ItemVideoProductPreviewBinding,
    private val productPreviewVideoListener: ProductPreviewVideoListener
) : RecyclerView.ViewHolder(binding.root) {

    private var mVideoPlayer: ProductPreviewExoPlayer? = null
    private var mIsSelected: Boolean = false
    private var mVideoId: String = ""

    init {
        binding.videoProductContainer.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {
                if (mVideoId.isEmpty()) return
                onSelected()
                productPreviewVideoListener.onImpressedVideo()
            }

            override fun onViewDetachedFromWindow(p0: View) {
                onNotSelected()
            }
        })
        binding.videoProductContainer.setOnClickListener {
            val state = !mVideoPlayer?.exoPlayer?.playWhenReady.orFalse()
            mVideoPlayer?.exoPlayer?.playWhenReady = state
        }
    }

    fun bind(content: ProductMediaUiModel) {
        bindVideoPlayer(content)
    }

    private fun bindVideoPlayer(content: ProductMediaUiModel) {
        mVideoId = String.format(PRODUCT_CONTENT_VIDEO_KEY_REF, content.url)
        mVideoPlayer = productPreviewVideoListener.getVideoPlayer(mVideoId)
        binding.playerVideo.player = mVideoPlayer?.exoPlayer
        binding.playerControl.player = mVideoPlayer?.exoPlayer

        binding.playerControl.setListener(object : ProductPreviewPlayerControl.Listener {
            override fun onScrubbing(
                view: PlayerControlView,
                currPosition: Long,
                totalDuration: Long
            ) {
                productPreviewVideoListener.onScrubbing()
                binding.videoTimeView.setCurrentPosition(currPosition)
                binding.videoTimeView.setTotalDuration(totalDuration)
                binding.videoTimeView.show()
            }

            override fun onStopScrubbing(
                view: PlayerControlView,
                currPosition: Long,
                totalDuration: Long
            ) {
                productPreviewVideoListener.onStopScrubbing()
                binding.videoTimeView.hide()
            }
        })
        mVideoPlayer?.setVideoListener(object : ProductPreviewExoPlayer.VideoStateListener {
            override fun onBuffering() {
                showLoading()
                binding.iconPlay.hide()
            }

            override fun onVideoReadyToPlay(isPlaying: Boolean) {
                hideLoading()
                productPreviewVideoListener.onPauseResumeVideo()
                binding.iconPlay.showWithCondition(!isPlaying)
            }

            override fun onVideoEnded() {
                binding.iconPlay.show()
                productPreviewVideoListener.onVideoEnded()
            }
        })
    }

    private fun onSelected() {
        mIsSelected = true
        productPreviewVideoListener.resumeVideo(mVideoId)
    }

    private fun onNotSelected() {
        mIsSelected = false
        productPreviewVideoListener.pauseVideo(mVideoId)
    }

    private fun showLoading() {
        binding.apply {
            loaderVideo.show()
            if (mVideoPlayer?.exoPlayer?.currentPosition == 0L) {
                playerVideo.hide()
            }
        }
    }

    private fun hideLoading() {
        binding.apply {
            loaderVideo.hide()
            playerVideo.show()
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: ProductPreviewVideoListener) =
            ProductMediaVideoViewHolder(
                binding = ItemVideoProductPreviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                productPreviewVideoListener = listener
            )
    }
}
