package com.tokopedia.user.session.datastore

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

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

        val store = DataStoreFactory.create(
            UserSessionSerializer(createAead(context)),
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME) })
        userSessionDataStore = UserSessionDataStoreImpl(context, store)
        return userSessionDataStore
    }

    private fun createAead(context: Context): Aead {
        AeadConfig.register()
        return AndroidKeysetManager.Builder()
            .withSharedPref(context.applicationContext, KEYSET_NAME, PREFERENCE_FILE)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }
}