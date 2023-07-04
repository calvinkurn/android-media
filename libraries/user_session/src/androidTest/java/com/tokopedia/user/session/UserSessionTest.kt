package com.tokopedia.user.session

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.utils.RawAccessPreference
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import timber.log.Timber

const val LEGACY_SESSION = "LOGIN_SESSION"
const val V2_SESSION = LEGACY_SESSION + "_v2"

class UserSessionTest {

    lateinit var sut: UserSession
    lateinit var context: Context

    @Before
    fun clearAll() {
        UserSessionMap.map.clear()
        context = InstrumentationRegistry.getInstrumentation().context
        val sharedPrefs = context.applicationContext.getSharedPreferences(
            "LOGIN_SESSION_v2",
            Context.MODE_PRIVATE
        )
        sharedPrefs.edit().clear().commit()
        val sharedPrefs2 =
            context.applicationContext.getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE)
        sharedPrefs2.edit().clear().commit()
        sut = UserSession(context)
    }

    @Test
    fun setAndGetStressTest() {
        val start = System.currentTimeMillis()
        repeat(1_000) {
            sut.email = "foo-$it@bar.com"
            sut.name = "Foo bar $it"
            Timber.d("Getting user session, name: ${sut.name} email: ${sut.email}")
        }
        Timber.i("Took ${System.currentTimeMillis() - start} ms")
    }

    @Test
    fun basicGetAndSet_stringValue() {
        val userSession = UserSession(context)
        assertEquals(userSession.email, "")
        var email = "noiz354@gmail.com"
        userSession.email = email
        assertEquals(userSession.email, email)

        // clear cache
        UserSessionMap.map.clear()

        // still return same value
        var givenEmail = userSession.email
        assertEquals(givenEmail, email)

        // changes one more time
        email = "foo@gmail.com"
        userSession.email = email
        givenEmail = userSession.email
        assertEquals(givenEmail, email)

        // clear cache
        UserSessionMap.map.clear()

        // still return same value
        givenEmail = userSession.email
        assertEquals(givenEmail, email)
    }

    @Test
    fun basecSetAndGet_boolValue() {
        val userSession = UserSession(context)
        assertEquals(false, userSession.isShopOwner)

        userSession.setIsShopOwner(true)
        assertEquals(true, userSession.isShopOwner)
    }

    @Test
    fun basicSetAndGet_longValue() {
        val userSession = UserSession(context)
        assertEquals(0, userSession.fcmTimestamp)

        userSession.setFcmTimestamp()
        assertTrue((System.currentTimeMillis() - userSession.fcmTimestamp) < 100)
    }

    @Test
    fun whenSetEmail_theValueShouldBeEncrypted() {
        val test = "test@tokopedia.com"
        sut.email = test

        val actual = RawAccessPreference(context, V2_SESSION).getRawValue("EMAIL_v2").toString()

        assertThat(actual, not(equalTo(test)))
    }
}
