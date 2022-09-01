package com.tokopedia.tokopedianow.educationalinfo.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.tokopedianow.educationalinfo.analytics.EducationalInfoAnalytics
import com.tokopedia.tokopedianow.educationalinfo.di.scope.EducationalInfoScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class EducationalInfoModule {

    @EducationalInfoScope
    @Provides
    fun provideEducationalInfoAnalytic(userSession: UserSessionInterface): EducationalInfoAnalytics {
        return EducationalInfoAnalytics(userSession)
    }

    @EducationalInfoScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}