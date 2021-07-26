package com.tokopedia.inbox.fake.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.di.InboxScope
import com.tokopedia.inbox.domain.data.notification.InboxNotificationResponse
import com.tokopedia.inbox.domain.usecase.InboxNotificationUseCase
import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.inbox.fake.domain.usecase.FakeInboxNotificationUseCase
import dagger.Module
import dagger.Provides

@Module
class FakeInboxModule {

    @Provides
    @InboxScope
    fun provideInboxNotificationUseCase(
            stub: FakeInboxNotificationUseCase
    ): InboxNotificationUseCase = stub

    @Provides
    @InboxScope
    fun provideFakeInboxNotificationUseCase(
            gqlUseCase: FakeGraphqlUseCase<InboxNotificationResponse>,
            dispatchers: CoroutineDispatchers
    ): FakeInboxNotificationUseCase {
        return FakeInboxNotificationUseCase(gqlUseCase, dispatchers)
    }

    // -- separator -- //

}