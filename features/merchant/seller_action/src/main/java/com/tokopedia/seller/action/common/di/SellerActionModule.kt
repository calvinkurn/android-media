package com.tokopedia.seller.action.common.di

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcher
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@SellerActionScope
@Module(includes = [SellerActionRepositoryModule::class])
class SellerActionModule(private val context: Context) {

    @SellerActionScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @SellerActionScope
    @Provides
    fun provideDispatchers(): SellerActionDispatcherProvider = SellerActionDispatcher

    @SellerActionScope
    @Provides
    fun providerHandler(): Handler = Handler(Looper.getMainLooper())

    @SellerActionScope
    @Provides
    fun providerUserSession(): UserSessionInterface = UserSession(context)

}