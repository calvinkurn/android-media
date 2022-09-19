package com.tokopedia.sessioncommon.domain.commonaction

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.register.Register
import com.tokopedia.sessioncommon.data.register.RegisterV2Model
import com.tokopedia.sessioncommon.data.register.RegisterV2Param
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.util.EncoderDecoder

abstract class RegisterV2SaveSession(
    private val registerV2Model: RegisterV2Model,
    private val userSession: UserSessionInterface
) {

    private fun result(): Result<Register> {

        val result = onSuccessRegisterV2Request(registerV2Model.register)

        when (result) {
            is Fail -> {
                userSession.clearToken()
            }
            else -> {}
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

    fun data(): Result<Register> = result()

    companion object {
        private const val REGISTRATION_TYPE = "email"
        private const val OS_TYPE = "1"
    }

}