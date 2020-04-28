package com.tokopedia.notifications.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase as RxUseCase
import com.tokopedia.notifications.data.DataManager
import com.tokopedia.notifications.data.model.AmplificationNotifier
import com.tokopedia.notifications.data.model.AttributionNotifier
import com.tokopedia.notifications.di.module.GraphQueryModule.Companion.AMPLIFICATION_QUERY
import com.tokopedia.notifications.di.module.GraphQueryModule.Companion.ATC_MUTATION_QUERY
import com.tokopedia.notifications.di.module.GraphQueryModule.Companion.NOTIFICATION_ATTRIBUTION
import com.tokopedia.notifications.di.scope.CMNotificationContext
import com.tokopedia.notifications.di.scope.CMNotificationScope
import com.tokopedia.notifications.domain.AmplificationUseCase
import com.tokopedia.notifications.domain.AttributionUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module class NotificationModule(val context: Context) {

    @Provides
    @CMNotificationContext
    fun provideCMNotificationContext(): Context {
        return context
    }

    @Provides
    @CMNotificationScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @CMNotificationScope
    fun provideGraphqlUseCase(
            repository: GraphqlRepository
    ): GraphqlUseCase<AttributionNotifier> {
        return GraphqlUseCase(repository)
    }

    @Provides
    @CMNotificationScope
    fun provideAttributionUseCase(
            useCase: GraphqlUseCase<AttributionNotifier>,
            @Named(NOTIFICATION_ATTRIBUTION) query: String
    ): AttributionUseCase {
        return AttributionUseCase(useCase, query)
    }

    @Provides
    @CMNotificationScope
    fun provideAtcUseCase(
            @Named(ATC_MUTATION_QUERY) query: String,
            useCase: RxUseCase,
            mapper: AddToCartDataMapper
    ): AddToCartUseCase {
        return AddToCartUseCase(query, useCase, mapper)
    }

    @Provides
    @CMNotificationScope
    fun provideAmplificationUseCase(
            useCase: GraphqlUseCase<AmplificationNotifier>,
            @Named(AMPLIFICATION_QUERY) query: String
    ): AmplificationUseCase {
        return AmplificationUseCase(useCase, query)
    }

    @Provides
    @CMNotificationScope
    fun provideDataManager(
            attributionUseCase: AttributionUseCase,
            atcProductUseCase: AddToCartUseCase,
            amplificationUseCase: AmplificationUseCase
    ): DataManager {
        return DataManager(
                attributionUseCase,
                atcProductUseCase,
                amplificationUseCase
        )
    }

}