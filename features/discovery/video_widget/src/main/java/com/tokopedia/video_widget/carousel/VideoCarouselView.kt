package com.tokopedia.video_widget.carousel

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.R
import com.tokopedia.video_widget.VideoPlayer
import com.tokopedia.video_widget.VideoPlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class VideoCarouselView : BaseCustomView, VideoPlayer {
    private val snapHelper = StartSnapHelper()
    private val defaultRecyclerViewDecorator = VideoCarouselVideoItemDefaultDecorator()
    private val adapter = VideoCarouselVideoItemAdapter()
    private var listener: VideoCarouselItemListener? = null
    private var internalListener: VideoCarouselInternalListener? = null
    private var videoPlayerStateFlow : MutableStateFlow<VideoPlayerState>? = null

    private val titleTextView: Typography by lazy {
        findViewById(R.id.carousel_title_textview)
    }

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.carousel_recycler_view)
    }

    private val scrollChangeListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    internalListener?.onWidgetCardsScrollChanged(recyclerView)
                }
            }
        }

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
        View.inflate(context, R.layout.video_carousel_view, this)

        defineCustomAttributes(attrs)
        initRecyclerView()
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes =
                context.obtainStyledAttributes(attrs, R.styleable.VideoCarouselView, 0, 0)

            try {
                tryDefineCustomAttributes(styledAttributes)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun tryDefineCustomAttributes(styledAttributes: TypedArray) {
        titleTextView.text = styledAttributes.getString(R.styleable.VideoCarouselView_title)
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(
            this.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.addItemDecoration(defaultRecyclerViewDecorator)
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(scrollChangeListener)
    }

    fun setCarouselModel(carouselDataView: VideoCarouselDataView) {
        titleTextView.text = carouselDataView.title
        adapter.submitList(carouselDataView.itemList)
    }

    fun recycle() {
        this.listener = null
    }

    fun setWidgetListener(listener: VideoCarouselItemListener?) {
        this.listener = listener
        adapter.setListener(listener)
    }

    fun onWifiConnectionChange(isConnectedToWifi: Boolean) {
        adapter.onWifiConnectionChange(isConnectedToWifi)
    }

    internal fun setWidgetInternalListener(internalListener: VideoCarouselInternalListener?) {
        this.internalListener = internalListener
    }

    override fun onDetachedFromWindow() {
        internalListener?.onWidgetDetached(this)
        super.onDetachedFromWindow()
    }

    override val hasVideo: Boolean
        get() = adapter.itemCount > 0

    override fun playVideo(exoPlayer: ExoPlayer): Flow<VideoPlayerState> {
        if(!hasVideo) return flowOf(VideoPlayerState.NoVideo)
        videoPlayerStateFlow = MutableStateFlow(VideoPlayerState.Starting)
        internalListener?.playVideo(recyclerView)
        return videoPlayerStateFlow as Flow<VideoPlayerState>
    }

    override fun stopVideo() {
        internalListener?.stopVideo()
        videoPlayerStateFlow?.tryEmit(VideoPlayerState.Ended)
    }
}
