package com.tokopedia.search.result.presentation.presenter.subscriber

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.discovery.common.data.Filter
import com.tokopedia.discovery.newdynamicfilter.helper.DynamicFilterDbManager
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener
import rx.Subscriber

open class RequestDynamicFilterSubscriber(
    private val requestDynamicFilterListener : RequestDynamicFilterListener
) : Subscriber<DynamicFilterModel>() {

    override fun onNext(dynamicFilterModel: DynamicFilterModel?) {
        if(dynamicFilterModel == null) {
            requestDynamicFilterListener.renderFailRequestDynamicFilter()
            return
        }

        if(requestDynamicFilterListener.shouldSaveToLocalDynamicFilterDb()) {
            storeLocalFilter(dynamicFilterModel)
        }

        requestDynamicFilterListener.renderDynamicFilter(dynamicFilterModel)
    }

    // Save DynamicFilterModel locally as temporary solution,
    // to prevent TransactionTooLargeException when opening RevampedDynamicFilterActivity
    // This method will not be tested with Unit Test
    private fun storeLocalFilter(model: DynamicFilterModel) {
        val listType = object : TypeToken<List<Filter>>() { }.type
        val gson = Gson()
        val filterData = gson.toJson(model.data.filter, listType)

        val cache = DynamicFilterDbManager()
        cache.filterID = requestDynamicFilterListener.screenNameId
        cache.filterData = filterData
        cache.store()
    }

    override fun onCompleted() { }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        requestDynamicFilterListener.renderFailRequestDynamicFilter()
    }
}