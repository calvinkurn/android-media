package com.tokopedia.user.session

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.utils.GoogleTinkExplorerLab
import com.tokopedia.utils.RawAccessPreference
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserSessionTinkRecoveryTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }


    @Test
    fun test_android_keystore_to_cleartext_migration() {
        val name = "Pak Toko"

        val keystoreAead = GoogleTinkExplorerLab.generateKey(
            context,
            name = "tkpd_master_keyset",
            withKeystore = true
        )
        val userSession = UserSession(context, DataStorePreference(context), keystoreAead)

        userSession.name = name

        UserSessionMap.map.clear()
        val actual = userSession.name

        assertThat(actual, `is`(name))


        GoogleTinkExplorerLab.delete(context)
        val ctUserSession = UserSession(context)

        val ctName = "Pak Edi"
        ctUserSession.name = ctName

        val ctActual = ctUserSession.name
        assertThat(ctActual, `is`(ctName))
    }

    @Test
    fun test_android_keystore_to_cleartext_migration_buggy_current_condition() {
        val name = "Pak Toko"

        val keystoreAead = GoogleTinkExplorerLab.generateKey(
            context,
            name = "tkpd_master_keyset",
            withKeystore = true
        )
        val userSession = UserSession(context, DataStorePreference(context), keystoreAead)

        userSession.name = name

        UserSessionMap.map.clear()
        val actual = userSession.name

        assertThat(actual, `is`(name))


        /**
         * Because of default behaviour of UserSession will generate Aead without AndroidKeystore,
         * after this execution, any string access should produce [InvalidProtocolBufferException]
         * */
        val ctUserSession = UserSession(context)

        /**
         * But because the exception was handled, it returns the expected value from the backup.
         * After 3.178, we always set a backup value whenever PII data is set
         * */
        UserSessionMap.map.clear()
        val backup = RawAccessPreference(context, "LOGIN_SESSION_v2")
            .getRawValue("FULL_NAME_v2_PII_BACKUP") as String
        assertThat(backup, `is`(notNullValue()))
        val ctActual = ctUserSession.name
        assertThat(ctActual, `is`(name))

        val encryptionErrorStatus = RawAccessPreference(context, "ENCRYPTION_STATE_PREF")
            .getRawValue("KEY_ENCRYPTION_ERROR") as Boolean
        assertTrue(encryptionErrorStatus)

        /**
         * In this scenario, the name will never empty, whereas, Google Tink recovers only if the name is empty.
         * Hence, this case won't recover and always gets [InvalidProtocolBufferException].
         * Who: Every user who already gets AndroidKeystore prior to 3.178,
         *      then having backup name after 3.178 without recovery first
         *
         * This condition has been fixed at version 3.184
         * */
    }

    @Test
    fun test_android_keystore_to_cleartext_migration_recovery_case() {
        val name = "Pak Toko"

        val keystoreAead = GoogleTinkExplorerLab.generateKey(
            context,
            name = "tkpd_master_keyset",
            withKeystore = true
        )
        val userSession = UserSession(context, DataStorePreference(context), keystoreAead)

        userSession.name = name

        UserSessionMap.map.clear()
        val actual = userSession.name

        assertThat(actual, `is`(name))


        /**
         * Because of default behaviour of UserSession will generate Aead without AndroidKeystore,
         * after this execution, any string access should produce [InvalidProtocolBufferException]
         * */
        val ctUserSession = UserSession(context)

        /**
         * In this case, we try to simulate a user when he/she has never had a backup before
         * */
        UserSessionMap.map.clear()
        RawAccessPreference(context, "LOGIN_SESSION_v2").clearPiiBackup("FULL_NAME_v2")
        val ctActual = ctUserSession.name
        assertThat(ctActual, `is`(""))

        val encryptionErrorStatus = RawAccessPreference(context, "ENCRYPTION_STATE_PREF")
            .getRawValue("KEY_ENCRYPTION_ERROR") as Boolean
        assertTrue(encryptionErrorStatus)

        /**
         * In this scenario, the name will be empty string, So our recovery will proceed as it is
         * Who: Every user who already gets AndroidKeystore prior to 3.178,
         *      then without setting any backup, immediately recovers
         * */
    }
}