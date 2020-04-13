package com.tokopedia.notifications.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase as RxUseCase
import com.tokopedia.notifications.R
import com.tokopedia.notifications.data.DataManager
import com.tokopedia.notifications.data.model.AttributionNotifier
import com.tokopedia.notifications.di.scope.CMNotificationContext
import com.tokopedia.notifications.di.scope.CMNotificationScope
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
    fun provideGraphqlUseCase(): GraphqlUseCase<AttributionNotifier> {
        return GraphqlUseCase(
                GraphqlInteractor.getInstance().graphqlRepository
        )
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
    fun provideDataManager(
            attributionUseCase: AttributionUseCase,
            atcProductUseCase: AddToCartUseCase
    ): DataManager {
        return DataManager(
                attributionUseCase,
                atcProductUseCase
        )
    }

    companion object {
        const val NOTIFICATION_ATTRIBUTION = "notification_attribution"
        const val ATC_MUTATION_QUERY = "atcMutation"
    }

}