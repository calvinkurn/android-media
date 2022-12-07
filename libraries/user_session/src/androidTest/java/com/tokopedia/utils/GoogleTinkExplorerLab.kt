package com.tokopedia.utils

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import java.security.KeyStore

object GoogleTinkExplorerLab {

    const val PREFERENCE_FILE = "tkpd_master_key_preference"
    private const val DEFAULT_NAME = "default_key"
    private const val MASTER_KEY_URI = "android-keystore://test_master_keyset"

    init {
        AeadConfig.register()
    }

    fun generateKey(context: Context, name: String = DEFAULT_NAME, withKeystore: Boolean = false): Aead {
        return AndroidKeysetManager.Builder()
            .withSharedPref(context, name, PREFERENCE_FILE)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .let {
                if (withKeystore) it.withMasterKeyUri(MASTER_KEY_URI)
                else it
            }
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }

    fun encrypt(aead: Aead, message: String, associatedData: ByteArray?): String {
        val messageBytes = message.toByteArray(Charsets.UTF_8)
        return Base64.encodeToString(
            aead.encrypt(messageBytes, associatedData),
            Base64.DEFAULT
        )
    }

    fun decrypt(aead: Aead, base64EncryptedString: String, associatedData: ByteArray?): String {
        val messageToDecrypt = Base64.decode(base64EncryptedString, Base64.DEFAULT)
        return String(aead.decrypt(messageToDecrypt, associatedData), Charsets.UTF_8)
    }

    fun delete(context: Context) {
        val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        keyStore.deleteEntry(MASTER_KEY_URI)

        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).edit().clear().apply()
    }


}
