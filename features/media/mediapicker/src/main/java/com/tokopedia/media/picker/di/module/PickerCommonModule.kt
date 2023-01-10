package com.tokopedia.media.picker.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object PickerCommonModule {

    @Provides
    @ActivityScope
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }
}
