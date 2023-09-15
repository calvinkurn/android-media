package com.tokopedia.home_component.viewholders

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicatorListener
import com.tokopedia.home_component.databinding.HomeComponentBannerRevampBinding
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.NpaLinearLayoutManager
import com.tokopedia.home_component.util.recordCrashlytics
import com.tokopedia.home_component.viewholders.adapter.BannerItemListener
import com.tokopedia.home_component.widget.atf_banner.BannerRevampItemModel
import com.tokopedia.home_component.viewholders.adapter.BannerRevampChannelAdapter
import com.tokopedia.home_component.visitable.BannerRevampDataModel
import com.tokopedia.home_component.widget.atf_banner.BannerShimmerModel
import com.tokopedia.home_component.widget.atf_banner.BannerVisitable
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class BannerRevampViewHolder(
    itemView: View,
    private val bannerListener: BannerComponentListener?,
    private val cardInteraction: Boolean = false
) :
    AbstractViewHolder<BannerRevampDataModel>(itemView),
    BannerItemListener,
    CoroutineScope {
    private var binding: HomeComponentBannerRevampBinding? by viewBinding()
    private var isCache = true
    private val layoutManager by lazy { NpaLinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL) }

    private val masterJob = Job()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private var channelModel: ChannelModel? = null
    private var totalBanner = 0
    private var previousTotalBanner = 0
    private var currentPosition: Int = 0
    private var isFromDrag = false
    private var isFromInitialize = false

    private val adapter by lazy { BannerRevampChannelAdapter( this) }

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(element: BannerRevampDataModel) {
        try {
            setViewPortImpression(element)
            channelModel = element.channelModel
            isCache = element.isCache
            renderBanner()
        } catch (_: Exception) { }
    }

    private fun renderBanner() {
        channelModel?.let { channel ->
            val banners = channel.convertToBannerItemModel()
            totalBanner = banners.size
            initBanner(banners)
            if (previousTotalBanner != totalBanner) {
                binding?.bannerIndicator?.setBannerIndicators(banners.size)
                binding?.bannerIndicator?.setBannerListener(object :
                    BannerIndicatorListener {
                    override fun onChangePosition(actualPosition: Int, bannerPosition: Int) {
                        scrollTo(bannerPosition)
                    }

                    override fun getCurrentPosition(position: Int) {
                        // no-op
                    }
                })
            } else {
                isFromInitialize = true
            }
        }
    }

    private fun setViewPortImpression(element: BannerRevampDataModel) {
        if (!element.isCache) {
            itemView.addOnImpressionListener(holder = element, onView = {
                element.channelModel?.let {
                    bannerListener?.onChannelBannerImpressed(it, element.channelModel.verticalPosition)
                }
            })
            setScrollListener()
        }
    }

    private fun setScrollListener() {
        binding?.rvBannerRevamp?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (isFromDrag) {
                            val currentPagePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                            currentPosition = currentPagePosition
                            if (currentPagePosition != RecyclerView.NO_POSITION) {
                                binding?.bannerIndicator?.startIndicatorByPosition(currentPagePosition)
                                isFromDrag = false
                            }
                        }
                        if (isFromInitialize) {
                            val currentPagePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                            currentPosition = currentPagePosition
                            if (currentPagePosition != RecyclerView.NO_POSITION) {
                                binding?.bannerIndicator?.setBannerIndicators(totalBanner, currentPagePosition)
                                isFromInitialize = false
                            }
                        }
                    }
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
        val paddings = MULTIPLY_NO_BOUNCE_BANNER * resources.getDimensionPixelSize(R.dimen.home_component_margin_default)
        if (position == Int.ZERO) {
            binding?.rvBannerRevamp?.smoothScrollToPosition(position)
        } else {
            binding?.rvBannerRevamp?.smoothScrollBy(
                width - paddings,
                Int.ZERO,
                if (isFromDrag) BannerComponentViewHolder.manualScrollInterpolator else BannerComponentViewHolder.autoScrollInterpolator,
                BannerComponentViewHolder.FLING_DURATION
            )
        }
    }

    private fun initBanner(list: List<BannerVisitable>) {
        if (list.size > Int.ONE) {
            val snapHelper: SnapHelper = BannerComponentViewHolder.CubicBezierSnapHelper(itemView.context)
            binding?.rvBannerRevamp?.let {
                it.onFlingListener = null
                snapHelper.attachToRecyclerView(it)
            }
        }

        val layoutParams = binding?.rvBannerRevamp?.layoutParams as ConstraintLayout.LayoutParams
        binding?.rvBannerRevamp?.layoutParams = layoutParams

        binding?.rvBannerRevamp?.layoutManager = this.layoutManager
        binding?.cardContainerBanner?.let {
            it.animateOnPress = if (cardInteraction) CardUnify2.ANIMATE_OVERLAY_BOUNCE else CardUnify2.ANIMATE_OVERLAY
            val halfIntegerSize = Integer.MAX_VALUE / DIVIDE_HALF_BANNER_SIZE_INT_SIZE
            adapter.submitList(list)
            binding?.rvBannerRevamp?.layoutManager?.scrollToPosition(halfIntegerSize - halfIntegerSize % totalBanner)
            binding?.rvBannerRevamp?.adapter = adapter
        }
    }

    override fun onImpressed(position: Int) {
        channelModel?.let { channel ->
            channel.selectGridInPosition(position) {
                if (bannerListener?.isMainViewVisible() == true && !isCache && !bannerListener.isBannerImpressed(it.id) && position != RecyclerView.NO_POSITION) {
                    bannerListener.onPromoScrolled(channel, it, position)
                }
            }
        }
    }

    override fun onClick(position: Int) {
        channelModel?.let { channel ->
            channel.selectGridInPosition(position) {
                bannerListener?.onBannerClickListener(position, it, channel)
            }
        }
    }

    override fun onLongPress() {
        binding?.bannerIndicator?.pauseAnimation()
    }

    override fun onRelease() {
        binding?.bannerIndicator?.continueAnimation()
    }

    override fun isDrag(): Boolean {
        return isFromDrag
    }

    override fun onTouchEvent(motionEvent: MotionEvent) {
        binding?.cardContainerBanner?.onTouchEvent(motionEvent)
    }

    private fun ChannelModel.convertToBannerItemModel(): List<BannerVisitable> {
        return try {
            this.channelGrids.mapIndexed { index, channelGrid ->
                BannerRevampItemModel(channelGrid.id.toIntOrZero(), channelGrid.imageUrl, index)
            }.ifEmpty { listOf(BannerShimmerModel()) }
        } catch (e: NumberFormatException) {
            listOf()
        }
    }

    private fun ChannelModel.selectGridInPosition(position: Int, action: (ChannelGrid) -> Unit = {}) {
        if (position != RecyclerView.NO_POSITION && this.channelGrids.size > position) {
            action.invoke(this.channelGrids[position])
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_banner_revamp
        private const val DIVIDE_HALF_BANNER_SIZE_INT_SIZE = 2
        private const val MULTIPLY_NO_BOUNCE_BANNER = 2
    }
}
