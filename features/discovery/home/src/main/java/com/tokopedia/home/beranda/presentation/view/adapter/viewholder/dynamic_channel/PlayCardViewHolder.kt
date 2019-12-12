package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.dynamicbanner.entity.PlayCard
import com.tokopedia.dynamicbanner.entity.PlayCardHome
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.view.helper.ExoPlayerListener
import com.tokopedia.home.beranda.presentation.view.helper.ExoThumbListener
import com.tokopedia.home.beranda.presentation.view.helper.TokopediaPlayerHelper
import com.tokopedia.kotlin.extensions.view.showWithCondition
import timber.log.Timber


class PlayCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener
): AbstractViewHolder<PlayCardViewModel>(view), ExoPlayerListener, ExoThumbListener {

    internal val container = view.findViewById<ConstraintLayout>(R.id.bannerPlay)
    private val imgBanner = view.findViewById<ImageView>(R.id.imgBanner)
    private val chipPlayLive = view.findViewById<LinearLayout>(R.id.chipPlayLive)
    private val chipPlayViewers = view.findViewById<LinearLayout>(R.id.chipPlayViewers)
    private val txtTotalViewers = view.findViewById<TextView>(R.id.txtTotalViewers)
    var helper: TokopediaPlayerHelper? = null

    var mThumbUrl: String = ""
    var mVideoUrl: String = ""

    val videoPlayer = view.findViewById<PlayerView>(R.id.video_player)

    private var playCardHome: PlayCardHome? = null

    override fun bind(element: PlayCardViewModel) {
        mVideoUrl = element.url
        mThumbUrl = "https://i.ytimg.com/vi/JQ5xItF40yA/maxresdefault.jpg"
    }

    fun createHelper() {
        helper = TokopediaPlayerHelper.Builder(view.context, videoPlayer)
                .setAutoPlayOn(true)
                .setToPrepareOnResume(false)
                .setVideoUrls(mVideoUrl)
                .setExoPlayerEventsListener(this)
                .addProgressBarWithColor(Color.RED)
                .create()
    }

    override fun onLoadingStatusChanged(isLoading: Boolean, bufferedPosition: Long, bufferedPercentage: Int) {
        Timber.tag("ExoPlayer").d("On Loading status changed $isLoading, $bufferedPosition, $bufferedPercentage")
    }

    override fun onPlayerPlaying(currentWindowIndex: Int) {
        Timber.tag("ExoPlayer").d("On Player Playing $currentWindowIndex")
    }

    override fun onPlayerPaused(currentWindowIndex: Int) {
        Timber.tag("ExoPlayer").d("On Player Pause $currentWindowIndex")
    }

    override fun onPlayerBuffering(currentWindowIndex: Int) {
        Timber.tag("ExoPlayer").d("On Buffering $currentWindowIndex")
    }

    override fun onPlayerStateEnded(currentWindowIndex: Int) {
        Timber.tag("ExoPlayer").d("On State end $currentWindowIndex")
    }

    override fun onPlayerStateIdle(currentWindowIndex: Int) {
        Timber.tag("ExoPlayer").d("On Idle $currentWindowIndex")
    }

    override fun onPlayerError(errorString: String?) {
        Timber.tag("ExoPlayer").d("On Error $errorString")
    }

    override fun createExoPlayerCalled(isToPrepare: Boolean) {
        Timber.tag("ExoPlayer").d("On Create Player Called $isToPrepare")
    }

    override fun releaseExoPlayerCalled() {
        Timber.tag("ExoPlayer").d("On Release Exo")
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