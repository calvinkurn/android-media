package com.tokopedia.additional_check.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.internal.TwoFactorTracker
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.encryption.security.AeadEncryptorImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main

/**
 * @author by nisie on 10/10/18.
 */
@Module
class AdditionalCheckModules {

    @ActivityScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @ActivityScope
    @Provides
    fun providePreference(@ApplicationContext context: Context): AdditionalCheckPreference = AdditionalCheckPreference(context)

    @ActivityScope
    @Provides
    fun provideTracker(): TwoFactorTracker = TwoFactorTracker()

    @ActivityScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ActivityScope
    @Provides
    fun provideEncryptor(@ApplicationContext context: Context): AeadEncryptor = AeadEncryptorImpl(context)
}