package com.tokopedia.user.session

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.crypto.tink.Aead
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.utils.GoogleTinkExplorerLab
import com.tokopedia.utils.RawAccessPreference
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class UserSessionWithPiiTest {

    private lateinit var aead: Aead
    private lateinit var userSession: UserSession
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        aead = GoogleTinkExplorerLab.generateKey(context)
        userSession = UserSession(context, DataStorePreference(context), aead)
    }

    @Test
    fun test_encrypt_decrypt_pii_data() {
        val name = "Pak Edi"

        userSession.name = name
        val raw = RawAccessPreference(context, "LOGIN_SESSION_v2")
        assertThat(raw.getRawValue("FULL_NAME_v2"), instanceOf(String::class.java))
        assertThat(raw.getRawValue("FULL_NAME_v2"), `is`(not(equalTo(name))))

        val actual = userSession.name

        assertThat(actual, `is`(name))
    }

}