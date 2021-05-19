package com.tokopedia.loginregister.login.behaviour.data

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sessioncommon.data.GenerateKeyPojo
import com.tokopedia.sessioncommon.data.KeyData
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase

class GeneratePublicKeyUseCaseStub(graphqlUseCase: GraphqlUseCase<GenerateKeyPojo>)
    : GeneratePublicKeyUseCase(graphqlUseCase) {

    override suspend fun executeOnBackground(): GenerateKeyPojo {
        val data = KeyData()
        return GenerateKeyPojo(data)
    }
}