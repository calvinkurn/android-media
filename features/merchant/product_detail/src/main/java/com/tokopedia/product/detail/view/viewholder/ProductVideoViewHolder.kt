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
import com.tokopedia.product.detail.view.widget.ProductExoPlayer
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import com.tokopedia.product.detail.view.widget.VideoStateListener
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder
import kotlinx.android.synthetic.main.pdp_video_view_holder.view.*

/**
 * Created by Yehezkiel on 23/11/20
 */
class ProductVideoViewHolder(val view: View, private val productVideoCoordinator: ProductVideoCoordinator?,
                             private val onVideoFullScreenClicked: (() -> Unit)? = null)
    : AbstractViewHolder<MediaDataModel>(view), ProductVideoReceiver {

    private var mPlayer: ProductExoPlayer? = null
    private var mVideoId: String = ""
    private var thumbnail: String = ""
    private var video_volume: ImageView? = null
    private var video_full_screen: ImageView? = null

    companion object {
        const val VIDEO_TYPE = "video"
        val LAYOUT = R.layout.pdp_video_view_holder
    }

    init {
        video_volume = view.pdp_main_video.findViewById(R.id.pdp_volume_control)
        video_full_screen = view.pdp_main_video.findViewById(R.id.pdp_maximize_control)
    }

    override fun bind(data: MediaDataModel) {
        mVideoId = data.id
        thumbnail = data.urlOriginal
        productVideoCoordinator?.configureVideoCoordinator(view.context, data.id, data.videoUrl)
        setupVolume()
        setThumbnail()
        video_volume?.setOnClickListener {
            setupVolume()
            productVideoCoordinator?.configureVolume(mPlayer?.isMute() != true, data.id)
        }
        video_full_screen?.setOnClickListener {
            onVideoFullScreenClicked?.invoke()
        }
    }

    private fun setThumbnail() = with(view) {
        pdp_video_overlay.loadImageWithoutPlaceholder(thumbnail)
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
            if (player.getExoPlayer().playbackState == Player.STATE_READY) removeThumbnail()
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
            if (pdp_main_video.player == null) {
                pdp_main_video.player = player.getExoPlayer()
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