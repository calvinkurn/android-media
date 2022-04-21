package com.tokopedia.encryption.security

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

class AeadEncryptorImpl(val context: Context): AeadEncryptor {

    private val KEYSET_NAME = "tkpd_master_keyset"
    private val MASTER_KEY_URI = "android-keystore://tkpd_master_keyset"
    private val PREFERENCE_FILE = "tkpd_master_key_preference"

    private lateinit var aeadInstance: Aead

    init {
	AeadConfig.register()
    }

    override fun getAead(): Aead {
	return if (this::aeadInstance.isInitialized) aeadInstance else initAead()
    }

    override fun initAead(): Aead {
	aeadInstance = AndroidKeysetManager.Builder()
	    .withSharedPref(context.applicationContext, KEYSET_NAME, PREFERENCE_FILE)
	    .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
	    .withMasterKeyUri(MASTER_KEY_URI)
	    .build()
	    .keysetHandle
	    .getPrimitive(Aead::class.java)
	return aeadInstance
    }

    override fun encrypt(message: ByteArray, associatedData: ByteArray?): String {
	return Base64.encodeToString(getAead()?.encrypt(message, associatedData), Base64.DEFAULT)
    }

    override fun decrypt(base64EncryptedString: String, associatedData: ByteArray?): String {
	return Base64.encodeToString(getAead()?.decrypt(Base64.decode(base64EncryptedString, Base64.DEFAULT), associatedData), Base64.DEFAULT)
    }
}