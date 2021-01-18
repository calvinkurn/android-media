package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.decoration.BannerChannelDecoration
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.LinearCenterLayoutManager
import com.tokopedia.home_component.viewholders.adapter.BannerChannelAdapter
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.home_component_lego_banner.view.*
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
        CircularListener, CoroutineScope {
    private var isCache = true
    private val rvBanner: RecyclerView = itemView.findViewById(R.id.rv_banner)
    private val adapter = BannerChannelAdapter(listOf(), this)
    private val layoutManager = LinearCenterLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

    // tracker
    private val impressionStatusList = mutableMapOf<Int, Boolean>()

    private val masterJob = Job()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private var channelModel: ChannelModel? = null

    //set to true if you want to activate auto-scroll
    private var isAutoScroll = false
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
            setViewPortImpression(element)
            channelModel = element.channelModel
            isCache = element.isCache
            onPromoScrolled(layoutManager.findFirstCompletelyVisibleItemPosition())

            channelModel?.let { it ->
                this.isCache = element.isCache
                try {
                    initBanner(it.convertToCircularModel())
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
            })
        }
    }

    override fun bind(element: BannerDataModel, payloads: MutableList<Any>) {
        try {
            setHeaderComponent(element)
            setViewPortImpression(element)
            channelModel = element.channelModel
            this.isCache = element.isCache

            onPromoScrolled(layoutManager.findFirstCompletelyVisibleItemPosition())
            element.channelModel?.let {
                val gridCount = it.channelGrids.size
                adapter.setItemList(it.convertToCircularModel())
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private suspend fun autoScrollCoroutine() = withContext(Dispatchers.Main){
        if (isAutoScroll) {
            rvBanner.smoothScrollToPosition(currentPagePosition)

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

    private fun initBanner(list: List<CircularModel>){
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvBanner)
        rvBanner.layoutManager = layoutManager
        rvBanner.clearOnScrollListeners()
        rvBanner.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when(newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        onPromoScrolled(layoutManager.findFirstCompletelyVisibleItemPosition())
                        onPageDragStateChanged(false)
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        onPageDragStateChanged(true)
                        pauseAutoScroll()
                    }
                }
            }
        })

        if (rvBanner.itemDecorationCount == 0) {
            rvBanner.addItemDecoration(BannerChannelDecoration())
        }
        adapter.setItemList(list)
        rvBanner.adapter = adapter
    }

    override fun onClick(position: Int) {
        channelModel?.let {channel ->
            channel.selectGridInPosition(position) {
                bannerListener?.onBannerClickListener(position, it, channel)
            }
        }
    }

    private fun onPromoScrolled(position: Int) {
        if (bannerListener?.isMainViewVisible() == true && !isCache && !isBannerImpressed(position) && position != -1) {
            channelModel?.let {channel ->
                channel.selectGridInPosition(position) {
                    bannerListener.onPromoScrolled(channel, it ,position)
                }
                impressionStatusList.put(position, true)
            }
        }
    }

    private fun isBannerImpressed(position: Int): Boolean {
        return if (impressionStatusList.size > position && position != -1) {
            impressionStatusList[position]?:false
        } else false
    }

    private fun onPageDragStateChanged(isDrag: Boolean) {
        bannerListener?.onPageDragStateChanged(isDrag)
    }

    private fun setHeaderComponent(element: BannerDataModel) {
        element.channelModel?.let {
            itemView.home_component_header_view.setChannel(element.channelModel, object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                    bannerListener?.onPromoAllClick(link)
                }

                override fun onChannelExpired(channelModel: ChannelModel) {
                    homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
                }
            })
        }
    }

    fun resetImpression(){
        impressionStatusList.clear()
        layoutManager.scrollToPosition(0)
    }

    private fun ChannelModel.convertToCircularModel(): List<CircularModel> {
        return try {
            this.channelGrids.map{ CircularModel(it.id.toInt(), it.imageUrl) }
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
