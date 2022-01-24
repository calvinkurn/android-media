package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.helper.glide.loadImageNoRounded
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardDataModel
import com.tokopedia.home.beranda.presentation.view.customview.TokopediaPlayView
import com.tokopedia.home.beranda.presentation.view.helper.ExoPlayerListener
import com.tokopedia.home.beranda.presentation.view.helper.HomePlayWidgetHelper
import com.tokopedia.home.beranda.presentation.view.helper.setSafeOnClickListener
import com.tokopedia.home.beranda.presentation.view.helper.setValue
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PlayCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener
): AbstractViewHolder<PlayCardDataModel>(view), ExoPlayerListener, CoroutineScope {

    private val frameLayout = view.findViewById<FrameLayout>(R.id.play_frame_layout)
    internal val container = view.findViewById<ConstraintLayout>(R.id.bannerPlay)
    private val play = view.findViewById<ImageView>(R.id.play)
    private val thumbnailView = view.findViewById<ImageView>(R.id.thumbnail_image_play)
    private val imageViewer = view.findViewById<ImageView>(R.id.image_viewer)
    private val viewer = view.findViewById<TextView>(R.id.viewer)
    private val live = view.findViewById<View>(R.id.live)
    private val titlePlay = view.findViewById<TextView>(R.id.title_play)
    private val seeAll = view.findViewById<TextView>(R.id.play_txt_see_all)
    private val broadcasterName = view.findViewById<TextView>(R.id.title_description)
    private val title = view.findViewById<TextView>(R.id.title)
    private var isClickable = false
    private val masterJob = Job()
    private var playCardDataModel: PlayCardDataModel? = null

    companion object {
        @LayoutRes val LAYOUT = R.layout.play_banner
        private const val DELAY_CLICKABLE = 1500L
    }

    private var helper: HomePlayWidgetHelper? = null

    private val videoPlayer = view.findViewById<TokopediaPlayView>(R.id.video_player)

    init {
        helper = HomePlayWidgetHelper.Builder(videoPlayer.context, videoPlayer)
                .setExoPlayerEventsListener(this)
                .create()
    }

    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.IO

    override fun bind(element: PlayCardDataModel?) {
        if(element?.playCardHome == null){
            container.hide()
        } else {
            onBind(element)
        }
    }

    override fun bind(element: PlayCardDataModel?, payloads: MutableList<Any>) {
        onBind(element)
    }

    private fun onBind(element: PlayCardDataModel?) {
        playCardDataModel = element
        playCardDataModel?.let { playCardViewModel ->
            if (container.visibility == View.GONE) container.show()
            initView(playCardViewModel)
            initAutoPlayVideo(playCardViewModel)
        }
    }

    private fun initAutoPlayVideo(playCardDataModel: PlayCardDataModel) {
        val videoStream = playCardDataModel.playCardHome?.videoStream
        if (videoStream != null) {
            helper?.isAutoPlay = videoStream.config.isAutoPlay
            if (helper?.isAutoPlay == true && videoStream.config.streamUrl.isNotEmpty()) {
                playChannel(videoStream.config.streamUrl)
            }
        }
    }

    private fun initView(model: PlayCardDataModel){
        model.playCardHome?.let{ playChannel ->
            handlingTracker(model)
            title.setValue(model.channel.name)

            if (model.channel.header.applink.isNotEmpty()) {
                seeAll.visible()
            } else {
                seeAll.gone()
            }

            thumbnailView.show()
            thumbnailView.loadImageNoRounded(playChannel.coverUrl)

            broadcasterName.text = playChannel.moderatorName
            titlePlay.text = playChannel.title

            if(playChannel.totalView.isNotEmpty() && playChannel.isShowTotalView){
                viewer.text = playChannel.totalView
                viewer.show()
                imageViewer.show()
            } else {
                viewer.hide()
                imageViewer.hide()
            }

            if(playChannel.videoStream.isLive) live.show()
            else live.hide()

            container.setSafeOnClickListener {
                goToPlayChannel(model)
            }

            itemView.setSafeOnClickListener {
                goToPlayChannel(model)
            }

            play.setSafeOnClickListener {
                goToPlayChannel(model)
            }

            seeAll.setOnClickListener {
                goToChannelList(model.channel.header.applink)
            }
        }
    }

    private fun handlingTracker(model: PlayCardDataModel){
        container.addOnImpressionListener(model){
            HomePageTracking.eventEnhanceImpressionPlayBanner(listener.getTrackingQueueObj(), model)
            listener.sendIrisTrackerHashMap(HomePageTracking.eventEnhanceImpressionIrisPlayBanner(model))
        }
    }

    private fun playChannel(url: String){
        helper?.play(url)
    }

    private fun goToPlayChannel(model: PlayCardDataModel){
        if(isClickable){
            videoPlayer?.applyZoom()
            listener.onOpenPlayActivity(frameLayout, model.playCardHome?.channelId)
            HomePageTracking.eventClickPlayBanner(model)
        }
    }

    private fun goToChannelList(appLink: String) {
        listener.onOpenPlayChannelList(appLink)
    }

    fun resume(){
        videoPlayer?.resetZoom()
        thumbnailView.show()
        helper?.onActivityResume()
    }

    fun pause(shouldPausePlay: Boolean){
        helper?.onActivityPause(shouldPausePlay)
    }

    fun getHelper() = helper

    fun onViewAttach(){
        thumbnailView.show()
        masterJob.cancelChildren()
        launch {
            delay(DELAY_CLICKABLE)
            isClickable = true
        }
        if(playCardDataModel != null && playCardDataModel?.playCardHome != null) {
            helper?.onViewAttach()
        }
    }

    fun onViewDetach(){
        thumbnailView.show()
        isClickable = false
        helper?.onViewDetach()
    }

    override fun onPlayerPlaying() {
        thumbnailView.hide()
    }

    override fun onPlayerBuffering() {
        thumbnailView.show()
    }

    override fun onPlayerPaused() {
        thumbnailView.show()
    }

    override fun onPlayerError(errorString: String?) {
        thumbnailView.show()
    }

    override fun onPlayerIdle() {
        thumbnailView.show()
    }
}
