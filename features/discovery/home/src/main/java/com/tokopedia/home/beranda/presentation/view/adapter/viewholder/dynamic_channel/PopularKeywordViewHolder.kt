package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
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
import com.tokopedia.home_component.util.invertIfDarkMode
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component_header.util.HomeChannelHeaderRollenceController
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography

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
        private val RECYCLER_VIEW_DEFAULT_MARGIN = 12f.toDpInt()
    }

    private var performanceMonitoring: PerformanceMonitoring? = null
    private val performanceTraceName = "mp_home_popular_keyword_widget_load_time"

    private var adapter: PopularKeywordAdapter? = null
    var channelTitle: Typography? = null
    var tvReload: Typography? = null
    var ivReload: AppCompatImageView? = null
    var loadingView: View? = null
    var channelSubtitle: TextView? = null

    private val errorPopularKeyword = view.findViewById<LocalLoad>(R.id.error_popular_keyword)
    private val rotateAnimation = RotateAnimation(ROTATE_FROM_DEGREES, ROTATE_TO_DEGREES, Animation.RELATIVE_TO_SELF, PIVOT_X_VALUE, Animation.RELATIVE_TO_SELF, PIVOT_Y_VALUE)
    private val recyclerView = view.findViewById<RecyclerView>(R.id.rv_popular_keyword)
    private val homeComponentDividerHeader = view.findViewById<DividerUnify>(R.id.home_component_divider_header)
    private val homeComponentDividerFooter = view.findViewById<DividerUnify>(R.id.home_component_divider_footer)
    private val loaderPopularKeywordTitle = view.findViewById<LoaderUnify>(R.id.loader_popular_keyword_title)
    private val containerPopularKeyword = view.findViewById<ConstraintLayout>(R.id.container_popular_keyword)
    private val headerView: DynamicChannelHeaderView by lazy { view.findViewById(R.id.home_component_header_view) }

    private val isUsingHeaderRevamp = HomeChannelHeaderRollenceController.isHeaderUsingRollenceVariant()

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
            val channelTitleStub: View? = itemView.findViewById(R.id.channel_pk_title)
            val channelSubtitleStub: View? = itemView?.findViewById(com.tokopedia.home_component.R.id.channel_pk_subtitle)
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

            if(isUsingHeaderRevamp) {
                loaderPopularKeywordTitle?.hide()
                ivReload?.hide()
                tvReload?.hide()
                channelTitle?.hide()
                channelSubtitle?.hide()
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
                                    errorPopularKeyword.progressState = true
                                    popularKeywordListener.onPopularKeywordSectionReloadClicked(element.position, element.channel)
                                }
                            }
                        )
                        loaderPopularKeywordTitle?.gone()
                    }
                }
                if(!element.isErrorLoad) {
                    errorPopularKeyword?.hide()
                    headerView.show()
                } else {
                    errorPopularKeyword.show()
                    loaderPopularKeywordTitle?.hide()
                    headerView.hide()
                    loadingView?.hide()
                }
            }
            else {
                headerView.hide()
                channelTitleStub?.let {
                    val title = if (element.title.isNotEmpty()) element.title else element.channel.header.name
                    if (title.isNotEmpty()) {
                        it.visibility = View.VISIBLE
                        channelTitle = if (channelTitleStub is ViewStub &&
                            !isViewStubHasBeenInflated(channelTitleStub)) {
                            val stubChannelView = channelTitleStub.inflate()
                            stubChannelView?.findViewById(R.id.channel_pk_title)
                        } else {
                            itemView.findViewById(R.id.channel_pk_title)
                        }
                        channelTitle?.text = title
                        channelTitle?.visibility = View.VISIBLE
                        channelTitle?.setTextColor(
                            if(element.channel.header.textColor.isNotEmpty()) Color.parseColor(element.channel.header.textColor).invertIfDarkMode(itemView.context)
                            else ContextCompat.getColor(view.context, R.color.Unify_NN950).invertIfDarkMode(itemView.context)
                        )
                        loaderPopularKeywordTitle?.gone()
                        anchorReloadButtonTo(R.id.channel_pk_title)
                    }
                }
                /**
                 * Requirement:
                 * Only show channel subtitle when it is exist
                 */
                val channelSubtitleName = element.subTitle
                if (channelSubtitleName.isNotEmpty()) {
                    channelSubtitle = if (channelSubtitleStub is ViewStub &&
                        !isViewStubHasBeenInflated(channelSubtitleStub)) {
                        val stubChannelView = channelSubtitleStub.inflate()
                        stubChannelView?.findViewById(com.tokopedia.home_component.R.id.channel_pk_subtitle)
                    } else {
                        itemView?.findViewById(com.tokopedia.home_component.R.id.channel_pk_subtitle)
                    }
                    channelSubtitle?.text = channelSubtitleName
                    channelSubtitle?.visibility = View.VISIBLE

                    anchorReloadButtonTo(R.id.channel_pk_subtitle)
                } else {
                    channelSubtitle?.visibility = View.GONE
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
                if(!element.isErrorLoad) {
                    errorPopularKeyword?.hide()
                    channelTitle?.show()
                    ivReload?.show()
                    tvReload?.show()
                } else {
                    errorPopularKeyword.show()
                    loaderPopularKeywordTitle?.hide()
                    ivReload?.hide()
                    tvReload?.hide()
                    channelTitle?.hide()
                    channelSubtitle?.hide()
                    loadingView?.hide()
                }
            }
            adjustRecyclerView()

            errorPopularKeyword.progressState = false
            errorPopularKeyword?.refreshBtn?.setOnClickListener(reloadClickListener(element))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun anchorReloadButtonTo(anchorRef: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(containerPopularKeyword)
        constraintSet.connect(R.id.iv_reload, ConstraintSet.TOP, anchorRef, ConstraintSet.TOP, 0)
        constraintSet.connect(R.id.iv_reload, ConstraintSet.BOTTOM, anchorRef, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(containerPopularKeyword)
    }
    private fun adjustRecyclerView() {
        if(isUsingHeaderRevamp) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(containerPopularKeyword)
            constraintSet.connect(R.id.rv_popular_keyword, ConstraintSet.TOP, R.id.home_component_header_view, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.rv_popular_keyword, ConstraintSet.BOTTOM, R.id.container_popular_keyword, ConstraintSet.BOTTOM, 0)
            constraintSet.connect(R.id.rv_popular_keyword, ConstraintSet.LEFT, R.id.home_component_header_view, ConstraintSet.LEFT, 0)
            constraintSet.connect(R.id.rv_popular_keyword, ConstraintSet.RIGHT, R.id.home_component_header_view, ConstraintSet.RIGHT, 0)
            constraintSet.connect(R.id.loading_popular, ConstraintSet.TOP, R.id.container_popular_keyword, ConstraintSet.TOP, 0)
            constraintSet.applyTo(containerPopularKeyword)
            containerPopularKeyword.setPadding(containerPopularKeyword.paddingLeft, 0, containerPopularKeyword.paddingRight, containerPopularKeyword.paddingBottom)
            recyclerView.setMargin(recyclerView.marginLeft, 0, recyclerView.marginRight, recyclerView.marginBottom)
            loadingView?.let {
                it.setMargin(it.marginLeft, 0, it.marginRight, it.marginBottom)
            }
        } else {
            val constraintSet = ConstraintSet()
            constraintSet.clone(containerPopularKeyword)
            constraintSet.connect(R.id.rv_popular_keyword, ConstraintSet.TOP, R.id.channel_pk_subtitle, ConstraintSet.BOTTOM, 0)
            constraintSet.connect(R.id.rv_popular_keyword, ConstraintSet.BOTTOM, R.id.container_popular_keyword, ConstraintSet.BOTTOM, 0)
            constraintSet.connect(R.id.rv_popular_keyword, ConstraintSet.LEFT, R.id.channel_pk_title, ConstraintSet.LEFT, 0)
            constraintSet.connect(R.id.rv_popular_keyword, ConstraintSet.RIGHT, R.id.iv_reload, ConstraintSet.RIGHT, 0)
            constraintSet.connect(R.id.loading_popular, ConstraintSet.TOP, R.id.header_barrier, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(containerPopularKeyword)
            containerPopularKeyword.setPadding(containerPopularKeyword.paddingLeft, RECYCLER_VIEW_DEFAULT_MARGIN, containerPopularKeyword.paddingRight, containerPopularKeyword.paddingBottom)
            recyclerView.setMargin(recyclerView.marginLeft, RECYCLER_VIEW_DEFAULT_MARGIN, recyclerView.marginRight, recyclerView.marginBottom)
            loadingView?.let {
                it.setMargin(it.marginLeft, RECYCLER_VIEW_DEFAULT_MARGIN, it.marginRight, it.marginBottom)
            }
        }
    }

    private fun isViewStubHasBeenInflated(viewStub: ViewStub?): Boolean {
        return viewStub?.parent == null
    }

    private fun reloadClickListener(element: PopularKeywordListDataModel): View.OnClickListener {
        return View.OnClickListener {
            if (ivReload?.isVisible == true) {
                ivReload?.startAnimation(rotateAnimation)
            }

            if (channelTitle == null || channelTitle?.isVisible == false) {
                loaderPopularKeywordTitle?.visible()
            }
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
