package com.tokopedia.oneclickcheckout.preference.list.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCase
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCaseImpl
import com.tokopedia.oneclickcheckout.common.domain.mapper.PreferenceModelMapper
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.list.domain.SetDefaultPreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.list.domain.SetDefaultPreferenceUseCaseImpl
import com.tokopedia.oneclickcheckout.preference.list.domain.model.SetDefaultPreferenceGqlResponse
import dagger.Module
import dagger.Provides

@Module
class PreferenceListModule {

    @PreferenceListScope
    @Provides
    internal fun providesGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @PreferenceListScope
    @Provides
    internal fun providesSetDefaultPreferenceGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SetDefaultPreferenceGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceListScope
    @Provides
    internal fun providesSetDefaultPreferenceUseCase(graphqlUseCase: GraphqlUseCase<SetDefaultPreferenceGqlResponse>): SetDefaultPreferenceUseCase {
        return SetDefaultPreferenceUseCaseImpl(graphqlUseCase)
    }

    @PreferenceListScope
    @Provides
    internal fun providesGetPreferenceListUseCase(graphqlRepository: GraphqlRepository): GetPreferenceListUseCase {
        return GetPreferenceListUseCaseImpl(GraphqlUseCase(graphqlRepository), PreferenceModelMapper)
    }

    @PreferenceListScope
    @Provides
    internal fun providePreferenceListAnalytics(): PreferenceListAnalytics {
        return PreferenceListAnalytics()
    }

}