package com.tokopedia.manageaddress.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
open class ManageAddressModule {

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    @ActivityScope
    open fun provideRemoteConfigAbTest(): RemoteConfig = RemoteConfigInstance.getInstance().abTestPlatform
}
