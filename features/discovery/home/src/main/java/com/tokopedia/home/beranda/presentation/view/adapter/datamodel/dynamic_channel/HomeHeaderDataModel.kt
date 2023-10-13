package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.LoadableComponent
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class HomeHeaderDataModel(
    var needToShowChooseAddress: Boolean = true,
    var headerDataModel: HeaderDataModel? = HeaderDataModel()
) : ImpressHolder(),
    HomeVisitable,
    LoadableComponent by BlocksLoadableComponent(
        {
            if (headerDataModel?.isUserLogin == false) {
                true
            } else if (headerDataModel?.isUserLogin == true && headerDataModel.homeBalanceModel.balanceDrawerItemModels.isNotEmpty()) {
                headerDataModel.homeBalanceModel.balanceDrawerItemModels.all {
                    it.state != BalanceDrawerItemModel.STATE_LOADING
                }
            } else {
                false
            }
        },
        "HomeHeaderDataModel"
    ) {
    var createdTimeMillis = ""
    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var trackingDataForCombination: List<Any>? = null
    private var isCombined: Boolean = false

    override fun equalsWith(b: Any?): Boolean {
        return false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun isCache(): Boolean {
        return isCache
    }

    override fun visitableId(): String {
        return "homeBannerOvoDataModel"
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    // avoid setter and gettter for tracking from parent class, implement your tracker in viewholders
    @Deprecated("")
    override fun setTrackingData(trackingData: Map<String, Any>) {
        this.trackingData = trackingData
    }

    // avoid setter and gettter for tracking from parent class, implement your tracker in viewholders
    @Deprecated("")
    override fun getTrackingData(): Map<String, Any>? {
        return trackingData
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return trackingDataForCombination
    }

    override fun setTrackingDataForCombination(trackingDataForCombination: List<Any>) {
        this.trackingDataForCombination = trackingDataForCombination
    }

    override fun isTrackingCombined(): Boolean {
        return isCombined
    }

    override fun setTrackingCombined(isCombined: Boolean) {
        this.isCombined = isCombined
    }
}
