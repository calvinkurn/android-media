package com.tokopedia.loginregister.login.behaviour.data

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.login.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import kotlinx.coroutines.delay

class RegisterCheckUseCaseStub(
        graphqlRepository: GraphqlRepository
): RegisterCheckUseCase(graphqlRepository) {

    var response = RegisterCheckPojo()
    var isError = false

    var delayMs = 0L

    override suspend fun executeOnBackground(): RegisterCheckPojo {
        if (isError) {
            throw IllegalStateException()
        }
        if (delayMs != 0L) {
            delay(delayMs)
        }
        return response
//        val data = RegisterCheckData(isExist = true, userID = "123456")
//        return RegisterCheckPojo(data = data)
    }
}