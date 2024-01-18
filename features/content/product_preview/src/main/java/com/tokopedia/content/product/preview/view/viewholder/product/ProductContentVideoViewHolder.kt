package com.tokopedia.content.product.preview.view.viewholder.product

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.PlayerControlView
import com.tokopedia.content.product.preview.databinding.ItemProductContentVideoBinding
import com.tokopedia.content.product.preview.utils.PRODUCT_CONTENT_VIDEO_KEY_REF
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewExoPlayer
import com.tokopedia.content.product.preview.view.components.player.ProductPreviewPlayerControl
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

class ProductContentVideoViewHolder(
    private val binding: ItemProductContentVideoBinding,
    private val listener: ProductPreviewListener
) : RecyclerView.ViewHolder(binding.root) {

    private var mVideoPlayer: ProductPreviewExoPlayer? = null
    private var mIsSelected: Boolean = false
    private var mVideoIds: String = ""

    init {
        binding.root.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {
                if (mVideoIds.isNotEmpty()) onSelected()
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

    fun bind(content: ContentUiModel) {
        bindVideoPlayer(content)

        if (content.selected) {
            onSelected()
        } else {
            onNotSelected()
        }
    }

    private fun bindVideoPlayer(content: ContentUiModel) {
        mVideoIds = PRODUCT_CONTENT_VIDEO_KEY_REF + content.url
        mVideoPlayer = listener.getVideoPlayer(mVideoIds)
        binding.playerProductContentVideo.player = mVideoPlayer?.exoPlayer
        binding.playerControl.player = mVideoPlayer?.exoPlayer

        binding.playerControl.setListener(object : ProductPreviewPlayerControl.Listener {
            override fun onScrubbing(
                view: PlayerControlView,
                currPosition: Long,
                totalDuration: Long
            ) {
                listener.onScrubbing()
                binding.videoTimeView.setCurrentPosition(currPosition)
                binding.videoTimeView.setTotalDuration(totalDuration)
                binding.videoTimeView.show()
            }

            override fun onStopScrubbing(
                view: PlayerControlView,
                currPosition: Long,
                totalDuration: Long
            ) {
                listener.onStopScrubbing()
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
        listener.resumeVideo(mVideoIds)
    }

    private fun onNotSelected() {
        mIsSelected = false
        listener.pauseVideo(mVideoIds)
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
