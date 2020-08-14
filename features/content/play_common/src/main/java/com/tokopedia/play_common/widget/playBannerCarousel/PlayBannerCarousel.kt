package com.tokopedia.play_common.widget.playBannerCarousel

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elyeproj.loaderviewlibrary.LoaderImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.R
import com.tokopedia.play_common.widget.playBannerCarousel.adapter.PlayBannerCarouselAdapter
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage
import com.tokopedia.play_common.widget.playBannerCarousel.extension.setGradientBackground
import com.tokopedia.play_common.widget.playBannerCarousel.extension.showOrHideView
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.PlayBannerCarouselTypeImpl
import com.tokopedia.play_common.widget.playBannerCarousel.widget.PlayBannerRecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs

class PlayBannerCarousel(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr), CoroutineScope {

    private val recyclerView: PlayBannerRecyclerView
    private val channelTitle: TextView
    private val channelSubtitle: TextView
    private val seeAllButton: TextView
    private val backgroundLoader: LoaderImageView
    private val parallaxBackground: AppCompatImageView
    private val parallaxImage: AppCompatImageView
    private val parallaxView: FrameLayout
    private val containerShimmering: FrameLayout
    private val containerPlayBanner: FrameLayout
    private val shimmeringView: View


    private var adapter: PlayBannerCarouselAdapter? = null
    private val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    private val adapterTypeFactory = PlayBannerCarouselTypeImpl()
    private var listener: PlayBannerCarouselViewEventListener? = null
    private var playBannerCarouselDataModel: PlayBannerCarouselDataModel? = null

    private var timer: CountDownTimer? = null

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val masterJob = Job()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    init {
        val view = LayoutInflater.from(context).inflate(LAYOUT, this)
        shimmeringView = LayoutInflater.from(context).inflate(R.layout.partial_layout_shimmering_play_banner, null)
        recyclerView = view.findViewById(R.id.recycler_view)
        channelTitle = view.findViewById(R.id.channel_title)
        channelSubtitle = view.findViewById(R.id.channel_subtitle)
        seeAllButton = view.findViewById(R.id.see_all_button)
        containerPlayBanner = view.findViewById(R.id.content_play_banner)
        backgroundLoader = view.findViewById(R.id.background_loader)
        parallaxBackground = view.findViewById(R.id.parallax_background)
        parallaxImage = view.findViewById(R.id.parallax_image)
        containerShimmering = view.findViewById(R.id.container_play_shimmering)
        parallaxView = view.findViewById(R.id.parallax_view)
        containerShimmering.addView(shimmeringView)
        showRefreshShimmer()
    }

    fun setListener(listener: PlayBannerCarouselViewEventListener){
        this.listener = listener
    }

    override fun onDetachedFromWindow() {
        recyclerView.resetVideoPlayer()
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        recyclerView.playVideos(false)
    }

    fun onPause(){
        recyclerView.pausePlayers()
    }

    fun onResume(){
        recyclerView.resumePlayers()
    }

    fun onDestroy(){
        recyclerView.releasePlayer()
        stopTimer()
    }

    fun setItem(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
        this.playBannerCarouselDataModel = playBannerCarouselDataModel
        val list = playBannerCarouselDataModel.channelList
        if(adapter == null){
            adapter = PlayBannerCarouselAdapter(adapterTypeFactory, listener)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
            recyclerView.addOnScrollListener(configureParallax())
        }
        recyclerView.setDelayDuration(playBannerCarouselDataModel.durationDelayStartVideo, playBannerCarouselDataModel.durationPlayWithData)
        recyclerView.setAutoPlay(playBannerCarouselDataModel.isAutoPlay, playBannerCarouselDataModel.isAutoPlayAmount)
        recyclerView.setMedia(list)
        configureHeader(playBannerCarouselDataModel)
        configureBackground(playBannerCarouselDataModel)
        adapter?.setItems(list)
        removeRefreshShimmer()
    }

    private fun configureHeader(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
        configureTitle(playBannerCarouselDataModel)
        configureSubtitle(playBannerCarouselDataModel)
        configureSeeMore(playBannerCarouselDataModel)
        configureAutoRefresh(playBannerCarouselDataModel)
    }

    private fun configureTitle(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
        channelTitle.showOrHideView(playBannerCarouselDataModel.title.isNotBlank())
        channelTitle.text = playBannerCarouselDataModel.title
        channelTitle.setTextColor(ContextCompat.getColor(context, R.color.Neutral_N700))
    }

    private fun configureSubtitle(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
//        channelSubtitle.showOrHideView(playBannerCarouselDataModel.subtitle.isNotBlank())
        channelSubtitle.text = playBannerCarouselDataModel.subtitle
        channelSubtitle.setTextColor(ContextCompat.getColor(context, R.color.Neutral_N700))
    }

    private fun configureSeeMore(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
        seeAllButton.showOrHideView(playBannerCarouselDataModel.seeMoreApplink.isNotBlank())
        seeAllButton.setOnClickListener { listener?.onSeeMoreClick(playBannerCarouselDataModel) }
    }

    private fun configureAutoRefresh(playBannerCarouselDataModel: PlayBannerCarouselDataModel){
        if(playBannerCarouselDataModel.isAutoRefresh){
            stopTimer()
            timer = object : CountDownTimer(playBannerCarouselDataModel.isAutoRefreshTimer.toLong() * 1000, 1000) {
                override fun onFinish() {
                    playBannerCarouselDataModel?.let {
                        recyclerView.resetVideoPlayer()
                        listener?.onRefreshView(it)
                    }
                }

                override fun onTick(millisUntilFinished: Long) {}
            }.start()
        }
    }

    private fun stopTimer(){
        if(timer != null){
            timer?.cancel()
        }
    }

    private fun configureBackground(playBannerCarouselDataModel: PlayBannerCarouselDataModel) {
        if(playBannerCarouselDataModel.imageUrl.isNotEmpty()) {
            backgroundLoader.show()
            parallaxImage.loadImage(playBannerCarouselDataModel.imageUrl, object : ImageHandler.ImageLoaderStateListener {
                override fun successLoad() {
                    if (playBannerCarouselDataModel.gradients.isNotEmpty()) {
                        parallaxBackground.setGradientBackground(playBannerCarouselDataModel.gradients)
                    } else if (playBannerCarouselDataModel.backgroundUrl.isNotBlank()) {
                        parallaxBackground.loadImage(playBannerCarouselDataModel.backgroundUrl)
                    }
                    backgroundLoader.hide()
                }

                override fun failedLoad() {
                    if (playBannerCarouselDataModel.gradients.isNotEmpty()) {
                        parallaxBackground.setGradientBackground(playBannerCarouselDataModel.gradients)
                    } else if (playBannerCarouselDataModel.backgroundUrl.isNotBlank()) {
                        parallaxBackground.loadImage(playBannerCarouselDataModel.backgroundUrl)
                    }
                    backgroundLoader.hide()
                }
            })
        } else{
            backgroundLoader.hide()
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
                        parallaxView.translationX = translateX

                        if (distanceFromLeft <= 0) {
                            val itemSize = it.width.toFloat()
                            val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                            parallaxImage.alpha = 1 - alpha
                        }
                    }
                }
            }
        }
    }

    fun showRefreshShimmer(){
        if(shimmeringView.parent == null) containerShimmering.addView(shimmeringView)
        channelTitle.hide()
        channelSubtitle.hide()
        seeAllButton.hide()
        backgroundLoader.hide()
        containerPlayBanner.hide()
    }

    fun removeRefreshShimmer(){
        containerShimmering.removeView(shimmeringView)
        channelTitle.show()
        channelSubtitle.show()
        seeAllButton.show()
        backgroundLoader.show()
        containerPlayBanner.show()
    }
    companion object{
        private val LAYOUT = R.layout.layout_play_banner_carousel
        private val TRANSITION_NAME = "root_play"
    }
}