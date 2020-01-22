package com.tokopedia.broadcast.message.common.di.module

import com.tokopedia.broadcast.message.common.di.scope.BroadcastMessageCreateScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.shop.common.di.ShopCommonModule
import dagger.Module
import dagger.Provides

@BroadcastMessageCreateScope
@Module(includes = arrayOf(ShopCommonModule::class))
class BroadcastMessageCreateModule {
    @BroadcastMessageCreateScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase =
            GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
}