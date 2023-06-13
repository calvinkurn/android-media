package com.tokopedia.user.session.di

import android.content.Context
import com.tokopedia.encryption.security.AeadEncryptorImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import dagger.Module
import dagger.Provides

@Module
open class SessionModule {

    @Provides
    open fun provideAbPlatform(context: Context): DataStorePreference {
        return DataStorePreference(context)
    }

    @Provides
    fun provideUserSession(context: Context, dataStorePreference: DataStorePreference): UserSessionInterface {
        return UserSession(context, dataStorePreference, AeadEncryptorImpl(context).getAead())
    }

    @Provides
    fun provideDataStore(context: Context): UserSessionDataStore {
        return UserSessionDataStoreClient.getInstance(context)
    }

}