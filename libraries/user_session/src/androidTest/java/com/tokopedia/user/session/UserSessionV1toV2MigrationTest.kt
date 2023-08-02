package com.tokopedia.user.session

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.user.session.Constants.*
import com.tokopedia.utils.MockTimber
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import timber.log.Timber

class UserSessionV1toV2MigrationTest {

    private lateinit var sut: UserSession
    private lateinit var timber: MockTimber
    private lateinit var context: Context

    @Before
    fun setup() {
        sut = UserSession(ApplicationProvider.getApplicationContext())
        UserSessionMap.map.clear()
        context = ApplicationProvider.getApplicationContext()
        timber = MockTimber()
        Timber.plant(timber)
    }

    @Test
    fun whenGettingValue_oldPreferenceShouldBeRemoved() {
        testV1PreferenceShouldBeRemoved(EMAIL, "noiz@gmail.com") {
            assertEquals(it, sut.email)
        }
    }

    @Test
    fun whenMigratingLoginMethodWithEmptyValue_oldPreferenceShouldBeRemoved() {
        testV1PreferenceShouldBeRemoved(LOGIN_METHOD, "") {
            assertEquals(it, sut.loginMethod)
        }
    }

    @Test
    fun whenMigratingLoginMethodWithEmptyValueWhileV2Exists_oldPreferenceShouldBeRemoved() {
        testV1PreferenceShouldBeRemoved(LOGIN_METHOD, "") {
            sut.loginMethod = "bar"
            UserSessionMap.map.clear()
            assertEquals("bar", sut.loginMethod)
        }
    }

    @Test
    fun whenMigratingEmailWhileV2Exists_oldPreferenceShouldBeRemoved() {
        testV1PreferenceShouldBeRemoved(EMAIL, "foo") {
            sut.email = "bar"
            UserSessionMap.map.clear()
            assertEquals("bar", sut.email)
        }
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
    @Ignore("boolean value will not be removed if value is default value, will resolve later")
    fun whenGettingIsShopOwner_oldPreferenceShouldBeRemoved() {
        val sharedPrefs = context.getSharedPreferences(LEGACY_SESSION, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(IS_SHOP_OWNER, false)
        editor.apply()

        // when
        assertEquals(false, sut.isShopOwner)
        sut.setIsShopOwner(true)
        UserSessionMap.map.clear()
        assertEquals(true, sut.isShopOwner)

        // old value should be removed by now
        val oldValue = sharedPrefs.contains(IS_SHOP_OWNER)
        assertEquals(false, oldValue)
    }

    @Test
    fun whenGettingFcmTimestamp_oldPreferenceShouldBeRemoved() {
        val sharedPrefs = context.getSharedPreferences(GCM_STORAGE, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putLong(GCM_ID_TIMESTAMP, 1000L)
        editor.apply()

        // when
        assertEquals(1000L, sut.fcmTimestamp)

        // old value should be removed by now
        val oldValue = sharedPrefs.getString(GCM_ID_TIMESTAMP, "none")
        assertEquals("none", oldValue)
    }

    @Ignore("Strange case where the value is empty. it is because of value.isEmpty() condition")
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

    /*
    * Utility method to test migration from v1 to v2
    * First, it add the key and value to the v1 shared preference
    * The migration process will happen when retrieving value from usersession through getAndTrimOldSting
    * Then, we validate that key in v1 pref should already be removed
    * */
    private fun testV1PreferenceShouldBeRemoved(KEY: String, VALUE: String, exec: (String) -> Unit) {
        val sharedPrefs = context.getSharedPreferences(LEGACY_SESSION, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString(KEY, VALUE)
        editor.apply()

        // when
        exec(VALUE)

        // old value should be removed by now
        val oldValue = sharedPrefs.getString(KEY, "none")
        assertEquals("none", oldValue)
    }

    private fun cleanUp() {
        context.getSharedPreferences(LEGACY_SESSION, Context.MODE_PRIVATE).edit().clear().apply()
        UserSessionMap.map.clear()
        timber.logs.clear()
    }
}
