package com.tokopedia.loginregister.shopcreation.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.DispatcherProvider
import com.tokopedia.loginregister.shopcreation.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.shopcreation.domain.usecase.ShopInfoUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-12-27.
 * ade.hadian@tokopedia.com
 */

@ShopCreationScope
@Module
class ShopCreationUseCaseModule {

    @ShopCreationScope
    @Provides
    fun provideRegisterCheckUseCase(
            @Named(ShopCreationQueryConstant.MUTATION_REGISTER_CHECK) query: String,
            graphqlRepository: GraphqlRepository,
            dispatcherProvider: DispatcherProvider
    ): RegisterCheckUseCase = RegisterCheckUseCase(query, graphqlRepository, dispatcherProvider)

    @ShopCreationScope
    @Provides
    fun provideShopInfoUseCase(
            @Named(ShopCreationQueryConstant.QUERY_SHOP_INFO) query: String,
            graphqlRepository: GraphqlRepository,
            dispatcherProvider: DispatcherProvider
    ): ShopInfoUseCase = ShopInfoUseCase(query, graphqlRepository, dispatcherProvider)
}