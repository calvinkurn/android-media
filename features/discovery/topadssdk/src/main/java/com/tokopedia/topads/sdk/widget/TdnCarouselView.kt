package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_CAROUSEL
import com.tokopedia.topads.sdk.TopAdsConstants.TdnBannerConstants.TYPE_VERTICAL_CAROUSEL
import com.tokopedia.topads.sdk.decoration.TdnCarouselItemDecoration
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.view.adapter.TdnCarouselAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private const val STATE_RUNNING = 0
private const val STATE_PAUSED = 1
const val INITIAL_PAGE_POSITION = 0
private const val FLING_DURATION = 300
class TdnCarouselView : BaseCustomView, CoroutineScope {

    private var tdnCarouselAdapter: TdnCarouselAdapter? = null
    private val tdnRv by lazy { findViewById<RecyclerView>(R.id.tdnCarouselRv) }
    private val layoutManager by lazy { LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) }
    private var isAutoScrollEnabled = false
    private var autoScrollState = STATE_PAUSED
    private var currentPagePosition = INITIAL_PAGE_POSITION
    private var scrollTransitionDuration: Long = 0L
    private var lastPagePosition = Integer.MAX_VALUE
    private var widgetType = TYPE_VERTICAL_CAROUSEL

    private val masterJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.layout_widget_tdn_carousel_view, this)
    }

    fun setCarouselModel(
        topAdsImageViewModel: List<TopAdsImageViewModel>,
        onTdnBannerClicked: (applink: String) -> Unit,
        cornerRadius: Int,
        onLoadFailed: () -> Unit,
        onTdnBannerImpressed: () -> Unit
    ) {
        val model = topAdsImageViewModel.firstOrNull()
        tdnCarouselAdapter = TdnCarouselAdapter(
            onTdnBannerClicked,
            cornerRadius,
            model?.isAutoScrollEnabled ?: false,
            onLoadFailed,
            onTdnBannerImpressed
        )
        tdnRv.layoutManager = layoutManager
        widgetType = model?.layoutType ?: TYPE_VERTICAL_CAROUSEL
        if (widgetType != TYPE_VERTICAL_CAROUSEL) {
            isAutoScrollEnabled = model?.isAutoScrollEnabled ?: false
            scrollTransitionDuration = model?.scrollDuration?.toLong() ?: 0L
            val snapHelper: SnapHelper = PagerSnapHelper()
            tdnRv.onFlingListener = null
            snapHelper.attachToRecyclerView(tdnRv)
            setScrollListener()
        }
        tdnRv.removeAllItemDecoration()
        tdnRv.addItemDecoration(TdnCarouselItemDecoration())
        tdnRv.adapter = tdnCarouselAdapter
        tdnCarouselAdapter?.setList(topAdsImageViewModel)
    }

    private fun scrollTo(position: Int) {
        val resources = context.resources
        val width = resources.displayMetrics.widthPixels
        val paddings = 2 * resources.getDimensionPixelSize(R.dimen.sdk_dp_16)
        pauseAutoScroll()
        if (position == Int.ZERO) {
            tdnRv.smoothScrollToPosition(position)
        } else {
            tdnRv.smoothScrollBy(width - paddings, Int.ZERO, null, FLING_DURATION)
        }
    }

    private fun setScrollListener() {
        tdnRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        currentPagePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                        resumeAutoScroll()
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        pauseAutoScroll()
                    }
                }
            }
        })
    }

    private fun resumeAutoScroll() {
        if (autoScrollState == STATE_PAUSED) {
            autoScrollState = STATE_RUNNING
            autoScrollLauncher()
        }
    }

    private fun autoScrollLauncher() = launch(coroutineContext) {
        if (autoScrollState == STATE_RUNNING) {
            delay(scrollTransitionDuration)
            autoScrollCoroutine()
        }
    }

    private fun pauseAutoScroll() {
        if (autoScrollState == STATE_RUNNING) {
            masterJob.cancelChildren()
            autoScrollState = STATE_PAUSED
        }
    }

    private fun autoScrollCoroutine() {
        if (isAutoScrollEnabled) {
            val nextPagePosition = if (currentPagePosition >= lastPagePosition) {
                0
            } else {
                currentPagePosition + 1
            }
            scrollTo(nextPagePosition)
        }
    }

    override fun onAttachedToWindow() {
        if (widgetType == TYPE_CAROUSEL && isAutoScrollEnabled) resumeAutoScroll()
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        if (widgetType == TYPE_CAROUSEL && isAutoScrollEnabled) pauseAutoScroll()
        super.onDetachedFromWindow()
    }
}

private fun RecyclerView.removeAllItemDecoration() {
    if (this.itemDecorationCount > Int.ZERO) {
        for (i in Int.ZERO until this.itemDecorationCount) {
            this.removeItemDecorationAt(i)
        }
    }
}
