package com.tokopedia.shop.info.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.shop.info.di.scope.ShopInfoScope
import com.tokopedia.shop.info.domain.usecase.GetShopStatisticUseCase
import com.tokopedia.shop.note.view.model.ShopNoteUiModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopInfoViewModelModule::class])
class ShopInfoModule {
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context?): NetworkRouter? {
        return context as NetworkRouter?
    }

    @ShopInfoScope
    @Provides
    fun provideShopNoteViewModel(): ShopNoteUiModel {
        return ShopNoteUiModel()
    }

    @ShopInfoScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopInfoScope
    @Provides
    fun getCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }
}