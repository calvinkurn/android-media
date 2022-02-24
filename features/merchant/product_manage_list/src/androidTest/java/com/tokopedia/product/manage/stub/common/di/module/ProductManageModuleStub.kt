package com.tokopedia.product.manage.stub.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.product.manage.common.di.ProductManageScope
import com.tokopedia.product.manage.common.session.ProductManageSession
import com.tokopedia.product.manage.stub.common.UserSessionStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class ProductManageModuleStub {

    @ProductManageScope
    @Provides
    fun provideMultiRequestGraphqlUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @ProductManageScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ProductManageScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @ProductManageScope
    @Provides
    fun provideProductManageSession(@ApplicationContext context: Context): ProductManageSession {
        return ProductManageSession(context)
    }

}