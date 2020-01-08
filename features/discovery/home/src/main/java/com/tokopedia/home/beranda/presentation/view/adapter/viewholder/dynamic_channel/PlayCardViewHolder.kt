package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.view.customview.TokopediaPlayView
import com.tokopedia.home.beranda.presentation.view.helper.ExoPlayerListener
import com.tokopedia.home.beranda.presentation.view.helper.ExoUtil
import com.tokopedia.home.beranda.presentation.view.helper.ExoUtil.visibleAreaOffset
import com.tokopedia.home.beranda.presentation.view.helper.TokopediaPlayerHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class PlayCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener
): AbstractViewHolder<PlayCardViewModel>(view), ExoPlayerListener {

    internal val container = view.findViewById<ConstraintLayout>(R.id.bannerPlay)
    private val volumeContainer = view.findViewById<FrameLayout>(R.id.volume)
    private val play = view.findViewById<ImageView>(R.id.play)
    private val volumeAsset = view.findViewById<ImageView>(R.id.volume_asset)
    private val errorMessage = view.findViewById<TextView>(R.id.error_message)
    private val thumbnailView = view.findViewById<ImageView>(R.id.thumbnail_image_play)
    private val viewer = view.findViewById<TextView>(R.id.viewer)
    private val live = view.findViewById<TextView>(R.id.live)
    private val titlePlay = view.findViewById<TextView>(R.id.title_play)
    private val broadcasterName = view.findViewById<TextView>(R.id.title_description)
    private val title = view.findViewById<TextView>(R.id.title)
    private val description = view.findViewById<TextView>(R.id.description)
    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

    private var helper: TokopediaPlayerHelper? = null

    private var mThumbUrl: String = ""
    private var mVideoUrl: String = ""

    private val videoPlayer = view.findViewById<TokopediaPlayView>(R.id.video_player)
    private var playCardViewModel: PlayCardViewModel ?= null

    override fun bind(element: PlayCardViewModel) {
        title.text = element.getChannel()?.header?.name ?: "Play Channel"
        description.hide()
        description.text = ""

        element.getPlayCardHome()?.let { model ->
            mVideoUrl = model.videoStream.config.streamUrl
            mThumbUrl = model.coverUrl

            createHelper()

            playCardViewModel = element

            thumbnailView.loadImage(mThumbUrl, 250, 100, true)

            broadcasterName.text = model.moderatorName
            titlePlay.text = model.title
            viewer.text = model.totalView

            if(model.videoStream.isLive) live.show()
            else live.hide()

            itemView.setOnClickListener {
                videoPlayer.getSurfaceView()?.let { listener.onOpenPlayActivity(it, "2") }
            }

            volumeContainer.setOnClickListener {
                helper?.updateVideoMuted()
                volumeAsset.setImageResource(if (helper?.isPlayerVideoMuted() == true) R.drawable.ic_volume_mute_white_24dp else R.drawable.ic_volume_up_white_24dp)
            }

            play.setOnClickListener { _ ->
                videoPlayer.getSurfaceView()?.let { listener.onOpenPlayActivity(it, "2") }
            }
        }

        if(playCardViewModel == null) {
            listener.onGetPlayBanner(adapterPosition)
        }
    }

    fun createHelper() {
        if(!ExoUtil.isDeviceHasRequirementAutoPlay(itemView.context)) return

        if(helper == null) {
            helper = TokopediaPlayerHelper.Builder(videoPlayer.context, videoPlayer)
                    .setAutoPlayOn(true)
                    .setVideoUrls(mVideoUrl)
                    .setExoPlayerEventsListener(this)
                    .setRepeatModeOn(true)
                    .setMutedVolume()
                    .setToPrepareOnResume(true)
                    .create()
            helper?.playerPlay()
        }

        if(helper?.isPlayerNull() == true){
            helper?.createPlayer(false)
        }
    }

    fun getHelper() = helper

    override fun onPlayerPlaying(currentWindowIndex: Int) {
        progressBar.hide()
        thumbnailView.hide()
        volumeContainer.show()
    }

    override fun onPlayerPaused(currentWindowIndex: Int) {
        errorMessage.hide()
    }

    override fun onPlayerBuffering(currentWindowIndex: Int) {
        progressBar.hide()
        errorMessage.hide()
    }

    override fun onPlayerError(errorString: String?) {
        progressBar.hide()
        errorMessage.text = errorString
        thumbnailView.show()
        errorMessage.show()
    }

    override fun releaseExoPlayerCalled() {
        thumbnailView.show()
        progressBar.hide()
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.play_banner
    }

    fun wantsToPlay() = visibleAreaOffset(videoPlayer, itemView.parent) >= 0.65

}