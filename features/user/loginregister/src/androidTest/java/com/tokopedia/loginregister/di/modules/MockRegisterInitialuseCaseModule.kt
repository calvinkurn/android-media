package com.tokopedia.loginregister.di.modules

import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestV2
import com.tokopedia.loginregister.stub.FakeGraphqlRepository
import com.tokopedia.loginregister.stub.usecase.*
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
    @ActivityScope
    fun provideTickerInfoUseCase(
        stub: TickerInfoUseCaseStub
    ): TickerInfoUseCase = stub

    @ActivityScope
    @Provides
    fun provideTickerInfoUseCaseStub(resources: Resources,
                                     graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase): TickerInfoUseCaseStub {
        return TickerInfoUseCaseStub(resources, graphqlUseCase)
    }

    @ActivityScope
    @Provides
    fun provideGeneratePublicUseCase(stub: GeneratePublicKeyUseCaseStub): GeneratePublicKeyUseCase = stub

    @ActivityScope
    @Provides
    fun provideGeneratePublicUseCaseStub(graphqlRepository: GraphqlRepository): GeneratePublicKeyUseCaseStub {
        val useCase = GraphqlUseCaseStub<GenerateKeyPojo>(graphqlRepository)
        return GeneratePublicKeyUseCaseStub(useCase)
    }

    @Provides
    @ActivityScope
    fun provideRegisterCheckUseCase(
        stub: GraphqlUseCaseStub<RegisterCheckPojo>
    ): GraphqlUseCase<RegisterCheckPojo> = stub

    @Provides
    @ActivityScope
    fun provideMockRegisterCheckUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCaseStub<RegisterCheckPojo> {
        return GraphqlUseCaseStub(graphqlRepository)
    }

    @Provides
    fun provideGetProfileUseCaseStub(resources: Resources,
                                     graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase): GetProfileUseCaseStub {
        return GetProfileUseCaseStub(resources, graphqlUseCase)
    }

    @ActivityScope
    @Provides
    fun provideFakeGraphql(@ApplicationContext repository: GraphqlRepository): FakeGraphqlRepository =
        repository as FakeGraphqlRepository

}