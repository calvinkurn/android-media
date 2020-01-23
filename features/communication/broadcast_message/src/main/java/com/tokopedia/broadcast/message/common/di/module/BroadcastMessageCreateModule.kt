package com.tokopedia.broadcast.message.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.broadcast.message.common.di.scope.BroadcastMessageCreateScope
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@BroadcastMessageCreateScope
@Module(includes = arrayOf(ShopCommonModule::class))
class BroadcastMessageCreateModule {
    @BroadcastMessageCreateScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}