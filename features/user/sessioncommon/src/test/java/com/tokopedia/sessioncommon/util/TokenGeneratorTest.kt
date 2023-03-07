package com.tokopedia.sessioncommon.util

import android.util.Base64
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Ignore
import org.junit.Test

class TokenGeneratorTest  {

    @Test
    fun `test generate random token`() {
        val tokenGenerator = TokenGenerator()

        val actual = tokenGenerator.randomChar(4)

        assertThat(actual.length, equalTo(4))
        assertThat(actual, not(containsString("-")))
    }

    @Test
    @Ignore("this test should be in androidTest, it is always fail because Base64 is android class")
    fun `test generate grantType password`() {
        val tokenGenerator = TokenGenerator()

        val actual = tokenGenerator.encode("password")
        println("GrantType: $actual")

        assertThat(Base64.decode(actual, Base64.NO_WRAP).toString(), startsWith("password"))
        assertThat(actual, not(startsWith("null")))
    }

}
