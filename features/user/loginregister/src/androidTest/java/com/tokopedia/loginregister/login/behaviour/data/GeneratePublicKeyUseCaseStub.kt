package com.tokopedia.loginregister.login.behaviour.data

import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import kotlinx.coroutines.delay

class GeneratePublicKeyUseCaseStub(graphqlUseCaseStub: GraphqlUseCaseStub<GenerateKeyPojo>)
    : GeneratePublicKeyUseCase(graphqlUseCaseStub) {

    var isError = false

    var delayMs = 0L

    var response = GenerateKeyPojo()
    override suspend fun executeOnBackground(): GenerateKeyPojo {
        println("Generate key stub bro $response")
        if (isError) {
            throw IllegalStateException()
        }
        if (delayMs != 0L) {
            delay(delayMs)
        }

        return response
    }
}