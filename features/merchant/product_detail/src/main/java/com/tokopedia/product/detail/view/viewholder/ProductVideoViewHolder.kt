package com.tokopedia.product.detail.view.viewholder

import android.animation.Animator
import android.view.View
import android.widget.ImageView
import com.google.android.exoplayer2.Player
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant.FADE_IN_VIDEO_THUMBNAIL_DURATION
import com.tokopedia.product.detail.data.util.ProductDetailConstant.HIDE_VALUE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.SHOW_VALUE
import com.tokopedia.product.detail.databinding.PdpVideoViewHolderBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.widget.ProductExoPlayer
import com.tokopedia.product.detail.view.widget.VideoStateListener
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder

/**
 * Created by Yehezkiel on 23/11/20
 */
class ProductVideoViewHolder(val view: View,
                             private val listener: DynamicProductDetailListener?)

    : AbstractViewHolder<MediaDataModel>(view), ProductVideoReceiver {

    private var mPlayer: ProductExoPlayer? = null
    private var mVideoId: String = ""
    private var thumbnail: String = ""
    private var videoVolume: ImageView? = null
    private var videoFullScreen: ImageView? = null

    companion object {
        const val VIDEO_TYPE = "video"
        val LAYOUT = R.layout.pdp_video_view_holder
    }

    private val binding = PdpVideoViewHolderBinding.bind(view)

    init {
        videoVolume = binding.pdpMainVideo.findViewById(R.id.pdp_volume_control)
        videoFullScreen = binding.pdpMainVideo.findViewById(R.id.pdp_maximize_control)
        binding.pdpMainVideo.layoutTransition.setAnimateParentHierarchy(false)
    }

    override fun bind(data: MediaDataModel) {
        mVideoId = data.id
        thumbnail = data.urlOriginal
        listener?.getProductVideoCoordinator()?.configureVideoCoordinator(view.context, data.id, data.videoUrl)
        setThumbnail()
        videoVolume?.setOnClickListener {
            listener?.onVideoVolumeCLicked(mPlayer?.isMute() != true)
            listener?.getProductVideoCoordinator()?.configureVolume(mPlayer?.isMute() != true, data.id)
        }
        videoFullScreen?.setOnClickListener {
            listener?.onVideoFullScreenClicked()
        }
    }

    private fun setThumbnail() = with(binding) {
        pdpVideoOverlay.loadImageWithoutPlaceholder(thumbnail)
        pdpVideoOverlay.alpha = SHOW_VALUE
        pdpVideoOverlay.show()
    }

    private fun removeThumbnail() = with(binding) {
        pdpVideoOverlay.animate().alpha(HIDE_VALUE).setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                pdpVideoOverlay.hide()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }

        }).duration = FADE_IN_VIDEO_THUMBNAIL_DURATION
    }

    private fun showBufferLoading() = with(binding) {
        pdpVideoLoading.animate()
        pdpVideoLoading.show()
    }

    private fun hideBufferLoading() = with(binding) {
        pdpVideoLoading.hide()
    }

    private fun setupVolume(isMute: Boolean) {
        videoVolume?.setImageResource(if (!isMute) R.drawable.ic_pdp_volume_up else R.drawable.ic_pdp_volume_mute)
    }

    override fun setPlayer(player: ProductExoPlayer?) = with(binding) {
        mPlayer = player
        if (player == null) {
            pdpMainVideo.player = null
            pdpMainVideo.gone()
            setThumbnail()
        } else {
            if (player.getExoPlayer().playbackState == Player.STATE_READY) removeThumbnail()
            player.setVideoStateListener(object : VideoStateListener {
                override fun onInitialStateLoading() {
                    showBufferLoading()
                }

                override fun onVideoReadyToPlay() {
                    removeThumbnail()
                    hideBufferLoading()
                    pdpMainVideo.useController = true
                }

                override fun onVideoBuffering() {
                    showBufferLoading()
                }

                override fun configureVolume(isMute: Boolean) {
                    setupVolume(isMute)
                }

                override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
                    listener?.onVideoStateChange(stopDuration, videoDuration)
                }
            })
            pdpMainVideo.show()
            if (pdpMainVideo.player == null) {
                pdpMainVideo.player = player.getExoPlayer()
            }
        }
    }

    override fun getPlayer(): ProductExoPlayer? = mPlayer

    override fun getVideoId(): String = mVideoId
}

interface ProductVideoReceiver {
    fun setPlayer(player: ProductExoPlayer?)

    fun getPlayer(): ProductExoPlayer?

    fun getVideoId(): String
}
