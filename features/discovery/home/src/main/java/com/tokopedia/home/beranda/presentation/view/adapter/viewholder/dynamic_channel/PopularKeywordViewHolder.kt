package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.PopularKeywordTracking
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.popularkeyword.PopularKeywordAdapter
import com.tokopedia.home.beranda.presentation.view.helper.HomeChannelWidgetUtil
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.DynamicChannelTabletConfiguration
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.LocalLoad

/**
 * @author by yoasfs on 2020-02-18
 */

class PopularKeywordViewHolder (val view: View,
                                val homeCategoryListener: HomeCategoryListener,
                                private val popularKeywordListener: PopularKeywordListener,
                                private val cardInteraction: Boolean = false
) : AbstractViewHolder<PopularKeywordListDataModel>(view) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_popular_keyword
        private const val ROTATE_TO_DEGREES = 360f
        private const val PIVOT_X_VALUE = 0.5f
        private const val PIVOT_Y_VALUE = 0.5f
        private const val ROTATE_FROM_DEGREES = 0f
        private const val ROTATE_DURATION = 500L
    }

    private var performanceMonitoring: PerformanceMonitoring? = null
    private val performanceTraceName = "mp_home_popular_keyword_widget_load_time"

    private var adapter: PopularKeywordAdapter? = null
    var loadingView: View? = null

    private val errorPopularKeyword = view.findViewById<LocalLoad>(R.id.error_popular_keyword)
    private val rotateAnimation = RotateAnimation(ROTATE_FROM_DEGREES, ROTATE_TO_DEGREES, Animation.RELATIVE_TO_SELF, PIVOT_X_VALUE, Animation.RELATIVE_TO_SELF, PIVOT_Y_VALUE)
    private val recyclerView = view.findViewById<RecyclerView>(R.id.rv_popular_keyword)
    private val homeComponentDividerHeader = view.findViewById<DividerUnify>(R.id.home_component_divider_header)
    private val homeComponentDividerFooter = view.findViewById<DividerUnify>(R.id.home_component_divider_footer)
    private val headerView: DynamicChannelHeaderView by lazy { view.findViewById(R.id.home_component_header_view) }


    init{
        rotateAnimation.duration = ROTATE_DURATION
        rotateAnimation.interpolator = LinearInterpolator()
        performanceMonitoring = PerformanceMonitoring()
    }

    override fun bind(element: PopularKeywordListDataModel) {
        performanceMonitoring?.startTrace(performanceTraceName)
        homeCategoryListener.sendIrisTrackerHashMap(PopularKeywordTracking.getPopularKeywordImpressionIris(element.channel, element.popularKeywordList, adapterPosition) as HashMap<String, Any>)

        initStub(element)
        initAdapter(element)
        setChannelDivider(element)
    }

    override fun bind(element: PopularKeywordListDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(element: PopularKeywordListDataModel) {
        HomeChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channel,
            dividerTop = homeComponentDividerHeader,
            dividerBottom = homeComponentDividerFooter
        )
    }

    private fun initAdapter(element: PopularKeywordListDataModel) {
        if(adapter == null) {
            adapter = PopularKeywordAdapter(popularKeywordListener, homeCategoryListener, element.channel, adapterPosition, cardInteraction)
            val spanCount = DynamicChannelTabletConfiguration.getSpanCountFor2x2(itemView.context)
            recyclerView.layoutManager = GridLayoutManager(view.context, spanCount)
            recyclerView.adapter = adapter
        }
        adapter?.submitList(element.popularKeywordList)
        if(element.isErrorLoad || element.popularKeywordList.isEmpty()) recyclerView.gone()
        else recyclerView.visible()
        performanceMonitoring?.stopTrace()
        performanceMonitoring = null
    }

    @SuppressLint("ResourcePackage")
    private fun initStub(element: PopularKeywordListDataModel) {
        try {
            val loadingViewStub: View? = itemView.findViewById(R.id.loading_popular)

            loadingViewStub?.let {
                initLoadingView()
                if(element.popularKeywordList.isEmpty()){
                    loadingView?.show()
                }else {
                    loadingView?.gone()
                }
            }

            headerView.let {
                val title = element.title.ifEmpty { element.channel.header.name }
                if (title.isNotEmpty()) {
                    it.show()
                    val channel = element.channel.copy(header = element.channel.header.copy(name = element.title.ifEmpty { element.channel.header.name }, subtitle = element.subTitle))
                    it.setChannel(
                        channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(channel, element.position),
                        listener = object : HeaderListener {
                            override fun onReloadClick(channelModel: ChannelModel) {
                                loadingView?.show()
                                errorPopularKeyword.hide()
                                adapter?.clearList()
                                popularKeywordListener.onPopularKeywordSectionReloadClicked(element.position, element.channel)
                            }
                        }
                    )
                }
            }
            if(!element.isErrorLoad) {
                errorPopularKeyword?.hide()
                headerView.show()
            } else {
                errorPopularKeyword.show()
                headerView.hide()
                loadingView?.hide()
            }

            errorPopularKeyword.progressState = false
            errorPopularKeyword?.refreshBtn?.setOnClickListener(reloadClickListener(element))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }

    private fun reloadClickListener(element: PopularKeywordListDataModel): View.OnClickListener {
        return View.OnClickListener {
            loadingView?.show()
            errorPopularKeyword.hide()
            adapter?.clearList()
            errorPopularKeyword.progressState = true
            popularKeywordListener.onPopularKeywordSectionReloadClicked(element.position, element.channel)
        }
    }

    private fun initLoadingView(){
        val loadingViewStub: View? = itemView.findViewById(R.id.loading_popular)
        loadingView = if (loadingViewStub is ViewStub &&
                !isViewStubHasBeenInflated(loadingViewStub)) {
            val stubView = loadingViewStub.inflate()
            stubView?.findViewById(R.id.loading_popular)
        } else {
            itemView.findViewById(R.id.loading_popular)
        }
    }

    interface PopularKeywordListener {
        fun onPopularKeywordSectionReloadClicked(position: Int, channel: DynamicHomeChannel.Channels)
        fun onPopularKeywordItemClicked(applink: String, channel: DynamicHomeChannel.Channels, position: Int, popularKeywordDataModel: PopularKeywordDataModel,  positionInWidget: Int)
        fun onPopularKeywordItemImpressed(channel: DynamicHomeChannel.Channels, position: Int, popularKeywordDataModel: PopularKeywordDataModel,  positionInWidget: Int)
    }
}
