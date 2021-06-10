package com.tokopedia.inbox.fake

import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterCacheManager
import javax.inject.Inject

class InboxNotifcenterFakeDependency {

    @Inject
    lateinit var userSession: FakeUserSession

    @Inject
    lateinit var cacheManager: FakeNotifcenterCacheManager

    fun init() {
//        initResponse()
    }

}