package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import com.tokopedia.inbox.common.AndroidFileUtil
import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.inbox.test.R
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

    val defaultResponse: NotifcenterFilterResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_filter_v2, NotifcenterFilterResponse::class.java
        )

    init {
        response = response
    }

    fun initialize() {
        this.response = defaultResponse
    }

}