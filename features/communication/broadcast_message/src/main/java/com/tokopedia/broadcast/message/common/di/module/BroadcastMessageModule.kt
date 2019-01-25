package com.tokopedia.broadcast.message.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.broadcast.message.common.di.scope.BroadcastMessageScope
import com.tokopedia.broadcast.message.common.domain.interactor.GetChatBlastSellerMetaDataUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides

@BroadcastMessageScope
@Module
class BroadcastMessageModule {

    @Provides
    fun provideGraphqlUseCase() = GraphqlUseCase()

    @BroadcastMessageScope
    @Provides
    fun provideGetChatBlastSellerMetaDataUseCase(graphqlUseCase: GraphqlUseCase,
                                                 @ApplicationContext context: Context) = GetChatBlastSellerMetaDataUseCase(graphqlUseCase, context)
}