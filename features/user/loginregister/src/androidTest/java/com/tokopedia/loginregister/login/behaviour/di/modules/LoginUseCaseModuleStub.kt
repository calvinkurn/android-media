package com.tokopedia.loginregister.login.behaviour.di.modules

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.discover.usecase.DiscoverUseCase
import com.tokopedia.loginregister.login.behaviour.data.DiscoverUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.RegisterCheckUseCaseStub
import com.tokopedia.loginregister.login.data.CloudDiscoverDataSource
import com.tokopedia.loginregister.login.di.LoginScope
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.login.domain.StatusFingerprintpojo
import com.tokopedia.loginregister.login.domain.pojo.StatusPinPojo
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
class LoginUseCaseModuleStub {

//    @Provides
//    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

//    @Provides
//    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

//    @Provides
//    fun provideStatusPinGraphQlUseCase(graphqlRepository: GraphqlRepository)
//            : GraphqlUseCase<StatusPinPojo> = GraphqlUseCase(graphqlRepository)

//    @Provides
//    open fun provideRegisterCheckGraphQlUseCase(graphqlRepository: GraphqlRepository)
//            : GraphqlUseCase<RegisterCheckPojo> = GraphqlUseCase(graphqlRepository)

//    @LoginScope
//    @Provides
//    fun provideRegisterCheckGraphQlUseCase(graphqlRepository: GraphqlRepository)
//            : GraphqlUseCase<RegisterCheckPojo> = GraphqlUseCaseStub(graphqlRepository)

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
    fun provideDiscoverUseCase(
            stub: DiscoverUseCaseStub
    ): DiscoverUseCase = stub

    @LoginScope
    @Provides
    fun provideDiscoverUseCasStub(cloudDiscoverDataSource: CloudDiscoverDataSource
    ): DiscoverUseCaseStub {
        return DiscoverUseCaseStub(cloudDiscoverDataSource)
    }

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @Provides
    fun provideStatusPinGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<StatusPinPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideStatusFingerprintGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<StatusFingerprintpojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideLoginTokenUseCaseV2(graphqlRepository: GraphqlRepository, @Named(SessionModule.SESSION_MODULE) userSessionInterface: UserSessionInterface): LoginTokenV2UseCase {
        val useCase = GraphqlUseCase<LoginTokenPojoV2>(graphqlRepository)
        return LoginTokenV2UseCase(useCase, userSessionInterface)
    }

    @Provides
    fun provideActivateUserUseCase(graphqlRepository: GraphqlRepository): ActivateUserUseCase {
        val useCase = GraphqlUseCase<ActivateUserPojo>(graphqlRepository)
        return ActivateUserUseCase(useCase)
    }

    @Provides
    fun provideGeneratePublicKeyUseCase(graphqlRepository: GraphqlRepository): GeneratePublicKeyUseCase {
        val useCase = GraphqlUseCase<GenerateKeyPojo>(graphqlRepository)
        return GeneratePublicKeyUseCase(useCase)
    }

    @Provides
    fun provideDynamicBannerUseCase(graphqlUseCase: MultiRequestGraphqlUseCase): DynamicBannerUseCase {
        return DynamicBannerUseCase(graphqlUseCase)
    }
}