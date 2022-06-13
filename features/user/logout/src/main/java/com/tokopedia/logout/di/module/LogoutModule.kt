package com.tokopedia.logout.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.encryption.security.AeadEncryptorImpl
import dagger.Module
import dagger.Provides

@Module
object LogoutModule {

    @ActivityScope
    @Provides
    fun provideAeadEncryptor(@ApplicationContext context: Context): AeadEncryptor = AeadEncryptorImpl(context)

}