package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.dynamicbanner.entity.PlayCard
import com.tokopedia.dynamicbanner.entity.PlayCardHome
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.ExoListener
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.VideoPlayerListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.player.TokopediaPlayManager
import timber.log.Timber

class PlayCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener,
        val videoPlayerListener: VideoPlayerListener
): AbstractViewHolder<PlayCardViewModel>(view) {

    internal val container = view.findViewById<ConstraintLayout>(R.id.bannerPlay)
    private val imgBanner = view.findViewById<ImageView>(R.id.imgBanner)
    private val chipPlayLive = view.findViewById<LinearLayout>(R.id.chipPlayLive)
    private val chipPlayViewers = view.findViewById<LinearLayout>(R.id.chipPlayViewers)
    private val txtTotalViewers = view.findViewById<TextView>(R.id.txtTotalViewers)

    val videoPlayer = view.findViewById<PlayerView>(R.id.video_player)

    private var playCardHome: PlayCardHome? = null
    private val progressBar: ProgressBar? = view.findViewById(R.id.progressBar)

    override fun bind(element: PlayCardViewModel) {
        if(videoPlayer.player == null) {
            videoPlayer.player = videoPlayerListener.getPlayer()
            if(element.url.isEmpty()) videoPlayer.hide()
            else videoPlayer.hide()
            videoPlayerListener.addListener(object : ExoListener {
                override fun onBuffering() {
                    Timber.tag(PlayCardViewHolder::class.java.name).i("onPlayerStateChanged: Buffering video.")
                    progressBar?.show()
                }

                override fun stopVideo() {
                    Timber.tag(PlayCardViewHolder::class.java.name).i("onPlayerStateChanged: Video ended.")
                    videoPlayer.player.playWhenReady = false
                    videoPlayer.player.playbackState
                }

                override fun playVideo() {
                    Timber.tag(PlayCardViewHolder::class.java.name).i("onPlayerStateChanged: Ready to play.")
                    progressBar?.hide()
                    videoPlayer.player.playWhenReady = true
                    videoPlayer.player.playbackState
                }

                override fun resetVideo() {
                    videoPlayerListener.getPlayer().seekTo(0)
                }
            })
            videoPlayerListener.playVideo(element.url)
        }
    }

    override fun bind(element: PlayCardViewModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && (payloads.first() as Bundle).containsKey("play")){
            val isPlay = (payloads.first() as Bundle).getBoolean("play")
            if(videoPlayer.player == null) {
                if(isPlay) videoPlayerListener.playVideo(element.url)
                else videoPlayerListener.getPlayer().stop()
            }
        }
    }

    private fun bindCard(card: PlayCard) {
        chipLive(card)
        chipViewers(card)
        ImageHandler.loadImageRounded2(view.context, imgBanner, card.imageUrl, ROUNDED_RADIUS)
    }

    private fun chipLive(card: PlayCard) {
        chipPlayLive.showWithCondition(card.isShowLive)
    }

    private fun chipViewers(card: PlayCard) {
        chipPlayViewers.showWithCondition(card.isShowTotalView)
        txtTotalViewers.text = card.totalView
    }

    companion object {
        const val ROUNDED_RADIUS = 20f
        @LayoutRes val LAYOUT = R.layout.play_banner
    }

}