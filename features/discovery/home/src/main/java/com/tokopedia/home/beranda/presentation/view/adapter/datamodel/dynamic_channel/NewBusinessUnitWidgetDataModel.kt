package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class NewBusinessUnitWidgetDataModel(
    val channel : DynamicHomeChannel.Channels = DynamicHomeChannel.Channels(),
    private val isCache: Boolean = false,
    val position: Int = -1,
    val backColor: String = "",
    val tabList: List<HomeWidget.TabItem>? = null,
    val contentsList: List<BusinessUnitDataModel>? = null
) : HomeVisitable{
    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun isCache(): Boolean {
        return isCache
    }

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {}

    override fun equalsWith(b: Any?): Boolean {
        return b is NewBusinessUnitWidgetDataModel && b.tabList === tabList && b.backColor == backColor && b.contentsList === contentsList && isCache == b.isCache()
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return null
    }

    override fun getTrackingData(): Map<String, Any>? {
        return null
    }

    override fun setTrackingCombined(isCombined: Boolean) {}

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

    override fun setTrackingDataForCombination(trackingDataForCombination: MutableList<Any>?) {}

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
        val itemPosition: Int,
        val tabPosition: Int,
        val tabName: String
): ImpressHolder()