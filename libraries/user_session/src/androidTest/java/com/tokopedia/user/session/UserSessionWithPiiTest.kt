package com.tokopedia.user.session

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.crypto.tink.shaded.protobuf.InvalidProtocolBufferException
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.encryption.security.AeadEncryptorImpl
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.utils.GoogleTinkExplorerLab
import com.tokopedia.utils.RawAccessPreference
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class UserSessionWithPiiTest {

    private lateinit var aead: AeadEncryptor
    private lateinit var userSession: UserSession
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        aead = spyk<AeadEncryptor>(AeadEncryptorImpl(context))
        userSession = UserSession(context, DataStorePreference(context), aead)
    }

    @Test
    fun test_encrypt_decrypt_pii_data() {
        val name = "Pak Edi"

        userSession.name = name
        val raw = RawAccessPreference(context, "LOGIN_SESSION_v2")
        assertThat(raw.getRawValue("FULL_NAME_v2"), instanceOf(String::class.java))
        assertThat(raw.getRawValue("FULL_NAME_v2"), `is`(not(equalTo(name))))
        verify { aead.encrypt(name, null) }

        val actual = userSession.name

        assertThat(actual, `is`(name))
    }

    @Test
    fun test_key_corrupted_still_got_backup() {
        val name = "Pak Edi"

        userSession.name = name
        verify { aead.encrypt(name, null) }

        every { aead.getAead() } throws InvalidProtocolBufferException("test")
        UserSessionMap.map.clear()

        val actual = userSession.name

        assertThat(actual, `is`(name))
    }

    @Test
    fun test_start_with_android_keystore() {
        val name = "Pak Edi"

        GoogleTinkExplorerLab.generateKey(context, name = "tkpd_master_keyset", withKeystore = true)
        GoogleTinkExplorerLab.delete(context)

        userSession.name = name
        UserSessionMap.map.clear()

        val actual = userSession.name

        assertThat(actual, `is`(name))
    }

}