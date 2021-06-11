package com.tokopedia.inbox.fake

import com.tokopedia.inbox.common.AndroidFileUtil
import com.tokopedia.inbox.domain.data.notification.InboxNotificationResponse
import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.cache.FakeInboxCacheManager
import com.tokopedia.inbox.fake.domain.usecase.FakeInboxNotificationUseCase
import com.tokopedia.inbox.test.R
import javax.inject.Inject

class InboxFakeDependency @Inject constructor(
    val inboxNotificationUseCase: FakeInboxNotificationUseCase,
    val userSession: FakeUserSession,
    val inboxCacheManager: FakeInboxCacheManager,
) {

    val defaultNotifCounter: InboxNotificationResponse = AndroidFileUtil.parseRaw(
        R.raw.inbox_notif_counter, InboxNotificationResponse::class.java
    )

    fun init() {
        initResponse()
    }

    private fun initResponse() {
        inboxNotificationUseCase.response = defaultNotifCounter
    }
}