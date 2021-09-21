package com.tokopedia.loginregister.login.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.loginregister.common.view.bottomsheet.SocmedBottomSheet
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Named

/**
 * @author by nisie on 10/10/18.
 */
@Module
open class LoginModule {
    @LoginScope
    @Provides
    @Named(LOGIN_CACHE)
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, LOGIN_CACHE)
    }

    @LoginScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @LoginScope
    @Provides
    fun provideSocmedBottomSheet(@ApplicationContext context: Context): SocmedBottomSheet {
        return SocmedBottomSheet(context)
    }

    companion object {
        const val LOGIN_CACHE = "LOGIN_CACHE"
    }
}