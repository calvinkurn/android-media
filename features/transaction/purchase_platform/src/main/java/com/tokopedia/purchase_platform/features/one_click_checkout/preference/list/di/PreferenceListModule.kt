package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceListUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper.PreferenceListModelMapper
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.domain.model.SetDefaultPreferenceGqlResponse
import dagger.Module
import dagger.Provides

@PreferenceListScope
@Module
class PreferenceListModule {

    @PreferenceListScope
    @Provides
    internal fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @PreferenceListScope
    @Provides
    internal fun providesSetDefaultPreferenceGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SetDefaultPreferenceGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceListScope
    @Provides
    internal fun providesGetPreferenceListUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository, preferenceListModelMapper: PreferenceListModelMapper): GetPreferenceListUseCase {
        return GetPreferenceListUseCase(context, GraphqlUseCase(graphqlRepository), preferenceListModelMapper)
    }

    @PreferenceListScope
    @Provides
    internal fun providePreferenceListAnalytics(): PreferenceListAnalytics {
        return PreferenceListAnalytics()
    }

}