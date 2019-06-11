package com.tokopedia.sessioncommon.domain.usecase

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.json.JSONObject
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * @author by nisie on 10/06/19.
 */
@RunWith(JUnitPlatform::class)
class LoginTokenUseCaseTest : Spek({

    given("LoginTokenUseCase") {

        on("test generate param login email") {
            private val PARAM_INPUT: String = "input"

            val PARAM_USERNAME: String = "username"
            val PARAM_PASSWORD: String = "password"
            val PARAM_ACCESS_TOKEN: String = "access_token"
            val PARAM_REFRESH_TOKEN: String = "refresh_token"
            val PARAM_GRANT_TYPE: String = "grant_type"
            val PARAM_PASSWORD_TYPE: String = "password_type"
            val PARAM_SUPPORTED: String = "supported"

            val testEmail = "email"
            val testPassword = "password"


            val params = LoginTokenUseCase.generateParamLoginEmail(testEmail,
                    testPassword)
            val input: JSONObject = JSONObject(params[PARAM_INPUT].toString())

            it("has param username as email") {
                val username: String = input[PARAM_USERNAME].toString()
                System.out.println("username : $username")
                assert(username == testEmail)
            }

            it("has grantType") {
                val grantType: String = input[PARAM_GRANT_TYPE].toString()
                assert(grantType.isNotBlank() && grantType != "null")
                System.out.println("grantType : $grantType")

            }
        }


    }
})