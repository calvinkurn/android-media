package com.tokopedia.oneclickcheckout.preference.list.di

import android.app.Activity
import android.content.Context
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCase
import com.tokopedia.oneclickcheckout.common.domain.mapper.PreferenceListModelMapper
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.list.domain.model.SetDefaultPreferenceGqlResponse
import dagger.Module
import dagger.Provides

@PreferenceListScope
@Module
class PreferenceListModule(private val activity: Activity) {

    @PreferenceListScope
    @Provides
    internal fun provideContext(): Context = activity

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
    internal fun providesGetPreferenceListUseCase(context: Context, graphqlRepository: GraphqlRepository, preferenceListModelMapper: PreferenceListModelMapper): GetPreferenceListUseCase {
        return GetPreferenceListUseCase(context, GraphqlUseCase(graphqlRepository), preferenceListModelMapper)
    }

    @PreferenceListScope
    @Provides
    internal fun providePreferenceListAnalytics(): PreferenceListAnalytics {
        return PreferenceListAnalytics()
    }

}