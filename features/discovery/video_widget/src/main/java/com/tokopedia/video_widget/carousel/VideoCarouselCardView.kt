package com.tokopedia.video_widget.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.R

class VideoCarouselCardView : BaseCustomView, CarouselVideoPlayerReceiver {
    private val videoView: PlayerView by lazy {
        findViewById(R.id.video_view)
    }
    private val videoThumbnailImageView: ImageUnify by lazy {
        findViewById(R.id.video_thumbnail_imageview)
    }
    private val titleTextView: Typography by lazy {
        findViewById(R.id.title_textview)
    }
    private val subTitleTextView: Typography by lazy {
        findViewById(R.id.subtitle_textview)
    }
    private val playButton: ImageUnify by lazy {
        findViewById(R.id.play_button_imageview)
    }

    private val playerListener = object : CarouselVideoPlayer.VideoPlayerListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) videoView.visible() else videoView.gone()
        }
    }

    private var player: CarouselVideoPlayer? = null
    private var model: VideoCarouselItemModel? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.video_carousel_card_view, this)
    }

    fun setVideoCarouselItemModel(videoItem: VideoCarouselItemModel) {
        this.model = videoItem
        videoThumbnailImageView.loadImage(videoItem.imageURL)
        titleTextView.text = videoItem.title
        renderSubTitle(videoItem.subTitle)
    }

    private fun renderSubTitle(subTitle: String) {
        subTitleTextView.apply {
            if (subTitle.isEmpty()) {
                gone()
            } else {
                text = subTitle
                show()
            }
        }
    }

    private fun renderPlayButton() {
        if (DeviceConnectionInfo.isConnectWifi(context)) {
            hidePlayButton()
        } else {
            showPlayButton()
        }
    }

    fun showPlayButton() {
        playButton.show()
    }

    fun hidePlayButton() {
        playButton.hide()
    }

    fun recycle() {

    }

    override fun setPlayer(player: CarouselVideoPlayer?) {
        renderPlayButton()
        this.player?.listener = null
        this.player = player
        videoView.player = player?.getPlayer()
        if (player == null) {
            videoView.gone()
        } else {
            model?.let {
                player.videoUrl = it.videoURL
                player.shouldCache = true
                player.start()
            }
            player.listener = playerListener
        }
    }

    override fun getPlayer(): CarouselVideoPlayer? {
        return player
    }

    override fun isPlayable(): Boolean {
        return model?.videoURL?.isNotBlank() == true
    }
}