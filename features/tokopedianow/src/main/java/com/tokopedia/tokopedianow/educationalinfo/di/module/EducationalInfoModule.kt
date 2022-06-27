package com.tokopedia.tokopedianow.educationalinfo.di.module

import com.tokopedia.tokopedianow.educationalinfo.analytics.EducationalInfoAnalytics
import com.tokopedia.tokopedianow.educationalinfo.di.scope.EducationalInfoScope
import dagger.Module
import dagger.Provides

@Module
class EducationalInfoModule {

    @EducationalInfoScope
    @Provides
    fun provideEducationalInfoAnalytic(): EducationalInfoAnalytics {
        return EducationalInfoAnalytics()
    }

}