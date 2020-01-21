package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.glide.loadImageWithoutPlaceholder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.view.customview.TokopediaPlayView
import com.tokopedia.home.beranda.presentation.view.helper.*
import com.tokopedia.home.beranda.presentation.view.helper.ExoUtil.visibleAreaOffset
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class PlayCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener
): AbstractViewHolder<PlayCardViewModel>(view), ExoPlayerListener {

    internal val container = view.findViewById<ConstraintLayout>(R.id.bannerPlay)
    private val play = view.findViewById<ImageView>(R.id.play)
    private val thumbnailView = view.findViewById<ImageView>(R.id.thumbnail_image_play)
    private val viewer = view.findViewById<TextView>(R.id.viewer)
    private val live = view.findViewById<TextView>(R.id.live)
    private val titlePlay = view.findViewById<TextView>(R.id.title_play)
    private val broadcasterName = view.findViewById<TextView>(R.id.title_description)
    private val title = view.findViewById<TextView>(R.id.title)
    private val description = view.findViewById<TextView>(R.id.description)

    private var helper: TokopediaPlayerHelper? = null

    private var mThumbUrl: String = ""
    private var mVideoUrl: String = ""

    private val videoPlayer = view.findViewById<TokopediaPlayView>(R.id.video_player)
    private var playCardViewModel: PlayCardViewModel ?= null

    override fun bind(element: PlayCardViewModel) {
        if (element.getPlayCardHome() == null) {
            itemView.hide()
            helper = null
            listener.onGetPlayBanner(adapterPosition)
        } else {
            itemView.show()
            val model = element.getPlayCardHome()
            title.setValue(element.getChannel()?.header?.name ?: "Play Channel")
            description.setValue("")
            mVideoUrl = if(model?.videoStream?.config?.streamUrl?.isEmpty() == true) "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4" else model?.videoStream?.config?.streamUrl ?: ""
//            mThumbUrl = if(model?.coverUrl?.isEmpty() == true) "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQsXmNcM4cLjmjXv-_9QJe5McOfdu6652WGC4LBq8FpirMHT9xl" else model?.coverUrl ?: ""
            mThumbUrl = ""
            createHelper()
            playCardViewModel = element

            thumbnailView.loadImageWithoutPlaceholder(mThumbUrl, 350, 150, true)

            broadcasterName.text = model?.moderatorName ?: ""
            titlePlay.text = model?.title ?: ""
            viewer.text = model?.totalView ?: ""

            if(model?.videoStream?.isLive == true) live.show()
            else live.hide()

            itemView.setSafeOnClickListener {
                videoPlayer.getSurfaceView()?.let { listener.onOpenPlayActivity(it, model?.channelId) }
            }

            play.setOnClickListener { _ ->
                videoPlayer.getSurfaceView()?.let { listener.onOpenPlayActivity(it, model?.channelId) }
            }
        }

    }

    private fun createHelper() {
        if(!ExoUtil.isDeviceHasRequirementAutoPlay(itemView.context)) return

        if(helper == null) {
            helper = TokopediaPlayerHelper.Builder(videoPlayer.context, videoPlayer)
                    .setVideoUrls(mVideoUrl)
                    .setExoPlayerEventsListener(this)
                    .create()
        }

        if(helper != null && helper!!.isPlayerNull()){
            helper?.createPlayer()
        }
        if(mThumbUrl.isEmpty()) helper?.seekToDefaultPosition()
    }

    fun resume(){
        helper?.onActivityResume()
    }

    fun pause(){
        helper?.onActivityPause()
    }

    fun getHelper() = helper

    override fun onPlayerPlaying(currentWindowIndex: Int) {
        thumbnailView.hide()
    }

    override fun onPlayerPaused(currentWindowIndex: Int) {
        helper?.seekToDefaultPosition()
    }

    override fun onPlayerBuffering(currentWindowIndex: Int) {
    }

    override fun onPlayerError(errorString: String?) {
        if(mThumbUrl.isEmpty()){
            helper?.seekToDefaultPosition()
        } else {
            thumbnailView.show()
        }
    }

    override fun releaseExoPlayerCalled() {
        if(mThumbUrl.isEmpty()){
            helper?.seekToDefaultPosition()
        } else {
            thumbnailView.show()
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.play_banner
    }

    fun wantsToPlay() = visibleAreaOffset(videoPlayer, itemView.parent) >= 0.65

}