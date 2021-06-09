package com.tokopedia.loginregister.login.behaviour.di.modules.registerinitial

import android.content.res.Resources
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.external_register.ovo.data.CheckOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.CheckHasOvoAccUseCase
import com.tokopedia.loginregister.login.behaviour.data.DynamicBannerUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.GeneratePublicKeyUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.GraphqlUseCaseStub
import com.tokopedia.loginregister.login.behaviour.data.TickerInfoUseCaseStub
import com.tokopedia.loginregister.login.di.LoginScope
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialScope
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestV2
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import dagger.Module
import dagger.Provides

@Module
class MockRegisterInitialuseCaseModule {

    @Provides
    fun provideRegisterRequestGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterRequestPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideRegisterRequestV2GraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterRequestV2> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideDynamicBannerUseCase(
        stub: DynamicBannerUseCaseStub
    ): DynamicBannerUseCase = stub

    @Provides
    fun provideDynamicBannerUseCaseStub(graphqlRepository: MultiRequestGraphqlUseCase): DynamicBannerUseCaseStub {
        return DynamicBannerUseCaseStub(graphqlRepository)
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
    fun provideCheckHasOvoUseCase(graphqlRepository: GraphqlRepository): CheckHasOvoAccUseCase {
        val useCase = GraphqlUseCase<CheckOvoResponse>(graphqlRepository)
        return CheckHasOvoAccUseCase(useCase)
    }

    @RegisterInitialScope
    @Provides
    fun provideGeneratePublicUseCase(stub: GeneratePublicKeyUseCaseStub): GeneratePublicKeyUseCase = stub

    @RegisterInitialScope
    @Provides
    fun provideGeneratePublicUseCaseStub(graphqlRepository: GraphqlRepository): GeneratePublicKeyUseCaseStub {
        val useCase = GraphqlUseCaseStub<GenerateKeyPojo>(graphqlRepository)
        return GeneratePublicKeyUseCaseStub(useCase)
    }

    @Provides
    @RegisterInitialScope
    fun provideRegisterCheckUseCase(
        stub: GraphqlUseCaseStub<RegisterCheckPojo>
    ): GraphqlUseCase<RegisterCheckPojo> = stub

    @Provides
    @RegisterInitialScope
    fun provideMockRegisterCheckUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCaseStub<RegisterCheckPojo> {
        return GraphqlUseCaseStub(graphqlRepository)
    }

//    @Provides
//    fun provideGeneratePublicUseCaseStub(graphqlRepository: GraphqlRepository): GeneratePublicKeyUseCaseStub {
//        val useCase = GraphqlUseCaseStub<GenerateKeyPojo>(graphqlRepository)
//        return GeneratePublicKeyUseCaseStub(useCase)
//    }

}