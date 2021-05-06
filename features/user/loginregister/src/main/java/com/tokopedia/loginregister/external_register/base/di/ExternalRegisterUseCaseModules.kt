package com.tokopedia.loginregister.external_register.base.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.external_register.base.domain.usecase.ExternalRegisterUseCase
import com.tokopedia.loginregister.external_register.ovo.data.ActivateOvoResponse
import com.tokopedia.loginregister.external_register.ovo.data.CheckOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.ActivateOvoUseCase
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.CheckHasOvoAccUseCase
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import dagger.Module
import dagger.Provides

/**
 * Created by Yoris Prayogo on 19/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
class ExternalRegisterUseCaseModules {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideGraphqlUseCase(graphqlRepository: GraphqlRepository) =
            GraphqlUseCase<Any>(graphqlRepository)

    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @Provides
    fun provideCheckHasOvoUseCase(graphqlRepository: GraphqlRepository): CheckHasOvoAccUseCase {
        val useCase = GraphqlUseCase<CheckOvoResponse>(graphqlRepository)
        return CheckHasOvoAccUseCase(useCase)
    }

    @Provides
    fun provideActivateOvoUseCase(graphqlRepository: GraphqlRepository): ActivateOvoUseCase {
        val useCase = GraphqlUseCase<ActivateOvoResponse>(graphqlRepository)
        return ActivateOvoUseCase(useCase)
    }

    @Provides
    fun provideExternalRegisterUsecase(graphqlRepository: GraphqlRepository): ExternalRegisterUseCase {
        val useCase = GraphqlUseCase<RegisterPojo>(graphqlRepository)
        return ExternalRegisterUseCase(useCase)
    }
}