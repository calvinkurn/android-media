package com.tokopedia.user.session

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserSessionTest {
    @Before
    fun clearAll() {
        UserSessionMap.map.clear()
        val context = InstrumentationRegistry.getInstrumentation().context
        val sharedPrefs = context.applicationContext.getSharedPreferences(
            "LOGIN_SESSION_v2",
            Context.MODE_PRIVATE
        )
        sharedPrefs.edit().clear().commit()
        val sharedPrefs2 =
            context.applicationContext.getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE)
        sharedPrefs2.edit().clear().commit()
    }

    @Test
    fun basicGetAndSet() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val userSession = UserSession(context)
        Assert.assertEquals(userSession.email, "")
        var email = "noiz354@gmail.com"
        userSession.email = email
        Assert.assertEquals(userSession.email, email)

        // clear cache
        UserSessionMap.map.clear()

        // still return same value
        var givenEmail = userSession.email
        Assert.assertEquals(givenEmail, email)

        // changes one more time
        email = "foo@gmail.com"
        userSession.email = email
        givenEmail = userSession.email
        Assert.assertEquals(givenEmail, email)

        // clear cache
        UserSessionMap.map.clear()

        // still return same value
        givenEmail = userSession.email
        Assert.assertEquals(givenEmail, email)
    }
}
