package com.tokopedia.loginregister.login.dagger

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.login.di.LoginScope
import com.tokopedia.loginregister.login.domain.StatusFingerprintpojo
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.login.domain.pojo.StatusPinPojo
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock

/**
 * Created by Yoris Prayogo on 09/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@LoginScope
@Module
class MockLoginUseCaseModule {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @Provides
    fun provideStatusPinGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<StatusPinPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideRegisterCheckGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterCheckPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideStatusFingerprintGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<StatusFingerprintpojo> = GraphqlUseCase(graphqlRepository)


    @Provides
    fun provideDynamicBannerUseCase(graphqlUseCase: MultiRequestGraphqlUseCase): DynamicBannerUseCase {
        return DynamicBannerUseCase(graphqlUseCase)
    }
}