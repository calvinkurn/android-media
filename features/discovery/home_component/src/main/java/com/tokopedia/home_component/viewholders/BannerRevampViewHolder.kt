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
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicator
import com.tokopedia.home_component.customview.bannerindicator.BannerIndicatorListener
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.NpaLinearLayoutManager
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
    private var isCache = true
    private val layoutManager by lazy { NpaLinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL) }

    private val masterJob = Job()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private var channelModel: ChannelModel? = null
    private var isBleeding: Boolean = false
    private var totalBanner = 0
    private var previousTotalBanner = 0
    private var currentPosition: Int = 0
    private var isFromDrag = false
    private var isFromInitialize = false

    private val adapter by lazy { BannerRevampChannelAdapter( this) }
    
    private val bannerCardContainer: CardUnify2 by lazy { itemView.findViewById(home_componentR.id.card_container_banner) }
    private val bannerContainer: ConstraintLayout by lazy { itemView.findViewById(home_componentR.id.container_banner) }
    private val bannerIndicator: BannerIndicator by lazy { itemView.findViewById(home_componentR.id.banner_indicator) }
    private val recyclerView: RecyclerView by lazy { itemView.findViewById(home_componentR.id.rv_banner_revamp) }

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(element: BannerRevampDataModel) {
        try {
            setScrollListener()
            setViewPortImpression(element)
            channelModel = element.channelModel
            isCache = element.isCache
            isBleeding = element.isBleeding
            renderBanner()
            setContainerPadding(element)
        } catch (_: Exception) { }
    }

    private fun renderBanner() {
        channelModel?.let { channel ->
            val banners = channel.convertToBannerItemModel()
            totalBanner = banners.size
            initBanner(banners)
            if (previousTotalBanner != totalBanner) {
                bannerIndicator.setBannerIndicators(banners.size)
                bannerIndicator.setBannerListener(object :
                    BannerIndicatorListener {
                    override fun onChangePosition(index: Int, position: Int) {
                        scrollTo(position)
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

    private fun setContainerPadding(element: BannerRevampDataModel) {
        if(element.isBleeding) {
            val topPadding = itemView.context.resources.getDimensionPixelSize(home_componentR.dimen.home_hpb_bleeding_padding_top)
            val bottomPadding = itemView.context.resources.getDimensionPixelSize(home_componentR.dimen.home_hpb_bleeding_padding_bottom)
            bannerContainer.setPadding(Int.ZERO, topPadding, Int.ZERO, bottomPadding)
        } else {
            bannerContainer.setPadding(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
        }
    }

    private fun setViewPortImpression(element: BannerRevampDataModel) {
        if (!element.isCache) {
            itemView.addOnImpressionListener(holder = element, onView = {
                element.channelModel?.let {
                    bannerListener?.onChannelBannerImpressed(it, element.channelModel.verticalPosition)
                }
            })
        }
    }

    private fun setScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (isFromDrag) {
                            val currentPagePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                            currentPosition = currentPagePosition
                            if (currentPagePosition != RecyclerView.NO_POSITION) {
                                bannerIndicator.startIndicatorByPosition(currentPagePosition)
                                isFromDrag = false
                            }
                        }
                        if (isFromInitialize) {
                            val currentPagePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                            currentPosition = currentPagePosition
                            if (currentPagePosition != RecyclerView.NO_POSITION) {
                                bannerIndicator.setBannerIndicators(totalBanner, currentPagePosition)
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
    }

    override fun bind(element: BannerRevampDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun scrollTo(position: Int) {
        val resources = itemView.context.resources
        val width = resources.displayMetrics.widthPixels
        val paddings = if(isBleeding) 0 else MULTIPLY_NO_BOUNCE_BANNER * resources.getDimensionPixelSize(home_componentR.dimen.home_component_padding_horizontal_default)
        if (position == Int.ZERO) {
            recyclerView.smoothScrollToPosition(position)
        } else {
            recyclerView.smoothScrollBy(
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
            recyclerView.let {
                it.onFlingListener = null
                snapHelper.attachToRecyclerView(it)
            }
        }

        val layoutParams = recyclerView.layoutParams as ConstraintLayout.LayoutParams
        recyclerView.layoutParams = layoutParams

        recyclerView.layoutManager = this.layoutManager
        val halfIntegerSize = Integer.MAX_VALUE / DIVIDE_HALF_BANNER_SIZE_INT_SIZE
        adapter.submitList(list) {
            recyclerView.layoutManager?.scrollToPosition(halfIntegerSize - halfIntegerSize % totalBanner)
        }
        recyclerView.adapter = adapter
        if(!isBleeding) {
            bannerCardContainer.animateOnPress = if (cardInteraction) CardUnify2.ANIMATE_OVERLAY_BOUNCE else CardUnify2.ANIMATE_OVERLAY
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
        pauseIndicator()
    }

    override fun onRelease() {
        resumeIndicator()
    }

    override fun isDrag(): Boolean {
        return isFromDrag
    }

    override fun onTouchEvent(motionEvent: MotionEvent) {
        if(isBleeding) {
            bannerContainer.onTouchEvent(motionEvent)
        } else {
            bannerCardContainer.onTouchEvent(motionEvent)
        }
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

    fun pauseIndicator() {
        bannerIndicator.pauseAnimation()
    }

    fun resumeIndicator() {
        bannerIndicator.continueAnimation()
    }

    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.home_component_banner_padding
        @LayoutRes
        val LAYOUT_BLEEDING = home_componentR.layout.home_component_banner_bleeding
        private const val DIVIDE_HALF_BANNER_SIZE_INT_SIZE = 2
        private const val MULTIPLY_NO_BOUNCE_BANNER = 2
    }
}
