package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipedetail.constant.MediaType
import com.tokopedia.tokopedianow.common.activity.TokoNowMediaGalleryActivity
import com.tokopedia.tokopedianow.common.model.MediaGalleryUiModel
import com.tokopedia.tokopedianow.common.model.MediaItemUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl

class MediaSliderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val INDICATOR_START_INDEX = 0
        private const val SLIDE_TO_SHOW = 1f
    }

    private var mediaCarousel: CarouselUnify? = null
    private var pageControl: PageControl? = null

    init {
        val view = View.inflate(context, R.layout.layout_recipe_media_slider, this)
        mediaCarousel = view.findViewById(R.id.media_carousel)
        pageControl = view.findViewById(R.id.page_control)
    }

    fun init(items: List<MediaItemUiModel>) {
        setupCarousel(items)
        setupIndicator(items)
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
                    val position = mediaList.indexOf(media)

                    renderImage(position, mediaList)
                    renderPlayBtn(position, mediaList)
                    renderFullScreenBtn(position, mediaList)
                }
            }

            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    pageControl?.setCurrentIndicator(current)
                }
            }
        }
    }

    private fun View.renderImage(position: Int, mediaList: List<MediaItemUiModel>) {
        findViewById<ImageUnify>(R.id.image_view).apply {
            val media = mediaList[position]
            loadImage(media.thumbnailUrl)

            setOnClickListener {
                openMediaGallery(position, mediaList)
            }
        }
    }

    private fun View.renderPlayBtn(position: Int, mediaList: List<MediaItemUiModel>) {
        val media = mediaList[position]
        val isVideo = media.type == MediaType.VIDEO
        val btnBg = findViewById<ImageUnify>(R.id.play_button_bg)
        val playBtn = findViewById<ImageUnify>(R.id.play_button)

        if (isVideo) {
            playBtn.setOnClickListener {
                openMediaGallery(position, mediaList)
            }
        }

        btnBg?.showWithCondition(isVideo)
        playBtn?.showWithCondition(isVideo)
    }

    private fun View.renderFullScreenBtn(position: Int, mediaList: List<MediaItemUiModel>) {
        val media = mediaList[position]
        val isVideo = media.type == MediaType.VIDEO
        val btnBg = findViewById<ImageUnify>(R.id.fullscreen_button_bg)
        val fullScreenBtn = findViewById<IconUnify>(R.id.fullscreen_btn)

        if (isVideo) {
            fullScreenBtn?.setOnClickListener {
                openMediaGallery(position, mediaList)
            }
        }

        btnBg?.showWithCondition(isVideo)
        fullScreenBtn?.showWithCondition(isVideo)
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