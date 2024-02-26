package com.tokopedia.content.product.preview.view.viewholder.review

import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.exoplayer2.ui.PlayerControlView
import com.tokopedia.content.product.preview.databinding.ItemVideoProductPreviewBinding
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
    private val binding: ItemVideoProductPreviewBinding,
    private val productPreviewVideoListener: ProductPreviewVideoListener
) : ViewHolder(binding.root) {

    private var mVideoPlayer: ProductPreviewExoPlayer? = null
    private var mIsSelected: Boolean = false
    private var mVideoId: String = ""

    init {
        binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {
                if (mVideoId.isEmpty()) return
                onSelected()
                productPreviewVideoListener.onImpressedVideo()
            }

            override fun onViewDetachedFromWindow(p0: View) {
                onNotSelected()
            }
        })
        val gesture = GestureDetector(
            binding.root.context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    val state = !mVideoPlayer?.exoPlayer?.playWhenReady.orFalse()
                    mVideoPlayer?.exoPlayer?.playWhenReady = state
                    return true
                }
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    productPreviewVideoListener.onDoubleTapVideo()
                    return true
                }
            }
        )
        binding.root.setOnTouchListener { _, motionEvent ->
            gesture.onTouchEvent(motionEvent)
            true
        }
    }

    fun bind(content: ReviewMediaUiModel) {
        bindVideoPlayer(content)
    }

    private fun bindVideoPlayer(content: ReviewMediaUiModel) {
        mVideoId = String.format(REVIEW_CONTENT_VIDEO_KEY_REF, content.url)
        mVideoPlayer = productPreviewVideoListener.getVideoPlayer(mVideoId)
        binding.playerVideo.player = mVideoPlayer?.exoPlayer
        binding.playerControl.player = mVideoPlayer?.exoPlayer

        videoPlayerListener()
        playerControlListener()
    }

    private fun videoPlayerListener() {
        mVideoPlayer?.setVideoListener(object : ProductPreviewExoPlayer.VideoStateListener {
            override fun onBuffering() {
                showLoading()
                binding.iconPlay.hide()
            }

            override fun onVideoReadyToPlay(isPlaying: Boolean) {
                hideLoading()
                binding.iconPlay.showWithCondition(!isPlaying)
            }

            override fun onVideoEnded() {
                binding.iconPlay.show()
                productPreviewVideoListener.onVideoEnded()
            }
        })
    }

    private fun playerControlListener() {
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
        fun create(parent: ViewGroup, productPreviewVideoListener: ProductPreviewVideoListener) =
            ReviewMediaVideoViewHolder(
                binding = ItemVideoProductPreviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                productPreviewVideoListener = productPreviewVideoListener
            )
    }
}
