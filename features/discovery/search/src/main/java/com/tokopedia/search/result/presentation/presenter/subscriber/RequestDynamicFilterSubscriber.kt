package com.tokopedia.search.result.presentation.presenter.subscriber

import com.tokopedia.discovery.common.data.DynamicFilterModel
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

        requestDynamicFilterListener.renderDynamicFilter(dynamicFilterModel)
    }

    override fun onCompleted() { }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        requestDynamicFilterListener.renderFailRequestDynamicFilter()
    }
}