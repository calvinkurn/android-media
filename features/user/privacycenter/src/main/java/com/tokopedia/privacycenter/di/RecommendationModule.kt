package com.tokopedia.privacycenter.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides

@Module
object RecommendationModule {

    @Provides
    @ActivityScope
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @ActivityScope
    fun providePermissionChecker(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }
}
