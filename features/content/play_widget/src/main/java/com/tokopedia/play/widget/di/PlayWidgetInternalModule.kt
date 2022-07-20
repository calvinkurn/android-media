package com.tokopedia.play.widget.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by kenny.hadisaputra on 02/06/22
 */
@Module
internal class PlayWidgetInternalModule {

    @Provides
    @PlayWidgetScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}