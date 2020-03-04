package com.tokopedia.loginregister.seamlesslogin.di

import dagger.Module

/**
 * Created by Ade Fulki on 2019-10-18.
 * ade.hadian@tokopedia.com
 */

@SeamlessLoginScope
@Module
class SeamlessLoginUseCaseModule{

//    @Provides
//    fun provideRegisterCheckGraphQlUseCase(graphqlRepository: GraphqlRepository)
//            : GraphqlUseCase<RegisterCheckPojo> = GraphqlUseCase(graphqlRepository)
//
//    @Provides
//    fun provideRegisterRequestGraphQlUseCase(graphqlRepository: GraphqlRepository)
//            : GraphqlUseCase<RegisterRequestPojo> = GraphqlUseCase(graphqlRepository)
//
//    @Provides
//    fun provideActivateUserGraphQlUseCase(graphqlRepository: GraphqlRepository)
//            : GraphqlUseCase<ActivateUserPojo> = GraphqlUseCase(graphqlRepository)
//
//    @Provides
//    fun provideDynamicBannerUseCase(graphqlUseCase: MultiRequestGraphqlUseCase): DynamicBannerUseCase {
//        return DynamicBannerUseCase(graphqlUseCase)
//    }
}