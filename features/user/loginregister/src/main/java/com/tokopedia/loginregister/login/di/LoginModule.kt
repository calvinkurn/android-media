package com.tokopedia.loginregister.login.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.loginregister.common.view.bottomsheet.SocmedBottomSheet
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * @author by nisie on 10/10/18.
 */
@Module
open class LoginModule {

    @LoginScope
    @Provides
    open fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @LoginScope
    @Provides
    @Named(LOGIN_CACHE)
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, LOGIN_CACHE)
    }

    @LoginScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @LoginScope
    @Provides
    @Named(NAMED_DISPATCHERS_IO)
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @LoginScope
    @Provides
    fun provideSocmedBottomSheet(@ApplicationContext context: Context): SocmedBottomSheet {
        return SocmedBottomSheet(context)
    }
    
    companion object {
        const val NAMED_DISPATCHERS_IO = "DispatcherIO"
        const val LOGIN_CACHE = "LOGIN_CACHE"
    }
}