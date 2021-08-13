package com.tokopedia.profilecompletion.addname.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by nisie on 23/04/19.
 */
@Module
class AddNameModule {

    @AddNameScope
    @Provides
    fun provideResource(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @AddNameScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
