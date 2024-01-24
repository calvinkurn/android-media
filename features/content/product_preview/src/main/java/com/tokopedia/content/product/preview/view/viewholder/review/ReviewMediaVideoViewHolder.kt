package com.tokopedia.content.product.preview.view.viewholder.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.exoplayer2.ui.PlayerControlView
import com.tokopedia.content.product.preview.databinding.ItemReviewContentVideoBinding
import com.tokopedia.content.product.preview.utils.REVIEW_CONTENT_VIDEO_KEY_REF
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewPlayerControl
import com.tokopedia.content.product.preview.view.listener.ProductPreviewVideoListener
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMediaUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

class ReviewMediaVideoViewHolder(
    private val binding: ItemReviewContentVideoBinding,
    private val productPreviewVideoListener: ProductPreviewVideoListener,
) : ViewHolder(binding.root) {

    private var mVideoPlayer: ProductPreviewExoPlayer? = null
    private var mIsSelected: Boolean = false
    private var mVideoId: String = ""

    init {
        binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {
                if (mVideoId.isNotEmpty()) onSelected()
            }

            override fun onViewDetachedFromWindow(p0: View) {
                onNotSelected()
            }
        })
        binding.root.setOnClickListener {
            val state = !mVideoPlayer?.exoPlayer?.playWhenReady.orFalse()
            mVideoPlayer?.exoPlayer?.playWhenReady = state
        }
    }

    fun bind(content: ReviewMediaUiModel) {
        bindVideoPlayer(content)
    }

    private fun bindVideoPlayer(content: ReviewMediaUiModel) {
        mVideoId = String.format(REVIEW_CONTENT_VIDEO_KEY_REF, content.url)
        mVideoPlayer = productPreviewVideoListener.getVideoPlayer(mVideoId)
        binding.playerReviewContentVideo.player = mVideoPlayer?.exoPlayer
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
                binding.iconPlay.showWithCondition(!isPlaying)
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
                playerReviewContentVideo.hide()
            }
        }
    }

    private fun hideLoading() {
        binding.apply {
            loaderVideo.hide()
            playerReviewContentVideo.show()
        }
    }

    companion object {
        fun create(parent: ViewGroup, productPreviewVideoListener: ProductPreviewVideoListener) =
            ReviewMediaVideoViewHolder(
                binding = ItemReviewContentVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false,
                ),
                productPreviewVideoListener = productPreviewVideoListener,
            )
    }
}
