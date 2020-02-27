package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.LocalLoad

@SuppressLint("SyntheticAccessor")
class NewBusinessViewHolder(view: View, private val listener: HomeCategoryListener) : AbstractViewHolder<NewBusinessUnitWidgetDataModel>(view){
    private val errorBuWidget = view.findViewById<LocalLoad>(R.id.error_bu_widget)
    private val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
    private val containerUnify= view.findViewById<ContainerUnify>(R.id.container_unify)
    private val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
    private val loadingView = view.findViewById<LinearLayout>(R.id.loading_view)
    private var model: NewBusinessUnitWidgetDataModel? = null
    private val adapterBusinessWidget = BusinessUnitAdapter(object: NewBusinessUnitViewHolder.BusinessUnitListener{
        override fun getBusinessUnit(position: Int) {
            if(model?.tabList != null && (model?.tabList?.size ?: -1) > tabLayout.selectedTabPosition){
                model?.tabList?.get(tabLayout.selectedTabPosition)?.id?.let{
                    listener.getBusinessUnit(it, position)
                }
            }
        }
    })

    private val tabChangeListener = object : BaseOnTabSelectedListener<TabLayout.Tab>{
        override fun onTabReselected(p0: TabLayout.Tab?) {}

        override fun onTabUnselected(p0: TabLayout.Tab?) {}


        override fun onTabSelected(tab: TabLayout.Tab) {
            BusinessUnitTracking.getPageSelected(tab.text.toString())
            viewPager.currentItem = tab.position
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
    }

    override fun bind(element: NewBusinessUnitWidgetDataModel?) {
        loadingView.show()
        errorBuWidget.hide()
        tabLayout.show()
        viewPager.show()

        model = element
//        if(element?.backColor?.isNotEmpty() == true){
//            initContainerColor(element.backColor)
//        }

        if(element?.tabList == null) listener.getTabBusinessWidget(adapterPosition)

//        if(element?.tabList != null && tabLayout.tabCount < 1){
//            initTabLayout(element.tabList)
//        }
//
//        if(element?.contentsList != null){
//            initViewPager(element.contentsList)
//        }
    }

    override fun bind(element: NewBusinessUnitWidgetDataModel?, payloads: MutableList<Any>) {
        try {
            model = element
            if (payloads.isNotEmpty() && payloads.getOrNull(0) is Bundle) {
                val bundle = (payloads.first() as Bundle)
                if(bundle.containsKey(ERROR_BUNDLE_TAB_LAYOUT)){
                    errorBuWidget.show()
                    tabLayout.hide()
                    viewPager.hide()
                    loadingView.hide()
                } else if(bundle.containsKey(UPDATE_BUNDLE_TAB_LAYOUT)){
                    errorBuWidget.hide()
                    loadingView.hide()
                    tabLayout.show()
                    viewPager.show()
                    if (element?.tabList != null) {
                        tabLayout.removeAllTabs()
                        initTabLayout(element.tabList)
                    }
                    if(element?.contentsList != null){
                        initViewPager(element.contentsList)
                    }
                    if(element?.backColor?.isNotEmpty() == true){
                        initContainerColor(element.backColor)
                    }
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
            loadingView.hide()
            tabLayout.hide()
            viewPager.hide()
        }
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

    private fun initTabLayout(tabList: List<HomeWidget.TabItem>){
        tabLayout.show()
        tabList.forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it.name))
        }
        tabLayout.addOnTabSelectedListener(tabChangeListener)
    }

    private fun initViewPager(list: List<BusinessUnitDataModel>){
        adapterBusinessWidget.setItemList(list)
    }

    companion object {
        val LAYOUT = R.layout.layout_business_unit_widget

        private const val BLUE = "blue"
        private const val YELLOW = "yellow"
        private const val RED = "red"
        private const val GREEN = "green"
    }
}