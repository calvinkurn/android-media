package com.tokopedia.play_common.widget.playBannerCarousel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.R
import com.tokopedia.play_common.widget.playBannerCarousel.adapter.PlayBannerCarouselAdapter
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage
import com.tokopedia.play_common.widget.playBannerCarousel.extension.setGradientBackground
import com.tokopedia.play_common.widget.playBannerCarousel.extension.showOrHideView
import com.tokopedia.play_common.widget.playBannerCarousel.mapper.PlayBannerMapper
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.PlayBannerCarouselTypeImpl
import kotlinx.android.synthetic.main.layout_header_play_banner.view.*
import kotlinx.android.synthetic.main.layout_play_banner_carousel.view.*
import kotlin.math.abs

class PlayBannerCarousel(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr) {
    private var adapter: PlayBannerCarouselAdapter? = null
    private val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    private val adapterTypeFactory = PlayBannerCarouselTypeImpl()
    private var listener: PlayBannerCarouselViewEventListener? = null

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    init {
        LayoutInflater.from(context).inflate(LAYOUT, this)
    }

    fun setListener(listener: PlayBannerCarouselViewEventListener){
        this.listener = listener
    }

    fun setItem(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
        val list = PlayBannerMapper.mapToDataModel(playBannerCarouselDataModel)
        if(adapter == null){
            recycler_view?.setMedia(list)
            adapter = PlayBannerCarouselAdapter(adapterTypeFactory)
            recycler_view?.layoutManager = linearLayoutManager
            recycler_view?.adapter = adapter
            recycler_view?.addOnScrollListener(configureParallax())
        }
        configureHeader(playBannerCarouselDataModel)
        configureBackground(playBannerCarouselDataModel)
        adapter?.setItems(list)
    }

    private fun configureHeader(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
        configureTitle(playBannerCarouselDataModel)
        configureSubtitle(playBannerCarouselDataModel)
        configureSeeMore(playBannerCarouselDataModel)
    }

    private fun configureTitle(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
        channel_title?.showOrHideView(playBannerCarouselDataModel.title.isNotBlank())
        channel_title?.text = playBannerCarouselDataModel.title
        channel_title?.setTextColor(
                if (playBannerCarouselDataModel.textColor.isNotEmpty()) Color.parseColor(playBannerCarouselDataModel.textColor)
                else ContextCompat.getColor(context, R.color.Neutral_N700)
        )
    }

    private fun configureSubtitle(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
        channel_subtitle?.showOrHideView(playBannerCarouselDataModel.subtitle.isNotBlank())
        channel_subtitle?.text = playBannerCarouselDataModel.subtitle
        channel_subtitle?.setTextColor(
                if (playBannerCarouselDataModel.textColor.isNotEmpty()) Color.parseColor(playBannerCarouselDataModel.textColor)
                else ContextCompat.getColor(context, R.color.Neutral_N700)
        )
    }

    private fun configureSeeMore(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
        see_all_button?.showOrHideView(playBannerCarouselDataModel.seeMoreApplink.isNotBlank())
    }

    private fun configureBackground(playBannerCarouselDataModel: PlayBannerCarouselDataModel) {
        if (playBannerCarouselDataModel.backgroundUrl.isNotEmpty()) {
            background_loader?.show()
            parallax_image.loadImage(playBannerCarouselDataModel.backgroundUrl, object : ImageHandler.ImageLoaderStateListener{
                override fun successLoad() {
                    parallax_background.setGradientBackground(playBannerCarouselDataModel.gradientColors)
                    background_loader.hide()
                }

                override fun failedLoad() {
                    parallax_background.setGradientBackground(playBannerCarouselDataModel.gradientColors)
                    background_loader.hide()
                }
            })
        } else {
            background_loader.hide()
        }
    }

    private fun configureParallax(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            @SuppressLint("SyntheticAccessor")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                    val firstView = linearLayoutManager.findViewByPosition(linearLayoutManager.findFirstVisibleItemPosition())
                    firstView?.let {
                        val distanceFromLeft = it.left
                        val translateX = distanceFromLeft * 0.2f
                        parallax_view?.translationX = translateX

                        if (distanceFromLeft <= 0) {
                            val itemSize = it.width.toFloat()
                            val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                            parallax_image?.alpha = 1 - alpha
                        }
                    }
                }
            }
        }
    }

    companion object{
        private val LAYOUT = R.layout.layout_play_banner_carousel
    }
}