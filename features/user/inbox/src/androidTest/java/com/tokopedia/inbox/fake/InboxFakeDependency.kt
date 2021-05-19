package com.tokopedia.inbox.fake

import com.tokopedia.inbox.fake.domain.usecase.FakeInboxNotificationUseCase
import javax.inject.Inject

class InboxFakeDependency @Inject constructor(
        val inboxNotificationUseCase: FakeInboxNotificationUseCase
) {
}