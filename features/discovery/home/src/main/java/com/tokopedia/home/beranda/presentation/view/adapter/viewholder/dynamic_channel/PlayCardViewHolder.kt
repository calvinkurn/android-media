package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.dynamicbanner.entity.PlayCard
import com.tokopedia.dynamicbanner.entity.PlayCardHome
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.kotlin.extensions.view.showWithCondition

class PlayCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener
): AbstractViewHolder<PlayCardViewModel>(view) {

    private val imgBanner = view.findViewById<ImageView>(R.id.imgBanner)
    private val chipPlayLive = view.findViewById<LinearLayout>(R.id.chipPlayLive)
    private val chipPlayViewers = view.findViewById<LinearLayout>(R.id.chipPlayViewers)
    private val txtTotalViewers = view.findViewById<TextView>(R.id.txtTotalViewers)
    private val videoPlayer = view.findViewById<PlayerView>(R.id.playerView)

    private var playCardHome: PlayCardHome? = null

    override fun bind(element: PlayCardViewModel) {
        element.getPlayCardHome()?.let { viewModel ->
            this.playCardHome = viewModel //flag to preventing re-hit

            bindCard(viewModel.playGetCardHome.data.card)

            //impression tracker
            if(element.getChannel() != null) {
                HomePageTracking.eventEnhanceImpressionPlayBanner(view.context, element.getChannel())
            }
            itemView.setOnClickListener {
                val appLink = viewModel.playGetCardHome.data.card.applink
                with(view.context) {
                    //event click tracker
                    HomePageTracking.eventClickPlayBanner(this, element.getChannel())

                    //start applink
                    startActivity(RouteManager.getIntent(this, appLink))
                }
            }
        }

        if (playCardHome == null) {
            listener.onGetPlayBanner(adapterPosition)
        }
    }

    private fun bindCard(card: PlayCard) {
        chipLive(card)
        chipViewers(card)
        loadVideo("https://www.w3schools.com/html/mov_bbb.mp4", object : PlayerStateCallback{
            override fun onVideoDurationRetrieved(duration: Long, player: Player) {

            }

            override fun onVideoBuffering(player: Player) {

            }

            override fun onStartedPlaying(player: Player) {

            }

            override fun onFinishedPlaying(player: Player) {
                player.stop()
            }
        })
    }

    private fun loadVideo(url: String, callback: PlayerStateCallback){
        if (url == null) return
        val player = ExoPlayerFactory.newSimpleInstance(
                itemView.context, DefaultRenderersFactory(itemView.context), DefaultTrackSelector(),
                DefaultLoadControl()
        )

        player.playWhenReady = true
        player.repeatMode = Player.REPEAT_MODE_ALL
        // When changing track, retain the latest frame instead of showing a black screen
        this.videoPlayer.setKeepContentOnPlayerReset(true)
        // We'll show the controller
        this.videoPlayer.useController = true
        // Provide url to load the video from here
        val mediaSource = ExtractorMediaSource.Factory(
                DefaultHttpDataSourceFactory("exoplayer-codelab")
        ).createMediaSource(Uri.parse(url))

        player.prepare(mediaSource)

        this.videoPlayer.player = player

        this.videoPlayer.player.addListener(object : Player.EventListener {

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

                if (playbackState == Player.STATE_BUFFERING) callback.onVideoBuffering(player) // Buffering.. set progress bar visible here
                if (playbackState == Player.STATE_READY){
                    // [PlayerView] has fetched the video duration so this is the block to hide the buffering progress bar
                    callback.onVideoDurationRetrieved(videoPlayer.player.duration, player)
                }
                if (playbackState == Player.STATE_READY && player.playWhenReady){
                    // [PlayerView] has started playing/resumed the video
                    callback.onStartedPlaying(player)
                }
            }
        })
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
        @LayoutRes val LAYOUT = R.layout.item_home_play_card
    }

}

interface PlayerStateCallback {
    /**
     * Callback to when the [PlayerView] has fetched the duration of video
     **/
    fun onVideoDurationRetrieved(duration: Long, player: Player)

    fun onVideoBuffering(player: Player)

    fun onStartedPlaying(player: Player)

    fun onFinishedPlaying(player: Player)
}