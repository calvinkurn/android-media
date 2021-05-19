package com.tokopedia.inbox.fake.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.domain.data.notification.InboxNotificationResponse
import com.tokopedia.inbox.domain.usecase.InboxNotificationUseCase
import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase

class FakeInboxNotificationUseCase(
        private val gqlUseCase: FakeGraphqlUseCase<InboxNotificationResponse>,
        dispatchers: CoroutineDispatchers
) : InboxNotificationUseCase(gqlUseCase, dispatchers) {

    var response = InboxNotificationResponse()
        set(value) {
            field = value
            gqlUseCase.response = value
        }

    init {
        response = response
    }
}