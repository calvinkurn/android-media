package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.BusinessUnitTracking
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.BusinessUnitAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel.Companion.ERROR_BUNDLE_CONTENT_LAYOUT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel.Companion.ERROR_BUNDLE_TAB_LAYOUT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel.Companion.UPDATE_BUNDLE_CONTENT_LAYOUT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel.Companion.UPDATE_BUNDLE_TAB_LAYOUT
import com.tokopedia.home.beranda.presentation.view.helper.HomeChannelWidgetUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.LocalLoad
import kotlinx.android.synthetic.main.layout_business_unit_widget.view.*
import java.util.*

@SuppressLint("SyntheticAccessor")
class NewBusinessViewHolder(view: View, private val listener: HomeCategoryListener) : AbstractViewHolder<NewBusinessUnitWidgetDataModel>(view){
    private val errorBuWidget = view.findViewById<LocalLoad>(R.id.error_bu_widget)
    private val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
    private val containerUnify= view.findViewById<ContainerUnify>(R.id.container_unify)
    private val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
    private val loadingView = view.findViewById<LinearLayout>(R.id.loading_view)
    private val loadingGridView = view.findViewById<LinearLayout>(R.id.grid_loading_layout)
    private var model: NewBusinessUnitWidgetDataModel? = null
    private var performanceMonitoring: PerformanceMonitoring? = null
    private val performanceTraceName = "mp_home_business_unit_widget_load_time"
    private val adapterBusinessWidget = BusinessUnitAdapter(object: NewBusinessUnitViewHolder.BusinessUnitListener{
        override fun getBusinessUnit(position: Int) {
            if(model?.tabList != null && (model?.tabList?.size ?: -1) > position){
                model?.tabList?.get(position)?.let {
                    listener.getBusinessUnit(it.id, position, it.name)
                }
            }
        }

        override fun sendEnhanceEcommerce(tracker: HashMap<String, Any>) {
            listener.sendEETracking(tracker)
        }

        override fun putEnhanceEcommerce(tracker: HashMap<String, Any>) {
            listener.putEEToTrackingQueue(tracker)
        }
    })

    private val tabChangeListener = object : BaseOnTabSelectedListener<TabLayout.Tab>{
        override fun onTabReselected(p0: TabLayout.Tab?) {}

        override fun onTabUnselected(p0: TabLayout.Tab?) {}


        override fun onTabSelected(tab: TabLayout.Tab) {
            listener.sendEETracking(BusinessUnitTracking.getPageSelected(tab.text.toString()) as HashMap<String, Any>)
            viewPager.setCurrentItem(tab.position, false)
        }
    }

    init {
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        viewPager.isUserInputEnabled = false
        viewPager.adapter = adapterBusinessWidget
        errorBuWidget.title?.text = itemView.context.getString(R.string.widget_gagal_ditampilkan)
        errorBuWidget.refreshBtn?.setOnClickListener {
            listener.getTabBusinessWidget(adapterPosition)
        }
        performanceMonitoring = PerformanceMonitoring()
    }

    override fun bind(element: NewBusinessUnitWidgetDataModel?) {
        setChannelDivider(element)
        adapterBusinessWidget.setPositionWidgetOnHome(adapterPosition)
        performanceMonitoring?.startTrace(performanceTraceName)
        showLoading()
        errorBuWidget.hide()
        tabLayout.show()
        viewPager.show()

        model = element

        if(element?.tabList == null) listener.getTabBusinessWidget(adapterPosition)
        else hideLoading()
        if(element?.tabList != null && tabLayout.tabCount < 1){
            initTabLayout(element.tabList)
            initContainerColor(element.backColor)
            performanceMonitoring?.stopTrace()
            performanceMonitoring = null
        }
        if(element?.contentsList != null){
            initViewPager(element.contentsList)
        }
    }

    override fun bind(element: NewBusinessUnitWidgetDataModel?, payloads: MutableList<Any>) {
        try {
            adapterBusinessWidget.setPositionWidgetOnHome(adapterPosition)
            model = element
            if (payloads.isNotEmpty() && payloads.getOrNull(0) is Bundle) {
                val bundle = (payloads.first() as Bundle)
                if(bundle.containsKey(ERROR_BUNDLE_TAB_LAYOUT)){
                    errorBuWidget.show()
                    tabLayout.hide()
                    viewPager.hide()
                    hideLoading()
                } else if(bundle.containsKey(UPDATE_BUNDLE_TAB_LAYOUT)){
                    hideLoading()
                    errorBuWidget.hide()
                    tabLayout.show()
                    viewPager.show()
                    if (element?.tabList != null) {
                        clearTabLayout()
                        initTabLayout(element.tabList)
                    }
                    if(element?.contentsList != null){
                        initViewPager(element.contentsList)
                    }
                    if(element?.backColor?.isNotEmpty() == true){
                        initContainerColor(element.backColor)
                    }
                    performanceMonitoring?.stopTrace()
                    performanceMonitoring = null
                } else if(bundle.containsKey(UPDATE_BUNDLE_CONTENT_LAYOUT)){
                    if(element?.contentsList != null){
                        initViewPager(element.contentsList)
                    }
                } else if(bundle.containsKey(ERROR_BUNDLE_CONTENT_LAYOUT)){
                    initViewPager(listOf())
                }
            }
        }catch (e: Exception){
            errorBuWidget.show()
            tabLayout.hide()
            viewPager.hide()
            hideLoading()
        }
    }

    private fun setChannelDivider(element: NewBusinessUnitWidgetDataModel?) {
        HomeChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element?.channel,
            dividerTop = itemView.home_component_divider_header,
            dividerBottom = itemView.home_component_divider_footer
        )
    }

    private fun initContainerColor(color: String){
        containerUnify.setContainerColor(
                when (color) {
                    RED -> ContainerUnify.RED
                    BLUE -> ContainerUnify.BLUE
                    YELLOW -> ContainerUnify.YELLOW
                    GREEN -> ContainerUnify.GREEN
                    else -> ContainerUnify.RED
                }
        )
    }

    private fun clearTabLayout(){
        tabLayout.removeAllTabs()
    }

    private fun initTabLayout(tabList: List<HomeWidget.TabItem>){
        tabLayout.show()
        if(tabLayout.tabCount == 0) {
            tabList.forEach {
                tabLayout.addTab(tabLayout.newTab().setText(it.name))
            }
            tabLayout.addOnTabSelectedListener(tabChangeListener)
        }
    }

    private fun initViewPager(list: List<BusinessUnitDataModel>){
        adapterBusinessWidget.setItemList(list)
    }
    
    private fun showLoading(){
        loadingGridView.show()
        loadingView.show()
    }
    
    private fun hideLoading(){
        loadingGridView.hide()
        loadingView.hide()
    }

    companion object {
        val LAYOUT = R.layout.layout_business_unit_widget

        private const val BLUE = "blue"
        private const val YELLOW = "yellow"
        private const val RED = "red"
        private const val GREEN = "green"
    }
}