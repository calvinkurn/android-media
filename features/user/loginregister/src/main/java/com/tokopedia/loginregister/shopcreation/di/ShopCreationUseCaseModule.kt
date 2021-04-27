package com.tokopedia.loginregister.shopcreation.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.shopcreation.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.shopcreation.domain.usecase.ShopInfoUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-12-27.
 * ade.hadian@tokopedia.com
 */

@Module
class ShopCreationUseCaseModule {

    @ShopCreationScope
    @Provides
    fun provideRegisterCheckUseCase(
            @Named(ShopCreationQueryConstant.MUTATION_REGISTER_CHECK) query: String,
            graphqlRepository: GraphqlRepository,
            dispatcherProvider: CoroutineDispatchers
    ): RegisterCheckUseCase = RegisterCheckUseCase(query, graphqlRepository, dispatcherProvider)

    @ShopCreationScope
    @Provides
    fun provideShopInfoUseCase(
            @Named(ShopCreationQueryConstant.QUERY_SHOP_INFO) query: String,
            graphqlRepository: GraphqlRepository,
            dispatcherProvider: CoroutineDispatchers
    ): ShopInfoUseCase = ShopInfoUseCase(query, graphqlRepository, dispatcherProvider)
}