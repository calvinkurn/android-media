package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularPageChangeListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPager
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.viewholders.adapter.BannerChannelAdapter
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.unifycomponents.PageControl
import kotlinx.android.synthetic.main.home_component_lego_banner.view.*

/**
 * @author by devarafikry on 11/28/20.
 */

class BannerComponentViewHolder(itemView: View,
                                private val bannerListener: BannerComponentListener?,
                                private val homeComponentListener: HomeComponentListener?
)
    : AbstractViewHolder<BannerDataModel>(itemView),
        CircularListener {
    private var isCache = true
    private val circularViewPager: CircularViewPager = itemView.findViewById(R.id.circular_view_pager)
    private val indicatorView: PageControl = itemView.findViewById(R.id.indicator_banner)
    private val adapter = BannerChannelAdapter(listOf(), this)

    private var channelModel: ChannelModel? = null

    init {
        indicatorView.activeColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        indicatorView.inactiveColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0_32)
    }

    override fun bind(element: BannerDataModel) {
        try {
            setHeaderComponent(element)
//            slidesList = element.slides
//            slidesList?.let {
//                this.isCache = element.isCache
//                initSeeAllPromo()
//                initBanner(it.map{ CircularModel(it.id, it.imageUrl) })
//            }

            channelModel = element.channelModel
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

    override fun bind(element: BannerDataModel, payloads: MutableList<Any>) {
        try {
            setHeaderComponent(element)
//            slidesList = element.slides
//            this.isCache = element.isCache
//            element.slides?.let {
//                if (it.size > 5) {
//                    indicatorView.setIndicator(it.size)
//                } else {
//                    indicatorView.setIndicator(it.size)
//                }
//                circularViewPager.setItemList(it.map { CircularModel(it.id, it.imageUrl) })
//            }

            channelModel = element.channelModel
            this.isCache = element.isCache
            element.channelModel?.let {
                val gridCount = it.channelGrids.size
                if (gridCount > 5) {
                    indicatorView.setIndicator(gridCount)
                } else {
                    indicatorView.setIndicator(gridCount)
                }
                circularViewPager.setItemList(it.convertToCircularModel())
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun initBanner(list: List<CircularModel>){
        circularViewPager.setIndicatorPageChangeListener(object: CircularViewPager.IndicatorPageChangeListener{
            override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                indicatorView.setCurrentIndicator(newIndicatorPosition)
            }
        })

        circularViewPager.setPageChangeListener(object: CircularPageChangeListener {
            override fun onPageScrolled(position: Int) {
                onPromoScrolled(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                onPageDragStateChanged(state == CircularViewPager.SCROLL_STATE_DRAGGING)
            }
        })
        circularViewPager.setAdapter(adapter)
        circularViewPager.setItemList(list)
        if (list.size > 5) {
            indicatorView.setIndicator(list.size)
        } else {
            indicatorView.setIndicator(list.size)
        }
    }

    override fun onClick(position: Int) {
//        slidesList?.let {
//            if(it.size > position) {
//                listener?.onPromoClick(position, it[position])
//            }
//        }
        channelModel?.selectGridInPosition(position) {
            bannerListener?.onBannerClickListener(position, it)
        }
    }

    private fun onPromoScrolled(position: Int) {
        if (bannerListener?.isMainViewVisible() == true && !isCache) {
//            slidesList?.let {
//                listener?.onPromoScrolled(it[position])
//                it[position].invoke()
//            }
            channelModel?.selectGridInPosition(position) {
                bannerListener.onPromoScrolled(it)
            }
        }
    }

    private fun onPageDragStateChanged(isDrag: Boolean) {
        bannerListener?.onPageDragStateChanged(isDrag)
    }

    private fun onPromoAllClick(applink: String) {
        bannerListener?.onPromoAllClick(applink)
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

    fun onResume(){
        circularViewPager.resetScrollToStart()
        circularViewPager.resumeAutoScroll()
    }

    fun resetImpression(){
        circularViewPager.resetScrollToStart()
    }

    fun onPause(){
        circularViewPager.pauseAutoScroll()
    }

    private fun ChannelModel.convertToCircularModel(): List<CircularModel> {
        return try {
            this.channelGrids.map{ CircularModel(it.id.toInt(), it.imageUrl) }
        } catch (e: NumberFormatException) {
            listOf()
        }
    }

    private fun ChannelModel.selectGridInPosition(position: Int, action: (ChannelGrid) -> Unit = {}): ChannelGrid? {
        return if (this.channelGrids.size > position) {
            action.invoke(this.channelGrids[position])
            this.channelGrids[position]
        } else null
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_banner
    }
}
