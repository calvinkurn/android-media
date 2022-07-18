package com.tokopedia.user.session.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.crypto.tink.Aead
import com.tokopedia.utils.GoogleTinkExplorerLab
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class GoogleTinkTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun test_recover_from_android_keystore() {
        val aead = GoogleTinkExplorerLab.generateKey(context, withKeystore = true)

        encryptDecryptTest(aead)

        /*
        * When we are recovering from corrupted Keyset using Android Keystore, we have to delete the
        * key associated with Android Keystore before generating a new AEAD with the same name.
        * issue on Android Keystore instability: https://github.com/google/tink/issues/504
        * */
        GoogleTinkExplorerLab.delete(context)

        val recoverAead = GoogleTinkExplorerLab.generateKey(context, withKeystore = false)
        encryptDecryptTest(recoverAead)
    }

    @Test
    fun change_keyset_name_wont_crash_test() {
        val aead = GoogleTinkExplorerLab.generateKey(context, withKeystore = true)

        encryptDecryptTest(aead)

        val recoverAead = GoogleTinkExplorerLab.generateKey(context, "newName", withKeystore = false)
        encryptDecryptTest(recoverAead)
    }

    private fun encryptDecryptTest(aead: Aead, secret: String = "top-secret") {
        with(aead) {
            val cipher = encrypt(secret.toByteArray(), null)
            val decipher = decrypt(cipher, null)
            assertThat(secret, `is`(String(decipher)))
        }
    }
}