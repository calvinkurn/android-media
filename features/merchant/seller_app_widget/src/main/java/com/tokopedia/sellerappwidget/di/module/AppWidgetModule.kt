package com.tokopedia.sellerappwidget.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerappwidget.coroutine.AppWidgetDispatcherProvider
import com.tokopedia.sellerappwidget.coroutine.AppWidgetDispatcherProviderImpl
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferences
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferencesImpl
import com.tokopedia.sellerappwidget.di.AppWidgetScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By @ilhamsuaib on 17/11/20
 */

@Module
class AppWidgetModule {

    @AppWidgetScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @AppWidgetScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @AppWidgetScope
    @Provides
    fun provideAppWidgetDispatcherProvider(): AppWidgetDispatcherProvider = AppWidgetDispatcherProviderImpl()

    @AppWidgetScope
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SellerAppWidgetPreferences = SellerAppWidgetPreferencesImpl(context)
}