package com.tokopedia.user.session

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.utils.RawAccessPreference
import junit.framework.TestCase.assertEquals
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

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
    fun basicGetAndSet() {
        val context = InstrumentationRegistry.getInstrumentation().context
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
    fun whenSetEmail_theValueShouldBeEncrypted() {
        val test = "test@tokopedia.com"
        sut.email = test

        val actual = RawAccessPreference(context, LEGACY_SESSION + "_v2")
            .getRawValue("EMAIL_v2").toString()

        assertThat(actual, not(equalTo(test)))
    }
}
