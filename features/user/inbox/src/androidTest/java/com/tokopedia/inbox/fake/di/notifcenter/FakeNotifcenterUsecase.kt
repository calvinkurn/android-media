package com.tokopedia.inbox.fake.di.notifcenter

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifOrderListUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterCacheManager
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterDetailUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterFilterV2UseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.recom.FakeGetRecommendationUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.recom.FakeRecommendationGraphqlUseCase
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.domain.NotifOrderListUseCase
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.domain.NotifcenterFilterV2UseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
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
        cacheManager: FakeNotifcenterCacheManager,
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

    // -- separator -- //

    @Provides
    @NotificationScope
    fun provideNotifcenterFilterV2UseCase(
        fake: FakeNotifcenterFilterV2UseCase
    ): NotifcenterFilterV2UseCase {
        return fake
    }

    @Provides
    @NotificationScope
    fun provideFakeNotifcenterFilterV2UseCase(
        gqlUseCase: FakeGraphqlUseCase<NotifcenterFilterResponse>,
        cacheManager: NotifcenterCacheManager
    ): FakeNotifcenterFilterV2UseCase {
        return FakeNotifcenterFilterV2UseCase(
            gqlUseCase, cacheManager
        )
    }

    // -- separator -- //

    @Provides
    @NotificationScope
    fun provideGetRecommendationUseCase(
        fake: FakeGetRecommendationUseCase
    ): GetRecommendationUseCase {
        return fake
    }

    @Provides
    @NotificationScope
    fun provideFakeGetRecommendationUseCase(
        @NotificationContext
        context: Context,
        userSession: UserSessionInterface
    ): FakeGetRecommendationUseCase {
        return FakeGetRecommendationUseCase(
            context, "recom request query",
            FakeRecommendationGraphqlUseCase(), userSession
        )
    }
}