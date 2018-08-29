package com.tokopedia.talk.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

/**
 * @author by nisie on 8/29/18.
 */
@TalkScope
@Module
class TalkModule {

    @TalkScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

}