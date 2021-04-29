package com.tokopedia.logintest.login.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logintest.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.logintest.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.logintest.login.domain.pojo.StatusPinPojo
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenV2UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-10-09.
 * ade.hadian@tokopedia.com
 */

@Module
class LoginTestAppUseCaseModule {

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
    fun provideDynamicBannerUseCase(graphqlUseCase: MultiRequestGraphqlUseCase): DynamicBannerUseCase {
        return DynamicBannerUseCase(graphqlUseCase)
    }

    @Provides
    fun provideGeneratePublicKeyUseCase(graphqlRepository: GraphqlRepository): GeneratePublicKeyUseCase {
        val useCase = GraphqlUseCase<GenerateKeyPojo>(graphqlRepository)
        return GeneratePublicKeyUseCase(useCase)
    }

    @Provides
    fun provideLoginTokenUseCaseV2(graphqlRepository: GraphqlRepository, @Named(SessionModule.SESSION_MODULE) userSessionInterface: UserSessionInterface): LoginTokenV2UseCase {
        val useCase = GraphqlUseCase<LoginTokenPojoV2>(graphqlRepository)
        return LoginTokenV2UseCase(useCase, userSessionInterface)
    }

}