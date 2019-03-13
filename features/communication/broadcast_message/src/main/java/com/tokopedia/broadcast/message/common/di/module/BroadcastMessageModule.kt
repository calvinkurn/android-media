package com.tokopedia.broadcast.message.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.broadcast.message.common.di.scope.BroadcastMessageScope
import com.tokopedia.broadcast.message.common.domain.interactor.GetChatBlastSellerMetaDataUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
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

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}