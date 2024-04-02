package com.tokopedia.sessioncommon.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.NetworkRouter
import com.tokopedia.sessioncommon.data.fingerprintpreference.FingerprintPreference
import com.tokopedia.sessioncommon.data.fingerprintpreference.FingerprintPreferenceManager
import com.tokopedia.sessioncommon.network.AccountsBearerInterceptor
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by nisie on 10/16/18.
 */
@Module
class SessionModule {
    @SessionCommonScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Named(SESSION_MODULE)
    @SessionCommonScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @SessionCommonScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context?): NetworkRouter {
        return context as NetworkRouter
    }

    @SessionCommonScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context?,
                                   networkRouter: NetworkRouter,
                                   @Named(SESSION_MODULE) userSession: UserSessionInterface): TkpdOldAuthInterceptor {
        return TkpdOldAuthInterceptor(context, networkRouter, userSession)
    }

    @SessionCommonScope
    @Provides
    fun provideAccountsBearerInterceptor(@Named(SESSION_MODULE) userSessionInterface: UserSessionInterface?): AccountsBearerInterceptor {
        return AccountsBearerInterceptor(userSessionInterface)
    }

    @SessionCommonScope
    @Provides
    fun provideFingerprintPreferenceManager(@ApplicationContext context: Context): FingerprintPreference {
        return FingerprintPreferenceManager(context)
    }

    companion object {
        const val SESSION_MODULE = "Session"
    }
}
