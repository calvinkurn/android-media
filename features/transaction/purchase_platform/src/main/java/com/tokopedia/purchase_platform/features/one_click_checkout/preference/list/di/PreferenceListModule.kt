package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceListUseCase
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
    internal fun providesGetPreferenceListUseCase(graphqlRepository: GraphqlRepository): GetPreferenceListUseCase {
        return GetPreferenceListUseCase(GraphqlUseCase(graphqlRepository))
    }
}