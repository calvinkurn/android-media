package com.tokopedia.user.session

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.utils.MockTimber
import com.tokopedia.utils.RawAccessPreference
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import timber.log.Timber

const val LEGACY_SESSION = "LOGIN_SESSION"

class UserSessionTest {

    private lateinit var sut: UserSession
    private lateinit var timber: MockTimber
    private lateinit var context: Context

    @Before
    fun setup() {
        sut = UserSession(ApplicationProvider.getApplicationContext())
        context = ApplicationProvider.getApplicationContext()
        timber = MockTimber()
        Timber.plant(timber)
    }

    @Test
    fun withMultipleDefaultValue_testMigration_shouldRemovePreviousUserSession() {
        val defaultValueTests = listOf(null, "Bearer", "", "0")
        val keyName = "REFRESH_TOKEN"
        val v = "foobar"
        // save using old key and value : for example login_session
        defaultValueTests.forEach { toTest ->
            cleanUp()
            Timber.d("Executing test for $toTest")
            val sharedPrefs = context.getSharedPreferences(LEGACY_SESSION, Context.MODE_PRIVATE)
            sharedPrefs.edit().putString(keyName, v).apply()
            val token = sut.getAndTrimOldString(LEGACY_SESSION, keyName, toTest)
            assertEquals(v, token)

            val none = "none"
            // old value should be removed by now
            val oldValue = sharedPrefs.getString(keyName, none)
            assertEquals(none, oldValue)

            UserSessionMap.map.clear()
            val secondToken = sut.getAndTrimOldString(LEGACY_SESSION, keyName, toTest)
            assertEquals(v, secondToken)
            // old value should be still removed
            val oldValue2 = sharedPrefs.getString(keyName, none)
            assertEquals(none, oldValue2)
            val cleaningLogs = timber.logs.filter { it.message.contains("cleaning") }
            assertEquals(1, cleaningLogs.size)
        }
    }

    @Test
    fun whenSetEmail_theValueShouldBeEncrypted() {
        val test = "test@tokopedia.com"
        sut.email = test

        val actual = RawAccessPreference(context, LEGACY_SESSION + "_v2")
            .getRawValue("EMAIL_v2").toString()

        assertThat(actual, not(equalTo(test)))
    }

    @Ignore("Strange case where the value is empty")
    @Test
    fun whenMigratingWithEmptyValue_shouldBeMigrated() {
        val empty = ""
        val key = "TOKEN_TYPE"
        val sharedPrefs = context.getSharedPreferences(LEGACY_SESSION, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(key, empty).apply()

        var userId = sut.tokenType
        assertThat(userId, equalTo(empty))

        UserSessionMap.map.clear()
        userId = sut.tokenType
        assertThat(userId, equalTo(empty))

    }

    private fun cleanUp() {
        context.getSharedPreferences(LEGACY_SESSION, Context.MODE_PRIVATE).edit().clear().apply()
        UserSessionMap.map.clear()
        timber.logs.clear()
    }
}
