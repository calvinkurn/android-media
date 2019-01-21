package com.tokopedia.referral.di


import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession

import dagger.Module
import dagger.Provides

/**
 * Created by ashwanityagi on 22/01/18.
 */

@Module
class ReferralModule {

    @Provides
    fun provideUserSession(@ApplicationContext context:Context) : UserSession{
        return UserSession(context)
    }

    @Provides
    fun provideRemoteConfig(@ApplicationContext context:Context) : RemoteConfig{
        return FirebaseRemoteConfigImpl(context)
    }
}
