package com.tokopedia.buyerorder.unifiedhistory.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.common.BuyerProductionDispatcherProvider
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.user.session.UserSessionInterface
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
    fun provideUohGetUnifiedOrderHistoryUseCase(graphqlRepository: GraphqlRepository): GetRecommendationUseCase = GetRecommendationUseCase(graphqlRepository)

    @UohListScope
    @Provides
    fun provideGetRecommendationUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<List<RecommendationWidget>> = GraphqlUseCase(graphqlRepository)

    @UohListScope
    @Provides
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources,
                    R.raw.query_recommendation_widget)
}