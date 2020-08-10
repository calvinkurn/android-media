package com.tokopedia.buyerorder.unifiedhistory.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.common.BuyerProductionDispatcherProvider
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.*
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * Created by fwidjaja on 04/07/20.
 */

@Module
@UohListScope
class UohListModule {
    @UohListScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @UohListScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @UohListScope
    @Provides
    fun provideBuyerDispatcherProvider(): BuyerDispatcherProvider = BuyerProductionDispatcherProvider()

    @UohListScope
    @Provides
    fun provideGetUohOrderListUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<UohListOrder.Data> = GraphqlUseCase(graphqlRepository)

    @UohListScope
    @Provides
    fun provideUohFinishOrderUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<UohFinishOrder> = GraphqlUseCase(graphqlRepository)

    @UohListScope
    @Provides
    fun provideAtcMultiUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<AtcMultiData> = GraphqlUseCase(graphqlRepository)

    @UohListScope
    @Provides
    fun provideLsPrintFinishOrderUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<LsPrintData> = GraphqlUseCase(graphqlRepository)

    @UohListScope
    @Provides
    fun provideFlightResendEmailUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<FlightResendEmail> = GraphqlUseCase(graphqlRepository)

    @UohListScope
    @Provides
    fun provideTrainResendEmailUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<TrainResendEmail> = GraphqlUseCase(graphqlRepository)

    @UohListScope
    @Provides
    fun provideRechargeSetFailUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<RechargeSetFailData> = GraphqlUseCase(graphqlRepository)

    @UohListScope
    @Provides
    fun provideGetRecommendationUseCase(graphqlRepository: GraphqlRepository): GetRecommendationUseCase = GetRecommendationUseCase(graphqlRepository)

    @UohListScope
    @Provides
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    R.raw.query_recommendation_widget)
}