package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.view.View
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.PopularKeywordTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.popularkeyword.PopularKeywordAdapter
import com.tokopedia.home_component.util.invertIfDarkMode
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by yoasfs on 2020-02-18
 */

class PopularKeywordViewHolder (val view: View,
                                val homeCategoryListener: HomeCategoryListener,
                                private val popularKeywordListener: PopularKeywordListener)
    : AbstractViewHolder<PopularKeywordListDataModel>(view) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_popular_keyword
    }

    private var performanceMonitoring: PerformanceMonitoring? = null
    private val performanceTraceName = "mp_home_popular_keyword_widget_load_time"

    private var adapter: PopularKeywordAdapter? = null
    var channelTitle: Typography? = null
    var tvReload: Typography? = null
    var ivReload: AppCompatImageView? = null
    var loadingView: View? = null
    private val errorPopularKeyword = view.findViewById<LocalLoad>(R.id.error_popular_keyword)
    private val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    private val recyclerView = view.findViewById<RecyclerView>(R.id.rv_popular_keyword)

    init{
        rotateAnimation.duration = 500
        rotateAnimation.interpolator = LinearInterpolator()
        performanceMonitoring = PerformanceMonitoring()
    }

    override fun bind(element: PopularKeywordListDataModel) {
        performanceMonitoring?.startTrace(performanceTraceName)
        homeCategoryListener.sendIrisTrackerHashMap(PopularKeywordTracking.getPopularKeywordImpressionIris(element.channel, element.popularKeywordList, adapterPosition) as HashMap<String, Any>)

        initStub(element)
        initAdapter(element)
    }

    override fun bind(element: PopularKeywordListDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun initAdapter(element: PopularKeywordListDataModel) {
        if(adapter == null) {
            adapter = PopularKeywordAdapter(popularKeywordListener, homeCategoryListener, element.channel, adapterPosition)
            recyclerView.layoutManager = GridLayoutManager(view.context, 2)
            recyclerView.adapter = adapter
        }
        adapter?.submitList(element.popularKeywordList)
        if(element.isErrorLoad) recyclerView.hide()
        else recyclerView.visible()
        performanceMonitoring?.stopTrace()
        performanceMonitoring = null
    }

    private fun initStub(element: PopularKeywordListDataModel) {
        try {
            val channelTitleStub: View? = itemView.findViewById(R.id.channel_title)
            val tvReloadStub: View? = itemView.findViewById(R.id.tv_reload)
            val ivReloadStub: View? = itemView.findViewById(R.id.iv_reload)
            val loadingViewStub: View? = itemView.findViewById(R.id.loading_popular)

            loadingViewStub?.let {
                initLoadingView()
                if(element.popularKeywordList.isEmpty()){
                    loadingView?.show()
                }else {
                    loadingView?.gone()
                }
            }

            channelTitleStub?.let {
                if (element.channel.header.name.isNotEmpty()) {
                    it.visibility = View.VISIBLE
                    channelTitle = if (channelTitleStub is ViewStub &&
                            !isViewStubHasBeenInflated(channelTitleStub)) {
                        val stubChannelView = channelTitleStub.inflate()
                        stubChannelView?.findViewById(R.id.channel_title)
                    } else {
                        itemView.findViewById(R.id.channel_title)
                    }
                    channelTitle?.text = element.channel.header.name
                    channelTitle?.visibility = View.VISIBLE
                    channelTitle?.setTextColor(
                            if(element.channel.header.textColor.isNotEmpty()) Color.parseColor(element.channel.header.textColor).invertIfDarkMode(itemView.context)
                            else ContextCompat.getColor(view.context, R.color.Unify_N700).invertIfDarkMode(itemView.context)
                    )
                } else {
                    it.visibility = View.GONE
                }
            }
            tvReloadStub?.let {
                it.visibility = View.VISIBLE
                tvReload = if (tvReloadStub is ViewStub &&
                        !isViewStubHasBeenInflated(tvReloadStub)) {
                    val stubChannelView = tvReloadStub.inflate()
                    stubChannelView?.findViewById(R.id.tv_reload)
                } else {
                    itemView.findViewById(R.id.tv_reload)
                }
                tvReload?.setOnClickListener(reloadClickListener(element))
            }
            ivReloadStub?.let {
                it.visibility = View.VISIBLE
                ivReload = if (ivReloadStub is ViewStub &&
                        !isViewStubHasBeenInflated(ivReloadStub)) {
                    val stubChannelView = ivReloadStub.inflate()
                    stubChannelView?.findViewById(R.id.iv_reload)
                } else {
                    itemView.findViewById(R.id.iv_reload)
                }
                ivReload?.setOnClickListener(reloadClickListener(element))
            }
            if(!element.isErrorLoad) errorPopularKeyword?.hide()
            else errorPopularKeyword.show()
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
            ivReload?.startAnimation(rotateAnimation)
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
        fun onPopularKeywordItemClicked(applink: String, channel: DynamicHomeChannel.Channels, position: Int, keyword: String,  positionInWidget: Int)
        fun onPopularKeywordItemImpressed(channel: DynamicHomeChannel.Channels, position: Int, keyword: String,  positionInWidget: Int)
    }
}