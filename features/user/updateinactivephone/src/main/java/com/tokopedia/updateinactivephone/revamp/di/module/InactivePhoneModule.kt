package com.tokopedia.updateinactivephone.revamp.di.module

import android.content.Context
import com.tokopedia.updateinactivephone.revamp.di.InactivePhoneContext
import com.tokopedia.updateinactivephone.revamp.di.InactivePhoneScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class InactivePhoneModule constructor(val context: Context) {

    @InactivePhoneScope
    @InactivePhoneContext
    @Provides
    fun provideContext(): Context {
        return context
    }

    @InactivePhoneScope
    @Provides
    fun provideUsserSession(@InactivePhoneContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}