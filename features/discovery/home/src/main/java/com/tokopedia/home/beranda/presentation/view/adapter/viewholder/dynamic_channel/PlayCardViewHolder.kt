package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.helper.glide.loadImageWithoutPlaceholder
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.view.customview.TokopediaPlayView
import com.tokopedia.home.beranda.presentation.view.helper.ExoUtil.visibleAreaOffset
import com.tokopedia.home.beranda.presentation.view.helper.TokopediaPlayerHelper
import com.tokopedia.home.beranda.presentation.view.helper.setSafeOnClickListener
import com.tokopedia.home.beranda.presentation.view.helper.setValue
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class PlayCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener
): AbstractViewHolder<PlayCardViewModel>(view) {

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

    init {
        helper = TokopediaPlayerHelper.Builder(videoPlayer.context, videoPlayer)
                .create()
    }

    override fun bind(element: PlayCardViewModel) {
        element.getPlayCardHome()?.let {channel ->
            initView(element.getChannel()?.header?.name ?: "", channel)
            playChannel("https://vod.tokopedia.net/73a58b49941d430d949b4a8273efdc74/100779c2d405420da252cc44d4ca21b3-edef9725173feab592c030523316fc60-sd.mp4")
//            playChannel(channel.videoStream.config.streamUrl)
        }
    }

    private fun initView(titleChannel: String, playChannel: PlayChannel){
        title.setValue(titleChannel)
        description.setValue("")

        mVideoUrl = if(playChannel.videoStream.config.streamUrl.isEmpty()) "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                    else playChannel.videoStream.config.streamUrl
        mThumbUrl = playChannel.coverUrl

        if(mThumbUrl.isEmpty()) thumbnailView.loadImageWithoutPlaceholder(mThumbUrl, 350, 150, true)
        else helper?.seekToDefaultPosition()

        broadcasterName.text = playChannel.moderatorName
        titlePlay.text = playChannel.title
        viewer.text = playChannel.totalView

        if(playChannel.videoStream.isLive) live.show()
        else live.hide()

        itemView.setSafeOnClickListener {
            videoPlayer.getSurfaceView()?.let { listener.onOpenPlayActivity(it, playChannel.channelId) }
        }

        play.setSafeOnClickListener { _ ->
            videoPlayer.getSurfaceView()?.let { listener.onOpenPlayActivity(it, playChannel.channelId) }
        }
    }

    private fun playChannel(url: String){
        helper?.play(url)
    }

    fun resume(){
        helper?.onActivityResume()
    }

    fun pause(){
        helper?.onActivityPause()
    }

    fun getHelper() = helper

    companion object {
        @LayoutRes val LAYOUT = R.layout.play_banner
    }

    fun wantsToPlay() = visibleAreaOffset(videoPlayer, itemView.parent) >= 0.65

}