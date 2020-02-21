package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceEditUseCase
import dagger.Module
import dagger.Provides

@PreferenceEditScope
@Module
class PreferenceEditModule {

    @PreferenceEditScope
    @Provides
    internal fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @PreferenceEditScope
    @Provides
    fun providesGetPreferenceEditUseCase(graphqlRepository: GraphqlRepository): GetPreferenceEditUseCase {
        return GetPreferenceEditUseCase(GraphqlUseCase(graphqlRepository))
    }
}