package com.tokopedia.insurance.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class InsuranceInfoModule {

    @ActivityScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)
}
