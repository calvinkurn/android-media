package com.tokopedia.inbox.fake

import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifOrderListUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterCacheManager
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterDetailUseCase
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

    fun init() {
//        initResponse()
    }

}