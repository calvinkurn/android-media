package com.tokopedia.privacycenter.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class PrivacyCenterModule {

    @Provides
    @ActivityScope
    fun provideHomeAccountPref(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}
