package com.tokopedia.tkpd.flashsale.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.tkpd.flashsale.di.scope.TokopediaFlashSaleScope
import com.tokopedia.tkpd.flashsale.presentation.common.constant.ValueConstant.SELLER_TOKOPEDIA_FLASH_SALE_SHARED_PREF_NAME
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class TokopediaFlashSaleModule {
    @TokopediaFlashSaleScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @TokopediaFlashSaleScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @TokopediaFlashSaleScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @TokopediaFlashSaleScope
    @Provides
    internal fun provideFlashSaleTokopediaSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SELLER_TOKOPEDIA_FLASH_SALE_SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
}
