package com.tokopedia.fcmcommon.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.FirebaseMessagingManagerImpl

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class FcmModule(@ApplicationContext private val context: Context) {

    @Provides
    @Singleton
    internal fun provideFcmManager(sharedPreferences: SharedPreferences): FirebaseMessagingManager {
        return FirebaseMessagingManagerImpl(context, sharedPreferences)
    }

    @Provides
    @Singleton
    internal fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}
