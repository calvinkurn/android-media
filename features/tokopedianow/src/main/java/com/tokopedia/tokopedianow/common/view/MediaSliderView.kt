package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipedetail.constant.MediaType
import com.tokopedia.tokopedianow.common.activity.TokoNowMediaGalleryActivity
import com.tokopedia.tokopedianow.common.analytics.MediaSliderAnalytics
import com.tokopedia.tokopedianow.common.model.MediaGalleryUiModel
import com.tokopedia.tokopedianow.common.model.MediaItemUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.videoplayer.view.widget.VideoPlayerView
import java.util.concurrent.TimeUnit

class MediaSliderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val INDICATOR_START_INDEX = 0
        private const val SLIDE_TO_SHOW = 1f
        private const val SEEK_TO_MS = 500
    }

    private var mediaCarousel: CarouselUnify? = null
    private var pageControl: PageControl? = null
    private var analytics: MediaSliderAnalytics? = null

    init {
        val view = View.inflate(context, R.layout.layout_recipe_media_slider, this)
        mediaCarousel = view.findViewById(R.id.media_carousel)
        pageControl = view.findViewById(R.id.page_control)
    }

    fun init(items: List<MediaItemUiModel>) {
        setupCarousel(items)
        setupIndicator(items)
    }

    fun setAnalytics(analytics: MediaSliderAnalytics?) {
        this.analytics = analytics
    }

    private fun setupCarousel(mediaList: List<MediaItemUiModel>) {
        mediaCarousel?.apply {
            slideToShow = SLIDE_TO_SHOW
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            freeMode = false
            centerMode = true
            autoplay = false

            addItems(R.layout.item_tokopedianow_media_view, ArrayList(mediaList)) { view, data ->
                with(view) {
                    val media = data as MediaItemUiModel
                    val index = mediaList.indexOf(media)

                    if(media.type == MediaType.VIDEO) {
                        renderVideo(index, mediaList)
                    } else {
                        renderImage(index, mediaList)
                    }
                }
            }

            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    pageControl?.setCurrentIndicator(current)
                    trackMediaImpression(mediaList[current])
                }
            }
        }
    }

    private fun trackMediaImpression(media: MediaItemUiModel) {
        val position = media.position

        when(media.type) {
            MediaType.IMAGE -> analytics?.trackImageImpression(position)
            MediaType.VIDEO -> analytics?.trackVideoImpression(position)
        }
    }

    private fun View.renderVideo(index: Int, mediaList: List<MediaItemUiModel>) {
        showShimmer()
        val media = mediaList[index]

        findViewById<VideoPlayerView>(R.id.video_player).apply {
            setVideoURI(Uri.parse(media.url))
        }.setOnPreparedListener { player ->
            val durationMillis = player.duration.toLong()
            player.seekTo(SEEK_TO_MS)

            setOnClickListener {
                openMediaGallery(index, mediaList)
                analytics?.trackClickVideo(media.position)
            }

            renderFullScreenBtn(index, mediaList)
            renderPlayBtn(index, mediaList)
            renderDuration(durationMillis)
            hideShimmer()
        }

        hideImageView()
    }

    private fun View.renderDuration(durationMillis: Long) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis)
        val duration = context.getString(R.string.tokopedianow_duration_format, minutes, seconds)

        findViewById<Typography>(R.id.text_video_duration).text = duration
        findViewById<Typography>(R.id.text_video_duration).show()
        findViewById<ImageUnify>(R.id.video_duration_bg).show()
    }

    private fun View.renderImage(index: Int, mediaList: List<MediaItemUiModel>) {
        findViewById<ImageUnify>(R.id.image_view).apply {
            val media = mediaList[index]
            loadImage(media.thumbnailUrl)

            setOnClickListener {
                openMediaGallery(index, mediaList)
                analytics?.trackClickImage(media.position)
            }
        }
        hideVideoPlayer()
    }

    private fun View.hideVideoPlayer() {
        findViewById<VideoPlayerView>(R.id.video_player).hide()
    }

    private fun View.hideImageView() {
        findViewById<ImageUnify>(R.id.image_view).hide()
    }

    private fun View.showShimmer() {
        findViewById<LoaderUnify>(R.id.shimmer).show()
    }

    private fun View.hideShimmer() {
        findViewById<LoaderUnify>(R.id.shimmer).hide()
    }

    private fun View.renderPlayBtn(position: Int, mediaList: List<MediaItemUiModel>) {
        val btnBg = findViewById<ImageUnify>(R.id.play_button_bg)
        val playBtn = findViewById<ImageUnify>(R.id.play_button)

        playBtn.setOnClickListener {
            openMediaGallery(position, mediaList)
        }

        btnBg?.show()
        playBtn?.show()
    }

    private fun View.renderFullScreenBtn(position: Int, mediaList: List<MediaItemUiModel>) {
        val btnBg = findViewById<ImageUnify>(R.id.fullscreen_button_bg)
        val fullScreenBtn = findViewById<IconUnify>(R.id.fullscreen_btn)

        fullScreenBtn?.setOnClickListener {
            openMediaGallery(position, mediaList)
            analytics?.trackClickFullscreen()
        }

        btnBg?.show()
        fullScreenBtn?.show()
    }

    private fun openMediaGallery(position: Int, mediaList: List<MediaItemUiModel>) {
        val data = MediaGalleryUiModel(mediaList, position)
        val intent = TokoNowMediaGalleryActivity.createIntent(context, data)
        context.startActivity(intent)
    }

    private fun setupIndicator(mediaList: List<MediaItemUiModel>) {
        pageControl?.apply {
            setIndicator(mediaList.count())
            setCurrentIndicator(INDICATOR_START_INDEX)
            indicatorType = PageControl.CIRCLE_INDICATOR_TYPE
            indicatorColorType = PageControl.COLOR_DEFAULT
        }
    }
}