package com.tokopedia.loginregister.registerinitial.di

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.view.banner.domain.usecase.DynamicBannerUseCase
import com.tokopedia.loginregister.external_register.ovo.data.CheckOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.CheckHasOvoAccUseCase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestPojo
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterRequestV2
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import dagger.Module
import dagger.Provides

/**
 * Created by Ade Fulki on 2019-10-18.
 * ade.hadian@tokopedia.com
 */

@Module
class RegisterInitialUseCaseModule{

    @Provides
    fun provideRegisterCheckGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterCheckPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideRegisterRequestGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterRequestPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideRegisterRequestV2GraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<RegisterRequestV2> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideDynamicBannerUseCase(graphqlUseCase: MultiRequestGraphqlUseCase): DynamicBannerUseCase {
        return DynamicBannerUseCase(graphqlUseCase)
    }

    @Provides
    fun provideCheckHasOvoUseCase(graphqlRepository: GraphqlRepository): CheckHasOvoAccUseCase {
        val useCase = GraphqlUseCase<CheckOvoResponse>(graphqlRepository)
        return CheckHasOvoAccUseCase(useCase)
    }

    @Provides
    fun provideGeneratePublicKeyUseCase(graphqlRepository: GraphqlRepository): GeneratePublicKeyUseCase {
        val useCase = GraphqlUseCase<GenerateKeyPojo>(graphqlRepository)
        return GeneratePublicKeyUseCase(useCase)
    }

}