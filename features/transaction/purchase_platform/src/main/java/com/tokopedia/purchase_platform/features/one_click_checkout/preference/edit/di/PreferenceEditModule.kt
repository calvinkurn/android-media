package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceEditUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetShippingDurationUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.create.CreatePreferenceUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.create.model.CreatePreferenceGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.delete.DeletePreferenceUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.delete.model.DeletePreferenceGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.GetPreferenceByIdUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model.GetPreferenceByIdGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.update.UpdatePreferenceUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.update.model.UpdatePreferenceGqlResponse
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

    @PreferenceEditScope
    @Provides
    fun provideGetShippingDurationUseCase(graphqlRepository: GraphqlRepository): GetShippingDurationUseCase {
        return GetShippingDurationUseCase(GraphqlUseCase(graphqlRepository))
    }

    @PreferenceEditScope
    @Provides
    fun providesGetPreferenceByIdGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetPreferenceByIdGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

//    @PreferenceEditScope
//    @Provides
//    fun providesGetPreferenceByIdUseCase(graphqlUseCase: GraphqlUseCase<GetPreferenceByIdGqlResponse>): GetPreferenceByIdUseCase {
//        return GetPreferenceByIdUseCase(graphqlUseCase)
//    }

    @PreferenceEditScope
    @Provides
    fun providesCreatePreferenceGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<CreatePreferenceGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun providesCreatePreferenceUseCase(graphqlUseCase: GraphqlUseCase<CreatePreferenceGqlResponse>): CreatePreferenceUseCase {
        return CreatePreferenceUseCase(graphqlUseCase)
    }

    @PreferenceEditScope
    @Provides
    fun providesDeletePreferenceGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<DeletePreferenceGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun providesDeletePreferenceUseCase(graphqlUseCase: GraphqlUseCase<DeletePreferenceGqlResponse>): DeletePreferenceUseCase {
        return DeletePreferenceUseCase(graphqlUseCase)
    }

    @PreferenceEditScope
    @Provides
    fun providesUpdatePreferenceGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<UpdatePreferenceGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun providesUpdatePreferenceUseCase(graphqlUseCase: GraphqlUseCase<UpdatePreferenceGqlResponse>): UpdatePreferenceUseCase {
        return UpdatePreferenceUseCase(graphqlUseCase)
    }
}