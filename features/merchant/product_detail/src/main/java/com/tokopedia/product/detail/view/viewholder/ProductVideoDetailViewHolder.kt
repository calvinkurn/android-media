package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.video.VideoListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.fragment.ProductVideoDetailInterface
import com.tokopedia.product.detail.view.widget.ProductExoPlayer
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import com.tokopedia.product.detail.view.widget.ProductVideoDataModel
import com.tokopedia.product.detail.view.widget.VideoStateListener
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder
import kotlinx.android.synthetic.main.pdp_video_view_holder.view.*

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

    private var mPlayer: ProductExoPlayer? = null
    private var mVideoId: String = ""
    private var video_volume: IconUnify? = null
    private var video_minimize: IconUnify? = null
    private var video_close_btn: IconUnify? = null
    private val videoListener: VideoListener = object : VideoListener {
        override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
            val data = height.toFloat() / width.toFloat()
            if (data > PORTRAIT_VALUE.toFloat()) {
                view.pdp_main_video?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            } else {
                view.pdp_main_video?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }

            mPlayer?.getExoPlayer()?.removeVideoListener(this)
        }
    }

    init {
        video_volume = view.pdp_main_video.findViewById(R.id.pdp_volume_control)
        video_minimize = view.pdp_main_video.findViewById(R.id.pdp_mainimize_control)
        video_close_btn = view.pdp_main_video.findViewById(R.id.pdp_video_detail_close)
    }

    override fun bind(data: ProductVideoDataModel) {
        mVideoId = data.videoId
        productVideoCoordinator?.configureVideoCoordinator(view.context, data)

        video_volume?.setOnClickListener {
            viewListener.onVolumeVideoClicked(mPlayer?.isMute() != true)
            productVideoCoordinator?.configureVolume(mPlayer?.isMute() != true, data.videoId)
        }

        video_minimize?.setOnClickListener {
            viewListener.onMinimizeVideoClicked()
        }

        video_close_btn?.setOnClickListener {
            viewListener.onMinimizeVideoClicked()
        }
    }

    private fun showBufferLoading() = with(view) {
        pdp_video_loading?.animate()
        pdp_video_loading?.show()
    }

    private fun hideBufferLoading() = with(view) {
        pdp_video_loading?.hide()
    }

    private fun setupVolume(isMute: Boolean) {
        video_volume?.setImage(if (!isMute) IconUnify.VOLUME_UP else IconUnify.VOLUME_MUTE)
    }

    override fun setPlayer(player: ProductExoPlayer?) = with(view) {
        mPlayer = player
        if (player == null) {
            pdp_main_video.player = null
            pdp_main_video.gone()
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
            pdp_main_video.show()
            pdp_main_video.player = player.getExoPlayer()
        }
    }

    override fun getPlayer(): ProductExoPlayer? = mPlayer

    override fun getVideoId(): String = mVideoId
}