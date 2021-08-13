package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.decoration.BannerChannelDecoration
import com.tokopedia.home_component.decoration.BannerChannelSingleItemDecoration
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.removeAllItemDecoration
import com.tokopedia.home_component.viewholders.adapter.BannerChannelAdapter
import com.tokopedia.home_component.viewholders.adapter.BannerItemListener
import com.tokopedia.home_component.viewholders.adapter.BannerItemModel
import com.tokopedia.home_component.viewholders.layoutmanager.PeekingLinearLayoutManager
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.home_component_banner.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * @author by devarafikry on 11/28/20.
 */

class BannerComponentViewHolder(itemView: View,
                                private val bannerListener: BannerComponentListener?,
                                private val homeComponentListener: HomeComponentListener?
)
    : AbstractViewHolder<BannerDataModel>(itemView),
        BannerItemListener, CoroutineScope {
    private var isCache = true
    private val rvBanner: RecyclerView = itemView.findViewById(R.id.rv_banner)
    private var layoutManager = LinearLayoutManager(itemView.context)

    private val masterJob = Job()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private var channelModel: ChannelModel? = null

    //set to true if you want to activate auto-scroll
    private var isAutoScroll = true
    private var interval = 5000
    private var currentPagePosition = 0

    private val state_running = 0
    private val state_paused = 1
    private var autoScrollState = state_paused

    private fun autoScrollLauncher() = launch(coroutineContext) {
        while (autoScrollState == state_running) {
            delay(interval.toLong())
            autoScrollCoroutine()
        }
    }

    init {
        itemView.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(p0: View?) {
                pauseAutoScroll()
            }

            override fun onViewAttachedToWindow(p0: View?) {
                resumeAutoScroll()
            }
        })
    }

    override fun bind(element: BannerDataModel) {
        try {
            setHeaderComponent(element)
            setChannelDivider(element)
            setViewPortImpression(element)
            channelModel = element.channelModel
            isCache = element.isCache

            channelModel?.let { it ->
                this.isCache = element.isCache
                try {
                    initBanner(it.convertToBannerItemModel())
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setViewPortImpression(element: BannerDataModel) {
        if (!element.isCache) {
            itemView.addOnImpressionListener(holder = element, onView = {
                element.channelModel?.let {
                    bannerListener?.onChannelBannerImpressed(it, adapterPosition)
                }
                setScrollListener()
            })
        }
    }

    override fun bind(element: BannerDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    fun scrollTo(position: Int) {
        rvBanner.smoothScrollToPosition(position)
    }

    private fun setChannelDivider(element: BannerDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.home_component_divider_header,
            dividerBottom = itemView.home_component_divider_footer
        )
    }

    private suspend fun autoScrollCoroutine() = withContext(Dispatchers.Main){
        if (isAutoScroll) {
            scrollTo(currentPagePosition)

            channelModel?.let {
                val size = channelModel?.channelGrids?.size?:0
                if (currentPagePosition == (size-1) ) {
                    currentPagePosition = 0
                } else {
                    currentPagePosition++
                }
            }
        }
    }

    private fun resumeAutoScroll() {
        if (autoScrollState == state_paused) {
            autoScrollLauncher()
            autoScrollState = state_running
        }
    }

    private fun pauseAutoScroll() {
        if (autoScrollState == state_running) {
            masterJob.cancelChildren()
            autoScrollState = state_paused
        }
    }

    private fun getLayoutManager(list: List<BannerItemModel>): LinearLayoutManager {
        layoutManager = if (list.size == 1) {
            LinearLayoutManager(itemView.context)
        } else PeekingLinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        return layoutManager
    }

    private fun initBanner(list: List<BannerItemModel>){
        rvBanner.clearOnScrollListeners()

        val snapHelper: SnapHelper = PagerSnapHelper()
        rvBanner.onFlingListener = null
        snapHelper.attachToRecyclerView(rvBanner)
        rvBanner.layoutManager = getLayoutManager(list)
        rvBanner.removeAllItemDecoration()
        if (rvBanner.itemDecorationCount == 0) {
            if (list.size == 1) {
                rvBanner.addItemDecoration(BannerChannelSingleItemDecoration())
            } else rvBanner.addItemDecoration(BannerChannelDecoration())
        }
        val adapter = BannerChannelAdapter(list, this)
        rvBanner.adapter = adapter
        adapter.setItemList(list)
    }

    private fun setScrollListener() {
        rvBanner.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        onPageDragStateChanged(false)
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
        channelModel?.let {channel ->
            channel.selectGridInPosition(position) {
                onPromoScrolled(position)
            }
        }
    }

    override fun onClick(position: Int) {
        channelModel?.let {channel ->
            channel.selectGridInPosition(position) {
                bannerListener?.onBannerClickListener(position, it, channel)
            }
        }
    }

    private fun onPromoScrolled(position: Int) {
        channelModel?.let {channel ->
            channel.selectGridInPosition(position) {
                if (bannerListener?.isMainViewVisible() == true && !isCache && !bannerListener.isBannerImpressed(it.id) && position != -1) {
                    bannerListener.onPromoScrolled(channel, it ,position)
                }
            }
        }

    }

    private fun onPageDragStateChanged(isDrag: Boolean) {
        bannerListener?.onPageDragStateChanged(isDrag)
    }

    private fun setHeaderComponent(element: BannerDataModel) {
        if (element.channelModel?.channelHeader?.name?.isNotEmpty() == true) {
            element.channelModel.let {
                itemView.home_component_header_view.setChannel(element.channelModel, object : HeaderListener {
                    override fun onSeeAllClick(link: String) {
                        bannerListener?.onPromoAllClick(element.channelModel)
                    }

                    override fun onChannelExpired(channelModel: ChannelModel) {
                        homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
                    }
                })
            }
            itemView.home_component_header_view.visible()
        } else {
            itemView.home_component_header_view.gone()
        }
    }

    private fun ChannelModel.convertToBannerItemModel(): List<BannerItemModel> {
        return try {
            this.channelGrids.map{ BannerItemModel(it.id.toInt(), it.imageUrl) }
        } catch (e: NumberFormatException) {
            listOf()
        }
    }

    private fun ChannelModel.selectGridInPosition(position: Int, action: (ChannelGrid) -> Unit = {}): ChannelGrid? {
        return if (position != -1 && this.channelGrids.size > position) {
            action.invoke(this.channelGrids[position])
            this.channelGrids[position]
        } else null
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_banner
    }
}
