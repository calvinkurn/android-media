package com.tokopedia.user.session

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.user.session.datastore.DataStorePreference
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import com.tokopedia.utils.GoogleTinkExplorerLab
import com.tokopedia.utils.RawAccessPreference
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserSessionTinkRecoveryTest {

    private lateinit var context: Context
    private lateinit var storePref: DataStorePreference

    private val actual_key_set = "tkpd_master_keyset"


    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        storePref = spyk(DataStorePreference(context)) {
            every { isDataStoreEnabled() } returns true
        }
    }

    @Test
    fun test_android_keystore_to_cleartext_migration() {
        val name = "Pak Toko"

        val keystoreAead = GoogleTinkExplorerLab.generateKey(
            context,
            name = actual_key_set,
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
    fun newSolution_fixingGeneralSecurityException() = runBlocking {
        /**
         * Log in MigratedUserSession's decryptString to see the Exception
         * */
        val name = "Pak Toko"

        val keystoreAead = GoogleTinkExplorerLab.generateKey(context, actual_key_set)
        val userSession = UserSession(context, storePref, keystoreAead)

        userSession.name = name

        UserSessionMap.map.clear()
        val actual = userSession.name
        val actualDs = UserSessionDataStoreClient.getInstance(context).getName().first()

        assertThat(actual, `is`(name))
        assertThat(actualDs, `is`(name))

        GoogleTinkExplorerLab.delete(context)

        val newKeystoreAead = GoogleTinkExplorerLab.generateKey(context, actual_key_set)
        val newUserSession = UserSession(context, storePref, newKeystoreAead)
        /**
         * To fix GeneralSecurityException, aead should be refreshed and datastore must be re-created
         *   Refreshing aead is refreshed by re-creating UserSession
         *   Refreshing datastore is refreshed by using the new reCreate function
         * */
        UserSessionDataStoreClient.reCreate(context)
        val ctName = "Pak Edi"
//        userSession.name = ctName
        newUserSession.name = ctName

        UserSessionMap.map.clear()
        val ctActual = newUserSession.name
        val newName = UserSessionDataStoreClient.getInstance(context).getName().first()
        assertThat(ctActual, `is`(ctName))
        assertThat(newName, `is`(ctName))
    }

    @Test
    fun test_android_keystore_to_cleartext_migration_recovery_case() {
        val name = "Pak Toko"

        val keystoreAead = GoogleTinkExplorerLab.generateKey(
            context,
            name = actual_key_set,
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
