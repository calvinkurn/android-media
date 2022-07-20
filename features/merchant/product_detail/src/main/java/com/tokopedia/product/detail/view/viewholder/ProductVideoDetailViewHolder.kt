package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.video.VideoListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.PdpVideoDetailViewHolderBinding
import com.tokopedia.product.detail.view.fragment.ProductVideoDetailInterface
import com.tokopedia.product.detail.view.widget.ProductExoPlayer
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import com.tokopedia.product.detail.view.widget.ProductVideoDataModel
import com.tokopedia.product.detail.view.widget.VideoStateListener
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder

/**
 * Created by Yehezkiel on 01/12/20
 */
class ProductVideoDetailViewHolder(val view: View, private val productVideoCoordinator: ProductVideoCoordinator?,
                                   private val viewListener: ProductVideoDetailInterface)
    : AbstractViewHolder<ProductVideoDataModel>(view), ProductVideoReceiver {

    companion object {
        val LAYOUT = R.layout.pdp_video_detail_view_holder
        const val PORTRAIT_VALUE = 1.7
    }

    private val binding = PdpVideoDetailViewHolderBinding.bind(view)

    private var mPlayer: ProductExoPlayer? = null
    private var mVideoId: String = ""
    private var videoVolume: IconUnify? = null
    private var videoMinimize: IconUnify? = null
    private var videoCloseBtn: IconUnify? = null
    private val videoListener: VideoListener = object : VideoListener {
        override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
            val data = height.toFloat() / width.toFloat()
            if (data > PORTRAIT_VALUE.toFloat()) {
                binding.pdpMainVideo.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            } else {
                binding.pdpMainVideo.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }

            mPlayer?.getExoPlayer()?.removeVideoListener(this)
        }
    }

    init {
        videoVolume = binding.pdpMainVideo.findViewById(R.id.pdp_volume_control)
        videoMinimize = binding.pdpMainVideo.findViewById(R.id.pdp_mainimize_control)
        videoCloseBtn = binding.pdpMainVideo.findViewById(R.id.pdp_video_detail_close)
    }

    override fun bind(data: ProductVideoDataModel) {
        mVideoId = data.videoId
        productVideoCoordinator?.configureVideoCoordinator(view.context, data)

        videoVolume?.setOnClickListener {
            viewListener.onVolumeVideoClicked(mPlayer?.isMute() != true)
            productVideoCoordinator?.configureVolume(mPlayer?.isMute() != true, data.videoId)
        }

        videoMinimize?.setOnClickListener {
            viewListener.onMinimizeVideoClicked()
        }

        videoCloseBtn?.setOnClickListener {
            viewListener.onMinimizeVideoClicked()
        }
    }

    private fun showBufferLoading() = with(binding) {
        pdpVideoLoading.animate()
        pdpVideoLoading.show()
    }

    private fun hideBufferLoading() = with(binding) {
        pdpVideoLoading.hide()
    }

    private fun setupVolume(isMute: Boolean) {
        videoVolume?.setImage(if (!isMute) IconUnify.VOLUME_UP else IconUnify.VOLUME_MUTE)
    }

    override fun setPlayer(player: ProductExoPlayer?) = with(binding) {
        mPlayer = player
        if (player == null) {
            pdpMainVideo.player = null
            pdpMainVideo.gone()
        } else {
            player.setVideoResizeListener(videoListener)
            player.setVideoStateListener(object : VideoStateListener {
                override fun onInitialStateLoading() {
                    showBufferLoading()
                }

                override fun onVideoReadyToPlay() {
                    hideBufferLoading()
                }

                override fun onVideoBuffering() {
                    showBufferLoading()
                }

                override fun configureVolume(isMute: Boolean) {
                    setupVolume(isMute)
                }

                override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {

                }
            })
            pdpMainVideo.show()
            pdpMainVideo.player = player.getExoPlayer()
        }
    }

    override fun getPlayer(): ProductExoPlayer? = mPlayer

    override fun getVideoId(): String = mVideoId
}