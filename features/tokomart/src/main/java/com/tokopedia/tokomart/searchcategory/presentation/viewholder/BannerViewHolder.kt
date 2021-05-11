package com.tokopedia.tokomart.searchcategory.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.base.adapter.BannerComponentAdapter
import com.tokopedia.tokomart.common.base.adapter.BannerItemListener
import com.tokopedia.tokomart.common.base.listener.BannerComponentListener
import com.tokopedia.tokomart.searchcategory.presentation.customview.PeekingLinearLayoutManager
import com.tokopedia.tokomart.searchcategory.presentation.itemdecoration.BannerComponentDecoration
import com.tokopedia.tokomart.searchcategory.presentation.itemdecoration.BannerComponentSingleItemDecoration
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class BannerViewHolder(
        itemView: View,
        private val bannerListener: BannerComponentListener?
): AbstractViewHolder<BannerDataView>(itemView), CoroutineScope, BannerItemListener {

    private var isCache = true
    private val rvBanner: RecyclerView = itemView.findViewById(R.id.rv_banner)
    private var layoutManager = LinearLayoutManager(itemView.context)

    private val masterJob = Job()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

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

    override fun bind(element: BannerDataView) {
        // ====== tokonow
        initBanner(listOf())
        // ====== home_component
//        try {
//            setHeaderComponent(element)
//            setViewPortImpression(element)
//            channelModel = element.channelModel
//            isCache = element.isCache
//
//            channelModel?.let { it ->
//                this.isCache = element.isCache
//                try {
//                    initBanner(it.convertToBannerItemModel())
//                } catch (e: NumberFormatException) {
//                    e.printStackTrace()
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    private fun setViewPortImpression(element: BannerDataView) {
//        if (!element.isCache) {
//            itemView.addOnImpressionListener(holder = element, onView = {
//                element.channelModel?.let {
//                    bannerListener?.onChannelBannerImpressed(it, adapterPosition)
//                }
//                setScrollListener()
//            })
//        }
    }

    override fun bind(element: BannerDataView, payloads: MutableList<Any>) {
        bind(element)
    }

    private suspend fun autoScrollCoroutine() = withContext(Dispatchers.Main){
        if (isAutoScroll) {
            rvBanner.smoothScrollToPosition(currentPagePosition)

//            channelModel?.let {
//                val size = channelModel?.channelGrids?.size?:0
//                if (currentPagePosition == (size-1) ) {
//                    currentPagePosition = 0
//                } else {
//                    currentPagePosition++
//                }
//            }
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

    private fun getLayoutManager(list: List<BannerDataView>): LinearLayoutManager {
        layoutManager = if (list.size == 1) {
            LinearLayoutManager(itemView.context)
        } else PeekingLinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        return layoutManager
    }

    private fun initBanner(list: List<BannerDataView>){
        val list = listOf(BannerDataView(
                "https://ecs7.tokopedia.net/img/cache/200-square/attachment/2019/7/23/20723472/20723472_5a07ff96-8ee1-4615-955c-f8290b070c0a.png"
        ))
        rvBanner.clearOnScrollListeners()

        val snapHelper: SnapHelper = PagerSnapHelper()
        rvBanner.onFlingListener = null
        snapHelper.attachToRecyclerView(rvBanner)
        rvBanner.layoutManager = getLayoutManager(list)
        rvBanner.removeAllItemDecoration()
        if (rvBanner.itemDecorationCount == 0) {
            if (list.size == 1) {
                rvBanner.addItemDecoration(BannerComponentSingleItemDecoration())
            } else rvBanner.addItemDecoration(BannerComponentDecoration())
        }
        val adapter = BannerComponentAdapter(list, this)
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
        // Do nothing
    }

    override fun onClick(position: Int) {
        bannerListener?.onBannerClick()
    }

    private fun onPromoScrolled(position: Int) {
        // Do nothing
    }

    private fun onPageDragStateChanged(isDrag: Boolean) {
        // Do nothing
    }

    private fun setHeaderComponent(element: BannerDataView) {
//        if (element.channelModel?.channelHeader?.name?.isNotEmpty() == true) {
//            element.channelModel.let {
//                itemView.home_component_header_view.setChannel(element.channelModel, object : HeaderListener {
//                    override fun onSeeAllClick(link: String) {
//                        bannerListener?.onPromoAllClick(element.channelModel)
//                    }
//
//                    override fun onChannelExpired(channelModel: ChannelModel) {
//                        homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
//                    }
//                })
//            }
//            itemView.home_component_header_view.visible()
//        } else {
//            itemView.home_component_header_view.gone()
//        }
    }

    private fun RecyclerView.removeAllItemDecoration() {
        if (this.itemDecorationCount > 0)
            for (i in 0 until this.itemDecorationCount) {
                this.removeItemDecorationAt(i)
            }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_search_category_banner
    }
}