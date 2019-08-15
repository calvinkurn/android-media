package com.tokopedia.sessioncommon.util

import android.util.Base64
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * @author by nisie on 29/05/19.
 */
@RunWith(JUnitPlatform::class)
class TokenGeneratorTest : Spek({

    given("Token Generator") {
        val tokenGenerator = TokenGenerator()
        on("test generate random char") {
            val randomChar = tokenGenerator.randomChar(4)

            it("generate 4 random char") {
                assert(randomChar.length == 4)
            }

            it("doesn't contain -") {
                assert(!randomChar.contains("-"))
            }
        }

        on("test generate grantType password") {
            val grantType = tokenGenerator.encode("password")
            System.out.println("GrantType : $grantType")

            it("encoded grantType is not null") {
                assert(!grantType.startsWith("null"))
            }

            it("when decoded, starts with password") {
                assert(Base64.decode(grantType, Base64.NO_WRAP).toString().startsWith("password"))
            }

        }

    }
})