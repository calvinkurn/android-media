package com.tokopedia.inbox.fake.di.notifcenter

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifOrderListUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterDetailUseCase
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.domain.NotifOrderListUseCase
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
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

    // -- separator -- //

    @Provides
    @NotificationScope
    fun provideNotifcenterDetailUseCase(
        fake: FakeNotifcenterDetailUseCase
    ): NotifcenterDetailUseCase {
        return fake
    }

    @Provides
    @NotificationScope
    fun provideFakeNotifcenterDetailUseCase(
        @Named(NotifcenterDetailUseCase.QUERY_NOTIFCENTER_DETAIL_V3)
        query: String,
        gqlUseCase: FakeGraphqlUseCase<NotifcenterDetailResponse>,
        mapper: NotifcenterDetailMapper,
        dispatchers: CoroutineDispatchers
    ): FakeNotifcenterDetailUseCase {
        return FakeNotifcenterDetailUseCase(
            query, gqlUseCase, mapper, dispatchers
        )
    }
}