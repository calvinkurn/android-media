package com.tokopedia.loginregister.login.behaviour.data

import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.data.CloudDiscoverDataSource
import com.tokopedia.loginregister.login.data.LocalDiscoverDataSource
import com.tokopedia.loginregister.login.view.model.DiscoverDataModel
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class DiscoverUseCaseStub(
        cloudDiscoverDataSource: CloudDiscoverDataSource,
        localDiscoverDataSource: LocalDiscoverDataSource
): DiscoverUseCase(cloudDiscoverDataSource, localDiscoverDataSource) {

    var response = DiscoverDataModel(arrayListOf(), "")

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<DiscoverDataModel>?) {
        subscriber?.onNext(response)
    }
}