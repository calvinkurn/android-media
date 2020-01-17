package com.tokopedia.loginregister.shopcreation.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.shopcreation.domain.usecase.RegisterCheckUseCase
import dagger.Module
import dagger.Provides
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
            graphqlRepository: GraphqlRepository
    ): RegisterCheckUseCase = RegisterCheckUseCase(query, graphqlRepository)
}