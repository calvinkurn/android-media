package com.tokopedia.user.session.datastore

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.encryption.security.AeadEncryptorImpl

object UserSessionDataStoreClient {
    lateinit var userSessionDataStore: UserSessionDataStore

    private const val KEYSET_NAME = "tkpd_master_keyset"
    private const val MASTER_KEY_URI = "android-keystore://tkpd_master_keyset"
    private const val PREFERENCE_FILE = "tkpd_master_key_preference"
    private const val DATA_STORE_FILE_NAME = "user_session.pb"

    @JvmStatic
    fun getInstance(context: Context): UserSessionDataStore {
        if (::userSessionDataStore.isInitialized) {
            return userSessionDataStore
        }

        val aead = AeadEncryptorImpl(context).getAead()
        val store = DataStoreFactory.create(
            UserSessionSerializer(aead),
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME) })
        userSessionDataStore = UserSessionDataStoreImpl(context, store)
        return userSessionDataStore
    }
}