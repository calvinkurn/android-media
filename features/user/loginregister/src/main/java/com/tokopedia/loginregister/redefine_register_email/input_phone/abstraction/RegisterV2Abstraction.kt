package com.tokopedia.loginregister.redefine_register_email.input_phone.abstraction

import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.RegisterV2UseCase
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data.Register
import com.tokopedia.loginregister.redefine_register_email.input_phone.domain.data.RegisterV2Param
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.util.EncoderDecoder

abstract class RegisterV2Abstraction(
    private val email: String = "",
    private val phone: String = "",
    private val fullName: String,
    private val encryptedPassword: String,
    private val validateToken: String,
    private val hash: String,
    private val registerV2UseCase: RegisterV2UseCase,
    private val userSession: UserSessionInterface
) {
    private suspend fun result(): Result<Register> {

        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")

        val registerV2Param = RegisterV2Param(
            regType = REGISTRATION_TYPE,
            osType = OS_TYPE,
            fullName = fullName,
            email = email,
            phone = phone,
            password = encryptedPassword,
            validateToken = validateToken,
            h = hash
        )

        val responseRegisterV2 = registerV2UseCase(registerV2Param)

        val result = onSuccessRegisterV2Request(responseRegisterV2.register)

        when (result) {
            is Success -> {
                loadUserInfo()
            }
            is Fail -> {
                userSession.clearToken()
            }
        }

        return result
    }

    private fun onSuccessRegisterV2Request(result: Register): Result<Register> {
        userSession.clearToken()
        return when {
            result.accessToken.isNotEmpty() && result.refreshToken.isNotEmpty() && result.tokenType.isNotEmpty() -> {
                setSession(result)
                Success(result)
            }
            result.errors.isNotEmpty() && result.errors[0].message.isNotEmpty() -> {
                Fail(MessageErrorException(result.errors[0].message))
            }
            else -> {
                Fail(RuntimeException())
            }
        }
    }

    private fun setSession(result: Register) {
        userSession.setToken(
            result.accessToken,
            result.tokenType,
            EncoderDecoder.Encrypt(result.refreshToken, userSession.refreshTokenIV)
        )
    }

    protected abstract suspend fun loadUserInfo()

    suspend fun data(): Result<Register> = result()

    companion object {
        private const val REGISTRATION_TYPE = "email"
        private const val OS_TYPE = "1"
    }
}