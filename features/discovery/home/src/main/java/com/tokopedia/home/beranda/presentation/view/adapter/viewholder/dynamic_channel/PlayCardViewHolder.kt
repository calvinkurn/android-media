package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.dynamicbanner.entity.PlayCard
import com.tokopedia.dynamicbanner.entity.PlayCardHome
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.VideoPlayerListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.view.helper.ExoPlayerListener
import com.tokopedia.home.beranda.presentation.view.helper.ExoThumbListener
import com.tokopedia.home.beranda.presentation.view.helper.TokopediaPlayerHelper
import com.tokopedia.kotlin.extensions.view.showWithCondition
import timber.log.Timber


class PlayCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener,
        val videoPlayerListener: VideoPlayerListener
): AbstractViewHolder<PlayCardViewModel>(view), TokopediaPlayer, ExoPlayerListener, ExoThumbListener {

    internal val container = view.findViewById<ConstraintLayout>(R.id.bannerPlay)
    private val imgBanner = view.findViewById<ImageView>(R.id.imgBanner)
    private val chipPlayLive = view.findViewById<LinearLayout>(R.id.chipPlayLive)
    private val chipPlayViewers = view.findViewById<LinearLayout>(R.id.chipPlayViewers)
    private val txtTotalViewers = view.findViewById<TextView>(R.id.txtTotalViewers)
    private var helper: TokopediaPlayerHelper? = null

    var mThumbUrl: String = ""
    var mVideoUrl: String = ""

    val videoPlayer = view.findViewById<PlayerView>(R.id.video_player)

    private var playCardHome: PlayCardHome? = null
    private val progressBar: ProgressBar? = view.findViewById(R.id.progressBar)



    override fun bind(element: PlayCardViewModel) {

    }

    override fun reset() {
        videoPlayerListener.getPlayer().release()
    }

    override fun play() {
        if(videoPlayer.player == null) videoPlayer.player = videoPlayerListener.getPlayer()
        videoPlayer.player.playWhenReady = true
    }

    override fun pause() {
        videoPlayerListener.getPlayer().release()
    }

    override fun release() {
        videoPlayer.player.release()
    }

    fun createHelper() {
        helper = TokopediaPlayerHelper.Builder(view.context, videoPlayer)
                .setAutoPlayOn(true)
                .setToPrepareOnResume(false)
                .setVideoUrls(mVideoUrl)
                .setExoPlayerEventsListener(this)
                .setThumbImageViewEnabled(this)
                .addProgressBarWithColor(Color.RED)
                .create()
    }

    override fun onLoadingStatusChanged(isLoading: Boolean, bufferedPosition: Long, bufferedPercentage: Int) {
        
    }

    override fun onPlayerPlaying(currentWindowIndex: Int) {
        
    }

    override fun onPlayerPaused(currentWindowIndex: Int) {
        
    }

    override fun onPlayerBuffering(currentWindowIndex: Int) {
        
    }

    override fun onPlayerStateEnded(currentWindowIndex: Int) {
        
    }

    override fun onPlayerStateIdle(currentWindowIndex: Int) {
        
    }

    override fun onPlayerError(errorString: String?) {
        
    }

    override fun createExoPlayerCalled(isToPrepare: Boolean) {
        
    }

    override fun releaseExoPlayerCalled() {
        
    }

    override fun onVideoResumeDataLoaded(window: Int, position: Long, isResumeWhenReady: Boolean) {
        
    }

    override fun onTracksChanged(currentWindowIndex: Int, nextWindowIndex: Int, isPlayBackStateReady: Boolean) {
        
    }

    override fun onMuteStateChanged(isMuted: Boolean) {
        
    }

    override fun onVideoTapped() {
    }

    override fun onPlayBtnTap(): Boolean {
        return false
    }

    override fun onPauseBtnTap(): Boolean {
        return false
    }

    override fun onFullScreenBtnTap() {
        
    }

    override fun onThumbImageViewReady(imageView: ImageView?) {
        imageView?.let { Glide.with(itemView.context).load(mThumbUrl).into(it) }
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

interface TokopediaPlayer{
    fun play()
    fun pause()
    fun reset()
    fun release()
}