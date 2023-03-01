package com.tokopedia.home_component.viewholders

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicatorListener
import com.tokopedia.home_component.databinding.HomeComponentBannerRevampBinding
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.viewholders.adapter.BannerItemListener
import com.tokopedia.home_component.viewholders.adapter.BannerItemModel
import com.tokopedia.home_component.viewholders.adapter.BannerRevampChannelAdapter
import com.tokopedia.home_component.visitable.BannerRevampDataModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

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

    private val masterJob = Job()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private var channelModel: ChannelModel? = null
    private var totalBanner = 0
    private var currentPosition: Int = 0
    private var isFromDrag = false

    init {
        itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(p0: View) {
            }

            override fun onViewAttachedToWindow(p0: View) {
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(element: BannerRevampDataModel) {
        try {
            setViewPortImpression(element)
            channelModel = element.channelModel
            isCache = element.isCache
            channelModel?.let { it ->
                this.isCache = element.isCache
                try {
                    val banners = it.convertToBannerItemModel()
                    totalBanner = banners.size
                    binding?.bannerIndicator?.setBannerIndicators(banners.size)
                    binding?.bannerIndicator?.setBannerListener(object : BannerIndicatorListener {
                        override fun onChangePosition(position: Int) {
                            scrollTo(position)
                        }

                        override fun getCurrentPosition(position: Int) {
                        }
                    })
                    initBanner(banners)
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
                    bannerListener?.onChannelBannerImpressed(it, absoluteAdapterPosition)
                }
            })
            setScrollListener()
        }
    }

    private fun setScrollListener() {
        binding?.rvBannerRevamp?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isFromDrag && kotlin.math.abs(dx) == Int.ONE) {
                    val currentPagePosition = if (dx > Int.ZERO) layoutManager.findLastVisibleItemPosition() % totalBanner else layoutManager.findFirstVisibleItemPosition() % totalBanner
                    currentPosition = currentPagePosition
                    if (currentPagePosition != RecyclerView.NO_POSITION) {
                        binding?.bannerIndicator?.startIndicatorByPosition(currentPagePosition)
                        isFromDrag = false
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        isFromDrag = true
                    }
                }
            }
        })
        binding?.rvBannerRevamp?.computeHorizontalScrollOffset()
    }

    override fun bind(element: BannerRevampDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun scrollTo(position: Int) {
        val resources = itemView.context.resources
        val width = resources.displayMetrics.widthPixels
        val paddings = 2 * resources.getDimensionPixelSize(R.dimen.home_component_margin_default)
        if (position == Int.ZERO) {
            binding?.rvBannerRevamp?.smoothScrollToPosition(position)
        } else {
            binding?.rvBannerRevamp?.smoothScrollBy(
                width - paddings,
                Int.ZERO,
                if (isFromDrag) manualScrollInterpolator else autoScrollInterpolator,
                FLING_DURATION
            )
        }
    }

    private fun getLayoutManager(): LinearLayoutManager {
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        return layoutManager
    }

    private fun initBanner(list: List<BannerItemModel>) {
        if (list.size > Int.ONE) {
            val snapHelper: SnapHelper = CubicBezierSnapHelper(itemView.context)
            binding?.rvBannerRevamp?.let {
                it.onFlingListener = null
                snapHelper.attachToRecyclerView(it)
            }
        }

        val layoutParams = binding?.rvBannerRevamp?.layoutParams as ConstraintLayout.LayoutParams
        binding?.rvBannerRevamp?.layoutParams = layoutParams

        binding?.rvBannerRevamp?.layoutManager = getLayoutManager()
        val adapter = BannerRevampChannelAdapter(list, this)
        val halfIntegerSize = Integer.MAX_VALUE / 2
        binding?.rvBannerRevamp?.layoutManager?.scrollToPosition(halfIntegerSize - halfIntegerSize % totalBanner)
        binding?.rvBannerRevamp?.adapter = adapter
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
        binding?.bannerIndicator?.pauseAnimation()
        // no-op
    }

    override fun onRelease() {
        binding?.bannerIndicator?.continueAnimation()
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
        val autoScrollInterpolator = PathInterpolatorCompat.create(.63f, .01f, .29f, 1f)
        val manualScrollInterpolator = PathInterpolatorCompat.create(.2f, .64f, .21f, 1f)
        const val FLING_DURATION = 600
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
