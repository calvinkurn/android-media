package com.tokopedia.user.session.datastore

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

object UserSessionDataStoreClient {
    lateinit var userSessionDataStore: UserSessionDataStore
    lateinit var aead: Aead

    private const val KEYSET_NAME = "tkpd_master_keyset"
    private const val MASTER_KEY_URI = "android-keystore://tkpd_master_keyset"
    private const val PREFERENCE_FILE = "tkpd_master_key_preference"

    fun createAead(context: Context): Aead {
	return if(::aead.isInitialized) {
	    aead
	} else {
	    AeadConfig.register()
	    aead =  AndroidKeysetManager.Builder()
		    .withSharedPref(context.applicationContext, KEYSET_NAME, PREFERENCE_FILE)
		    .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
		    .withMasterKeyUri(MASTER_KEY_URI)
		    .build()
		    .keysetHandle
		    .getPrimitive(Aead::class.java)
	    aead
	}
    }

    fun getInstance(context: Context): UserSessionDataStore {
        if(::userSessionDataStore.isInitialized) {
            return userSessionDataStore
	}
	userSessionDataStore = UserSessionDataStoreImpl(context)
	return userSessionDataStore
    }
}