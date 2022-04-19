package com.tokopedia.loginregister.login.behaviour.di.modules

import android.content.res.Resources
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.behaviour.data.*
import com.tokopedia.loginregister.login.di.LoginScope
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.usecase.*
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-10-09.
 * ade.hadian@tokopedia.com
 */

@Module
class LoginUseCaseModuleStub {

    @LoginScope
    @Provides
    fun provideGeneratePublicUseCase(stub: GeneratePublicKeyUseCaseStub): GeneratePublicKeyUseCase = stub

    @LoginScope
    @Provides
    fun provideGeneratePublicUseCaseStub(graphqlRepository: GraphqlRepository): GeneratePublicKeyUseCaseStub {
        val useCase = GraphqlUseCaseStub<GenerateKeyPojo>(graphqlRepository)
        return GeneratePublicKeyUseCaseStub(useCase)
    }

    @LoginScope
    @Provides
    fun provideLoginTokenUseCaseV2(stub: LoginTokenV2UseCaseStub): LoginTokenV2UseCase = stub

    @LoginScope
    @Provides
    fun provideLoginTokenUseCaseV2Stub(graphqlRepository: GraphqlRepository, @Named(SessionModule.SESSION_MODULE) userSessionInterface: UserSessionInterface): LoginTokenV2UseCaseStub {
        val useCase = GraphqlUseCaseStub<LoginTokenPojoV2>(graphqlRepository)
        return LoginTokenV2UseCaseStub(useCase, userSessionInterface)
    }

    @Provides
    @LoginScope
    fun provideRegisterCheckUseCase(
            stub: RegisterCheckUseCaseStub
    ): RegisterCheckUseCase = stub

    @LoginScope
    @Provides
    fun provideRegisterCheckGraphQlUseCase(graphqlRepository: GraphqlRepository): RegisterCheckUseCaseStub {
        return RegisterCheckUseCaseStub(graphqlRepository)
    }

    @Provides
    @LoginScope
    fun provideUserSessionInterface(): UserSessionInterface = UserSessionStub()

    @Provides
    @LoginScope
    fun provideGetAdminTypeUseCase(
            stub: GetAdminTypeUseCaseStub
    ): GetAdminTypeUseCase = stub

    @LoginScope
    @Provides
    fun provideGetAdminTypeUseCaseStub(graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase): GetAdminTypeUseCaseStub {
        return GetAdminTypeUseCaseStub(graphqlUseCase)
    }

    @Provides
    @LoginScope
    fun provideLoginTokenUseCase(
            stub: LoginTokenUseCaseStub
    ): LoginTokenUseCase = stub

    @LoginScope
    @Provides
    fun provideLoginTokenUseCaseStub(resources: Resources,
                                     graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase,
                                     userSessionInterface: UserSessionInterface): LoginTokenUseCaseStub {
        return LoginTokenUseCaseStub(resources, graphqlUseCase, userSessionInterface)
    }

    @Provides
    @LoginScope
    fun provideTickerInfoUseCase(
            stub: TickerInfoUseCaseStub
    ): TickerInfoUseCase = stub

    @LoginScope
    @Provides
    fun provideTickerInfoUseCaseStub(resources: Resources,
                                     graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase): TickerInfoUseCaseStub {
        return TickerInfoUseCaseStub(resources, graphqlUseCase)
    }

    @Provides
    @LoginScope
    fun provideDynamicBannerUseCase(
            stub: DynamicBannerUseCaseStub
    ): DynamicBannerUseCase = stub

    @LoginScope
    @Provides
    fun provideDynamicBannerUseCaseStub(graphqlRepository: MultiRequestGraphqlUseCase): DynamicBannerUseCaseStub {
        return DynamicBannerUseCaseStub(graphqlRepository)
    }

    @Provides
    @LoginScope
    fun provideActivateUserUseCase(
            stub: ActivateUserUseCaseStub
    ): ActivateUserUseCase = stub

    @LoginScope
    @Provides
    fun provideActivateUserUseCaseStub(graphqlRepository: GraphqlRepository): ActivateUserUseCaseStub {
        val useCase = GraphqlUseCaseStub<ActivateUserPojo>(graphqlRepository)
        return ActivateUserUseCaseStub(useCase)
    }

    @Provides
    @LoginScope
    fun provideGetProfileUseCase(
            stub: GetProfileUseCaseStub
    ): GetProfileUseCase = stub

    @LoginScope
    @Provides
    fun provideGetProfileUseCaseStub(resources: Resources,
                                     graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase): GetProfileUseCaseStub {
        return GetProfileUseCaseStub(resources, graphqlUseCase)
    }

    @Provides
    @LoginScope
    fun provideDiscoverUseCase(
            stub: DiscoverUseCaseStub
    ): DiscoverUseCase = stub

    @LoginScope
    @Provides
    fun provideDiscoverUseCasStub(
        graphqlRepository: GraphqlRepository,
        coroutineDispatcher: CoroutineDispatcher
    ): DiscoverUseCaseStub {
        return DiscoverUseCaseStub(graphqlRepository, coroutineDispatcher)
    }

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
}