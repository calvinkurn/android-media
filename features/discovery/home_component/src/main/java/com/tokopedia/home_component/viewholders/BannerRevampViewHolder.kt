package com.tokopedia.home_component.viewholders

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentBannerRevampBinding
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.viewholders.adapter.BannerItemListener
import com.tokopedia.home_component.viewholders.adapter.BannerItemModel
import com.tokopedia.home_component.viewholders.adapter.BannerRevampChannelAdapter
import com.tokopedia.home_component.visitable.BannerRevampDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import com.tokopedia.unifyprinciples.R as RUnify

class BannerRevampViewHolder(
    itemView: View,
    private val bannerListener: BannerComponentListener?,
    private val homeComponentListener: HomeComponentListener?
) :
    AbstractViewHolder<BannerRevampDataModel>(itemView),
    BannerItemListener,
    CoroutineScope {
    private var binding: HomeComponentBannerRevampBinding? by viewBinding()
    private var isCache = true
    private var layoutManager = LinearLayoutManager(itemView.context)

    private var isUsingDotsAndInfiniteScroll: Boolean = false
    private var scrollTransitionDuration: Long = 5000L

    private val masterJob = Job()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private var channelModel: ChannelModel? = null

    // set to true if you want to activate auto-scroll
    private var isAutoScroll = true
    private var autoScrollState = STATE_PAUSED
    private var currentPagePosition = INITIAL_PAGE_POSITION
    private var lastPagePosition = Integer.MAX_VALUE

    private fun autoScrollLauncher() = launch(coroutineContext) {
        if (autoScrollState == STATE_RUNNING) {
            delay(scrollTransitionDuration)
            autoScrollCoroutine()
        }
    }

    init {
        itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(p0: View) {
                pauseAutoScroll()
            }

            override fun onViewAttachedToWindow(p0: View) {
                resumeAutoScroll()
            }
        })
    }

    override fun bind(element: BannerRevampDataModel) {
        try {
            isUsingDotsAndInfiniteScroll = element.enableDotsAndInfiniteScroll
            scrollTransitionDuration = element.scrollTransitionDuration
            setViewPortImpression(element)
            channelModel = element.channelModel
            isCache = element.isCache

            channelModel?.let { it ->
                this.isCache = element.isCache
                val size = it.channelGrids.size
                lastPagePosition = if (isUsingDotsAndInfiniteScroll) Integer.MAX_VALUE else size - 1
                drawIndicators(binding!!.indicatorLayout, 0, it.channelGrids.size)
                try {
                    initBanner(it.convertToBannerItemModel(), element.dimenMarginTop, element.dimenMarginBottom, element.cardInteraction)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setViewPortImpression(element: BannerRevampDataModel) {
        if (!element.isCache) {
            itemView.addOnImpressionListener(holder = element, onView = {
                element.channelModel?.let {
                    bannerListener?.onChannelBannerImpressed(it, adapterPosition)
                }
                setScrollListener()
            })
        }
    }

    override fun bind(element: BannerRevampDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    fun scrollTo(position: Int) {
        val resources = itemView.context.resources
        val width = resources.displayMetrics.widthPixels
        val paddings = 2 * resources.getDimensionPixelSize(R.dimen.home_component_margin_default)
        if (isUsingDotsAndInfiniteScroll) {
            pauseAutoScroll()
            binding?.rvBannerRevamp?.smoothScrollBy(width - paddings, 0, autoScrollInterpolator, FLING_DURATION)
        } else {
            pauseAutoScroll()
            if (position == 0) {
                binding?.rvBannerRevamp?.smoothScrollToPosition(position)
            } else {
                binding?.rvBannerRevamp?.smoothScrollBy(width - paddings, 0, null, FLING_DURATION_OLD)
            }
        }
    }

    private fun autoScrollCoroutine() {
        if (isAutoScroll) {
            val nextPagePosition = if (currentPagePosition >= lastPagePosition) {
                0
            } else {
                currentPagePosition + 1
            }

            scrollTo(nextPagePosition)
        }
    }

    private fun resumeAutoScroll() {
        if (autoScrollState == STATE_PAUSED) {
            autoScrollState = STATE_RUNNING
            autoScrollLauncher()
        }
    }

    private fun pauseAutoScroll() {
        if (autoScrollState == STATE_RUNNING) {
            masterJob.cancelChildren()
            autoScrollState = STATE_PAUSED
        }
    }

    private fun getLayoutManager(list: List<BannerItemModel>): LinearLayoutManager {
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        return layoutManager
    }

    private fun initBanner(list: List<BannerItemModel>, dimenMarginTop: Int, dimenMarginBottom: Int, cardInteraction: Boolean) {
        binding?.rvBannerRevamp?.clearOnScrollListeners()

        if (list.size > 1) {
            val snapHelper: SnapHelper = if (isUsingDotsAndInfiniteScroll) CubicBezierSnapHelper(itemView.context) else PagerSnapHelper()
            binding?.rvBannerRevamp?.let {
                it.onFlingListener = null
                snapHelper.attachToRecyclerView(it)
            }
        }

        val layoutParams = binding?.rvBannerRevamp?.layoutParams as ConstraintLayout.LayoutParams
        binding?.rvBannerRevamp?.layoutParams = layoutParams

        binding?.rvBannerRevamp?.layoutManager = getLayoutManager(list)
        val adapter = BannerRevampChannelAdapter(list, this, isUsingDotsAndInfiniteScroll)
        binding?.rvBannerRevamp?.adapter = adapter
    }

    private fun setScrollListener() {
        binding?.rvBannerRevamp?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        onPageDragStateChanged(false)
                        currentPagePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                        channelModel?.let {
                            val dotsPosition = currentPagePosition % (channelModel?.channelGrids?.size ?: 0)
                            drawIndicators(binding?.indicatorLayout!!, dotsPosition, it.channelGrids.size)
                        }
                        resumeAutoScroll()
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        onPageDragStateChanged(true)
                        pauseAutoScroll()
                    }
                }
            }
        })
    }

    override fun onImpressed(position: Int) {
        channelModel?.let { channel ->
            val realPosition = position % channel.channelGrids.size
            channel.selectGridInPosition(realPosition) {
                if (bannerListener?.isMainViewVisible() == true && !isCache && !bannerListener.isBannerImpressed(it.id) && position != -1) {
                    bannerListener.onPromoScrolled(channel, it, realPosition)
                }
            }
        }
    }

    override fun onClick(position: Int) {
        channelModel?.let { channel ->
            val realPosition = position % channel.channelGrids.size
            channel.selectGridInPosition(realPosition) {
                bannerListener?.onBannerClickListener(realPosition, it, channel)
            }
        }
    }

    override fun onLongPress() {
        pauseAutoScroll()
    }

    override fun onRelease() {
        resumeAutoScroll()
    }

    private fun onPageDragStateChanged(isDrag: Boolean) {
        bannerListener?.onPageDragStateChanged(isDrag)
    }

    private fun drawIndicators(viewGroup: ViewGroup, currentPage: Int, totalSize: Int) {
        if (isUsingDotsAndInfiniteScroll) {
            viewGroup.visible()
            if (viewGroup.childCount > 0) {
                viewGroup.removeAllViews()
            }

            val params = LinearLayout.LayoutParams(
                DOTS_SIZE.toDpInt(),
                DOTS_SIZE.toDpInt()
            )
            params.setMargins(
                DOTS_MARGIN.toDpInt(),
                MARGIN_ZERO,
                DOTS_MARGIN.toDpInt(),
                MARGIN_ZERO
            )

            for (i in 0 until totalSize) {
                viewGroup.addView(
                    AppCompatImageView(viewGroup.context).apply {
                        layoutParams = params
                        background = GradientDrawable().apply {
                            shape = GradientDrawable.OVAL
                            if (currentPage == i) {
                                setColor(ContextCompat.getColor(context, RUnify.color.Unify_GN500))
                            } else {
                                setColor(ContextCompat.getColor(context, RUnify.color.Unify_NN200))
                            }
                        }
                    }
                )
            }
        } else {
            viewGroup.gone()
        }
    }

    private fun ChannelModel.convertToBannerItemModel(): List<BannerItemModel> {
        return try {
            this.channelGrids.map { BannerItemModel(it.id.toIntOrZero(), it.imageUrl) }
        } catch (e: NumberFormatException) {
            listOf()
        }
    }

    private fun ChannelModel.selectGridInPosition(position: Int, action: (ChannelGrid) -> Unit = {}) {
        if (position != -1 && this.channelGrids.size > position) {
            action.invoke(this.channelGrids[position])
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_banner_revamp
        private const val STATE_RUNNING = 0
        private const val STATE_PAUSED = 1
        private const val INITIAL_PAGE_POSITION = 0
        private const val MARGIN_ZERO = 0
        private const val DOTS_SIZE = 6f
        private const val DOTS_MARGIN = 2f
        private val autoScrollInterpolator = PathInterpolatorCompat.create(.63f, .01f, .29f, 1f)
        private val manualScrollInterpolator = PathInterpolatorCompat.create(.2f, .64f, .21f, 1f)
        private const val FLING_DURATION = 600
        private const val FLING_DURATION_OLD = 300
    }

    class CubicBezierSnapHelper(private val context: Context) : PagerSnapHelper() {
        companion object {
            private const val DX_POS = 0
            private const val DY_POS = 1
        }

        override fun createScroller(layoutManager: RecyclerView.LayoutManager): RecyclerView.SmoothScroller? {
            if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
                return null
            }
            return object : LinearSmoothScroller(context) {
                override fun onTargetFound(
                    targetView: View,
                    state: RecyclerView.State,
                    action: Action
                ) {
                    val snapDistances = calculateDistanceToFinalSnap(layoutManager, targetView)
                    val dx = snapDistances?.get(DX_POS) ?: 0
                    val dy = snapDistances?.get(DY_POS) ?: 0
                    val time = FLING_DURATION
                    action.dx = dx
                    action.dy = dy
                    action.update(dx, dy, time, manualScrollInterpolator)
                }
            }
        }
    }
}
