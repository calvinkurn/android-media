package com.tokopedia.activation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.activation.domain.mapper.GetShopFeatureMapper
import com.tokopedia.activation.domain.mapper.ShippingEditorMapper
import com.tokopedia.activation.domain.mapper.UpdateShopFeatureMapper
import com.tokopedia.activation.model.response.GetShopFeatureResponse
import com.tokopedia.activation.model.response.ShippingEditorResponse
import com.tokopedia.activation.model.response.UpdateShopFeatureResponse
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class ActivationPageModule {

    @ActivationPageScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @ActivationPageScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ActivationPageScope
    @Provides
    fun provideGetShopFeatureUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetShopFeatureResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @ActivationPageScope
    @Provides
    fun providesGetShopFeatureMapper(): GetShopFeatureMapper = GetShopFeatureMapper

    @ActivationPageScope
    @Provides
    fun provideUpdateShopFeatureUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<UpdateShopFeatureResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @ActivationPageScope
    @Provides
    fun providesUpdateShopFeatureMapper(): UpdateShopFeatureMapper = UpdateShopFeatureMapper

    @ActivationPageScope
    @Provides
    fun provideGetShippingEditorUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<ShippingEditorResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @ActivationPageScope
    @Provides
    fun providesShippingEditorMapper(): ShippingEditorMapper = ShippingEditorMapper
}