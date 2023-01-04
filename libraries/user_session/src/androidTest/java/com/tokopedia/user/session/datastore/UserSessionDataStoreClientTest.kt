package com.tokopedia.user.session.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.encryption.security.AeadEncryptorImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.security.GeneralSecurityException

class UserSessionDataStoreClientTest {

    private lateinit var ctx: Context

    @Before
    fun setup() {
        ctx = ApplicationProvider.getApplicationContext()
    }

    @Test(expected = GeneralSecurityException::class)
    fun reproduce_GeneralSecurityException(): Unit = runBlocking {
        with(UserSessionDataStoreClient.getInstance(ctx)) {
            setName("Pak Toko")
        }
        AeadEncryptorImpl(ctx).delete()
        // Re create without deleting the file:
        // Simulating existing issue with GeneralSecurityException
        UserSessionDataStoreClient.reCreate(ctx, false)
        with(UserSessionDataStoreClient.getInstance(ctx)) {
            getName().first()
        }
    }

    @Test
    fun when_recreated_datastore_still_produce_expected_behaviour() = runBlocking {
        with(UserSessionDataStoreClient.getInstance(ctx)) {
            setName("Pak Toko")
        }
        AeadEncryptorImpl(ctx).delete()
        // Re create without deleting the file:
        // Simulating exisisting issue with GeneralSecurityException
        UserSessionDataStoreClient.reCreate(ctx)
        with(UserSessionDataStoreClient.getInstance(ctx)) {
            val v = "Pak Edi"
            setName(v)
            val actual = getName().first()
            assertThat(actual, `is`(v))
        }
    }
}
