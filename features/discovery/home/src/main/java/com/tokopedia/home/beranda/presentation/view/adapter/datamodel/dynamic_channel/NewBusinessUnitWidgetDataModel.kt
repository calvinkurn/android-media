package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.analytics.v2.BaseTracking
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.BusinessWidgetTypeFactory

data class NewBusinessUnitWidgetDataModel(
        private var isCache: Boolean = false,
        val position: Int = -1,
        private var trackingData: Map<String, Any>? = null,
        private var trackingDataForCombination: List<Any>? = null,
        private var isCombined: Boolean = false,
        val backColor: String = "",
        val tabList: List<HomeWidget.TabItem>? = null,
        val contentsList: List<BusinessUnitDataModel>? = null
) : HomeVisitable{
    override fun isTrackingCombined(): Boolean {
        return isCombined
    }

    override fun isCache(): Boolean {
        return isCache
    }

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {
        this.trackingData = trackingData
    }

    override fun equalsWith(b: Any?): Boolean {
        return b is NewBusinessUnitWidgetDataModel && b.tabList === tabList && b.backColor == backColor && b.contentsList === contentsList
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return trackingDataForCombination
    }

    override fun getTrackingData(): Map<String, Any>? {
        return trackingData
    }

    override fun setTrackingCombined(isCombined: Boolean) {
        this.isCombined = isCombined
    }

    override fun getChangePayloadFrom(b: Any?): Bundle {
        val bundle = Bundle()
        if(b is NewBusinessUnitWidgetDataModel){
            if (b.tabList?.isEmpty() == true){
                bundle.putBoolean(ERROR_BUNDLE_TAB_LAYOUT, true)
            } else if(b.tabList !== tabList || b.tabList?.size !== tabList?.size){
                bundle.putBoolean(UPDATE_BUNDLE_TAB_LAYOUT, true)
            } else if(b.contentsList?.isEmpty() == true){
                bundle.putBoolean(ERROR_BUNDLE_CONTENT_LAYOUT, true)
            } else if(b.contentsList !== contentsList || b.contentsList?.size !== tabList?.size){
                bundle.putBoolean(UPDATE_BUNDLE_CONTENT_LAYOUT, true)
            }
        }

        return bundle
    }

    override fun setTrackingDataForCombination(trackingDataForCombination: MutableList<Any>?) {
        this.trackingDataForCombination = trackingDataForCombination
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return "buWidgetSection"
    }

    companion object{
        const val ERROR_BUNDLE_TAB_LAYOUT = "error_tab_bundle"
        const val UPDATE_BUNDLE_TAB_LAYOUT = "update_tab_bundle"
        const val UPDATE_BUNDLE_CONTENT_LAYOUT = "update_content_bundle"
        const val ERROR_BUNDLE_CONTENT_LAYOUT = "error_content_bundle"
    }
}

data class BusinessUnitDataModel(
        val tabName: String = "",
        val tabPosition: Int,
        val list: List<BusinessUnitItemDataModel>? = null
)

data class BusinessUnitItemDataModel(
        val content: HomeWidget.ContentItemTab,
        val itemPosition: Int
): BaseTracking.Promotion(content.contentId.toString(), content.contentName, "", content.imageUrl, itemPosition.toString()), Visitable<BusinessWidgetTypeFactory> {
    override fun type(typeFactory: BusinessWidgetTypeFactory?): Int {
        return typeFactory!!.type(this.content)
    }
}