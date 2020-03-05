package com.tokopedia.product.manage.feature.stockreminder.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@StockReminderScope
@Module
class StockReminderModule {

    @StockReminderScope
    @Provides
    fun provideUserSerssionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @StockReminderScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

}