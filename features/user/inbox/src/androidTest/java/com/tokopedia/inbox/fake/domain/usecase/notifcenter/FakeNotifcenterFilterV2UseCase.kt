package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.domain.NotifcenterFilterV2UseCase

class FakeNotifcenterFilterV2UseCase(
    private val gqlUseCase: FakeGraphqlUseCase<NotifcenterFilterResponse>,
    cacheManager: NotifcenterCacheManager
) : NotifcenterFilterV2UseCase(gqlUseCase, cacheManager) {

    var response = NotifcenterFilterResponse()
        set(value) {
            field = value
            gqlUseCase.response = value
        }

    init {
        response = response
    }

}