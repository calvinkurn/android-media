package com.tokopedia.inbox.fake

import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.*
import javax.inject.Inject

class InboxNotifcenterFakeDependency {

    @Inject
    lateinit var userSession: FakeUserSession

    @Inject
    lateinit var cacheManager: FakeNotifcenterCacheManager

    @Inject
    lateinit var notifOrderListUseCase: FakeNotifOrderListUseCase

    @Inject
    lateinit var notifcenterDetailUseCase: FakeNotifcenterDetailUseCase

    @Inject
    lateinit var notifcenterFilterUseCase: FakeNotifcenterFilterV2UseCase

    @Inject
    lateinit var topAdsRepository: FakeTopAdsRepository

    fun init() {
//        initResponse()
    }

}