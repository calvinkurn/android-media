package com.tokopedia.home_component.viewholders

import android.annotation.SuppressLint
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
    private val bannerListener: BannerComponentListener?
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

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(element: BannerRevampDataModel) {
        try {
            setViewPortImpression(element)
            channelModel = element.channelModel
            isCache = element.isCache
            channelModel?.let { channel ->
                this.isCache = element.isCache
                try {
                    val banners = channel.convertToBannerItemModel()
                    totalBanner = banners.size
                    binding?.bannerIndicator?.setBannerIndicators(banners.size)
                    binding?.bannerIndicator?.setBannerListener(object : BannerIndicatorListener {
                        override fun onChangePosition(position: Int) {
                            scrollTo(position)
                        }

                        override fun getCurrentPosition(position: Int) {
                            // no-op
                        }
                    })
                    initBanner(banners)
                } catch (_: NumberFormatException) {
                    // no-op
                }
            }
        } catch (_: Exception) {
            // no-op
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

    private fun getLayoutManager(): LinearLayoutManager {
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        return layoutManager
    }

    private fun initBanner(list: List<BannerItemModel>) {
        if (list.size > Int.ONE) {
            val snapHelper: SnapHelper = BannerComponentViewHolder.CubicBezierSnapHelper(itemView.context)
            binding?.rvBannerRevamp?.let {
                it.onFlingListener = null
                snapHelper.attachToRecyclerView(it)
            }
        }

        val layoutParams = binding?.rvBannerRevamp?.layoutParams as ConstraintLayout.LayoutParams
        binding?.rvBannerRevamp?.layoutParams = layoutParams

        binding?.rvBannerRevamp?.layoutManager = getLayoutManager()
        val adapter = BannerRevampChannelAdapter(list, this)
        val halfIntegerSize = Integer.MAX_VALUE / DIVIDE_HALF_BANNER
        binding?.rvBannerRevamp?.layoutManager?.scrollToPosition(halfIntegerSize - halfIntegerSize % totalBanner)
        binding?.rvBannerRevamp?.adapter = adapter
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

    private fun ChannelModel.convertToBannerItemModel(): List<BannerItemModel> {
        return try {
            this.channelGrids.mapIndexed { index, channelGrid -> BannerItemModel(channelGrid.id.toIntOrZero(), channelGrid.imageUrl, index) }
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
        private const val DIVIDE_HALF_BANNER = 2
        private const val MULTIPLY_NO_BOUNCE_BANNER = 2
    }
}
