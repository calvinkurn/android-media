package com.tokopedia.product.detail.view.viewholder

import android.animation.Animator
import android.view.View
import android.widget.ImageView
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.video.VideoListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.widget.ProductExoPlayer
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import com.tokopedia.product.detail.view.widget.ProductVideoDataModel
import com.tokopedia.product.detail.view.widget.VideoStateListener
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder
import kotlinx.android.synthetic.main.pdp_video_view_holder.view.*

/**
 * Created by Yehezkiel on 01/12/20
 */
class ProductVideoDetailViewHolder(val view: View, private val productVideoCoordinator: ProductVideoCoordinator?)
    : AbstractViewHolder<ProductVideoDataModel>(view), ProductVideoReceiver {

    private var mPlayer: ProductExoPlayer? = null
    private var mVideoId: String = ""
    private var thumbnail: String = ""
    private var video_volume: ImageView? = null
    private val listener: VideoListener = object : VideoListener {
        override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
            val data = height.toFloat() / width.toFloat()
            if (data > 1.7.toFloat()) {
                view.pdp_main_video?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            } else {
                view.pdp_main_video?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }

            mPlayer?.getExoPlayer()?.removeVideoListener(this)
        }
    }

    companion object {
        const val VIDEO_TYPE = "video"
        val LAYOUT = R.layout.pdp_video_detail_view_holder
    }

    init {
        video_volume = view.pdp_main_video.findViewById(R.id.pdp_volume_control)
    }

    override fun bind(data: ProductVideoDataModel) {
        mVideoId = data.videoId
        thumbnail = data.videoUrl
        setThumbnail()
        productVideoCoordinator?.configureVideoCoordinator(view.context, data.videoId, data.videoUrl)

        setupVolume()
        video_volume?.setOnClickListener {
            setupVolume()
            productVideoCoordinator?.configureVolume(mPlayer?.isMute() != true, data.videoId)
        }
    }

    private fun setThumbnail() = with(view) {
        pdp_video_overlay.loadImage(thumbnail)
        pdp_video_overlay.alpha = 1F
        pdp_video_overlay.show()
    }

    private fun removeThumbnail() = with(view) {
        pdp_video_overlay.animate().alpha(0F).setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                pdp_video_overlay.hide()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        }).duration = 200
    }

    private fun showBufferLoading() = with(view) {
        pdp_video_loading?.animate()
        pdp_video_loading?.show()
    }

    private fun hideBufferLoading() = with(view) {
        pdp_video_loading?.hide()
    }

    private fun setupVolume() {
        video_volume?.setImageResource(if (mPlayer?.isMute() == true) R.drawable.ic_pdp_volume_up else R.drawable.ic_pdp_volume_mute)
    }

    override fun setPlayer(player: ProductExoPlayer?) = with(view) {
        mPlayer = player
        if (player == null) {
            pdp_main_video.player = null
            pdp_main_video.gone()
            setThumbnail()
        } else {
            player.setVideoResizeListener(listener)
            player.setVideoStateListener(object : VideoStateListener {
                override fun onInitialStateLoading() {
                    showBufferLoading()
                }

                override fun onVideoReadyToPlay() {
                    removeThumbnail()
                    hideBufferLoading()
                }

                override fun onVideoBuffering() {
                    showBufferLoading()
                }
            })
            pdp_main_video.show()
            pdp_main_video.player = player.getExoPlayer()
        }
    }

    override fun getPlayer(): ProductExoPlayer? = mPlayer

    override fun getVideoId(): String = mVideoId
}