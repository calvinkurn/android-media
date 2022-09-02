package com.tokopedia.user.session.datastore

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.tokopedia.encryption.security.AeadEncryptorImpl

object UserSessionDataStoreClient {
    lateinit var userSessionDataStore: UserSessionDataStore

    private const val DATA_STORE_FILE_NAME = "user_session.pb"

    @JvmStatic
    fun getInstance(context: Context): UserSessionDataStore {
        if (::userSessionDataStore.isInitialized) {
            return userSessionDataStore
        }

        val aead = AeadEncryptorImpl(context)
        val store = DataStoreFactory.create(
            UserSessionSerializer(aead),
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME) })
        userSessionDataStore = UserSessionDataStoreImpl(store)
        return userSessionDataStore
    }
}