package com.tokopedia.feedcomponent.view.adapter.viewholder.post.video

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedcomponent.view.widget.VideoStateListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by kenny.hadisaputra on 29/06/22
 */
class CarouselVideoViewHolder(
    itemView: View,
    private val listener: Listener,
) : BaseViewHolder(itemView) {

    private var videoPlayer: FeedExoPlayer? = null

    private val playButtonVideo = itemView.findViewById<ImageUnify>(R.id.ic_play)
    private val frameVideo = itemView.findViewById<ConstraintLayout>(R.id.frame_video)
    private val layoutVideo = itemView.findViewById<PlayerView>(R.id.layout_video)
    private val videoPreviewImage = itemView.findViewById<ImageUnify>(R.id.videoPreviewImage)
    private val llLihatProduct = itemView.findViewById<LinearLayout>(R.id.ll_lihat_product)
    private val tvLihatProduct = itemView.findViewById<TextView>(R.id.tv_lihat_product)
    private val volumeIcon = itemView.findViewById<ImageView>(R.id.volume_icon)
    private val loader = itemView.findViewById<LoaderUnify>(R.id.loader)
    private val icPlay = itemView.findViewById<ImageUnify>(R.id.ic_play)
    private val timerView = itemView.findViewById<Typography>(R.id.timer_view)

    private var mMedia = FeedXMedia()

    private var isMuted: Boolean
        set(value) {
            GridPostAdapter.isMute = value
        }
        get() = GridPostAdapter.isMute

    private val countDownTimer = object : CountDownTimer(TIME_THREE_SEC, TIME_SECOND) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            volumeIcon?.gone()
        }
    }

    init {
        itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                clearVideo()
            }
        })
    }

    fun focusMedia() {
        playVideo(mMedia)
    }

    fun removeFocus() {
        clearVideo()
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        clearVideo()
        tvLihatProduct.gone()
    }

    fun bind(item: FeedXMedia) {
        mMedia = item

        videoPreviewImage?.setImageUrl(item.coverUrl)
        llLihatProduct.showWithCondition(item.tagProducts.isNotEmpty())

        playButtonVideo?.setOnClickListener {
            playButtonVideo.gone()
            playVideo(item)
        }

        llLihatProduct?.setOnClickListener {
            listener.onLihatProductClicked(
                this,
                item,
            )
        }
        volumeIcon.setOnClickListener {
            toggleMute(item)
        }

    }

    private fun toggleMute(media: FeedXMedia) {
        isMuted = !isMuted
        videoPlayer?.toggleVideoVolume(isMuted)

        changeVolumeIcon()

        listener.onMuteChanged(this, media, isMuted)
    }

    private fun changeVolumeIcon() {
        volumeIcon.setImageResource(
            if (isMuted) R.drawable.ic_feed_volume_mute_large
            else R.drawable.ic_feed_volume_up_large
        )
    }

    private fun showVideoLoading() {
        loader.animate()
        loader.visible()
        icPlay.visible()
    }

    private fun hideVideoLoading() {
        loader.gone()
        icPlay.gone()
        videoPreviewImage.gone()
    }

    private fun clearVideo() {
        videoPlayer?.pause()
        videoPlayer?.setVideoStateListener(null)
        videoPlayer?.destroy()
        videoPlayer = null
        layoutVideo.player = null

        videoPreviewImage.visible()
        icPlay.visible()
    }

    private fun createVideoStateListener(media: FeedXMedia): VideoStateListener {
        return object : VideoStateListener {
            override fun onInitialStateLoading() {
                showVideoLoading()
            }

            override fun onVideoReadyToPlay() {
                hideVideoLoading()
                timerView.visible()
                var time = videoPlayer?.getExoPlayer()?.duration.orZero() / TIME_SECOND
                object : CountDownTimer(TIME_THREE_SEC, TIME_SECOND) {
                    override fun onTick(millisUntilFinished: Long) {
                        time -= 1
                        timerView.text =
                            String.format(
                                "%02d:%02d",
                                (time / MINUTE_IN_HOUR) % MINUTE_IN_HOUR,
                                time % MINUTE_IN_HOUR
                            )
                    }

                    override fun onFinish() {
                        timerView.gone()
                        volumeIcon.gone()
                    }
                }.start()
            }

            override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
                media.canPlay = false
                listener.onVideoStopTrack(
                    this@CarouselVideoViewHolder,
                    videoPlayer?.getExoPlayer()?.currentPosition.orZero() / TIME_SECOND
                )
            }
        }
    }

    private fun playVideo(media: FeedXMedia) {
        if (videoPlayer == null) {
            videoPlayer = FeedExoPlayer(itemView.context)
            layoutVideo.player = videoPlayer?.getExoPlayer()
            layoutVideo.videoSurfaceView?.setOnClickListener {
                toggleMute(media)
                runAutoHideMute()
                listener.onVideoSurfaceTapped(this, media, isMuted)
            }
            videoPlayer?.toggleVideoVolume(isMuted)
            videoPlayer?.setVideoStateListener(createVideoStateListener(media))
        }
        media.canPlay = true

        animateLihatProduct(true)
        videoPlayer?.start(media.mediaUrl, isMuted)
        changeVolumeIcon()
    }

    private fun runAutoHideMute() {
        if (!volumeIcon.isVisible) volumeIcon.visible()

        countDownTimer.cancel()
        countDownTimer.start()
    }

    private fun animateLihatProduct(shouldShow: Boolean) {
        TransitionManager.beginDelayedTransition(
            llLihatProduct,
            AutoTransition()
                .setDuration(ANIMATION_LIHAT_PRODUCT_DURATION)
        )

        tvLihatProduct.showWithCondition(shouldShow)
    }

    companion object {
        private const val TIME_THREE_SEC = 3000L
        private const val TIME_SECOND = 1000L
        private const val MINUTE_IN_HOUR = 60

        private const val ANIMATION_LIHAT_PRODUCT_DURATION = 250L

        fun create(
            parent: ViewGroup,
            listener: Listener,
        ) = CarouselVideoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_post_video_new,
                    parent,
                    false,
                ),
            listener,
        )
    }

    interface Listener {
        fun onLihatProductClicked(viewHolder: CarouselVideoViewHolder, media: FeedXMedia)
        fun onVideoStopTrack(viewHolder: CarouselVideoViewHolder, lastPosition: Long)
        fun onMuteChanged(viewHolder: CarouselVideoViewHolder, media: FeedXMedia, isMuted: Boolean)
        fun onVideoSurfaceTapped(viewHolder: CarouselVideoViewHolder, media: FeedXMedia, isMuted: Boolean)
    }
}
