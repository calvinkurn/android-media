package com.tokopedia.inbox.fake.di.notifcenter

import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifOrderListUseCase
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.domain.NotifOrderListUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class FakeNotifcenterUsecase {

    @Provides
    @NotificationScope
    fun provideNotifOrderListUseCase(
        fake: FakeNotifOrderListUseCase
    ): NotifOrderListUseCase {
        return fake
    }

    @Provides
    @NotificationScope
    fun provideFakeNotifOrderListUseCase(
        @Named(NotifOrderListUseCase.QUERY_ORDER_LIST)
        query: String,
        gqlUseCase: FakeGraphqlUseCase<NotifOrderListResponse>,
        cacheManager: NotifcenterCacheManager,
        userSession: UserSessionInterface
    ): FakeNotifOrderListUseCase {
        return FakeNotifOrderListUseCase(
            query, gqlUseCase, cacheManager, userSession
        )
    }

}