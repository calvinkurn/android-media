package com.tokopedia.loginregister.login.behaviour.data

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.loginregister.common.domain.pojo.ActivateUserPojo
import com.tokopedia.loginregister.common.domain.usecase.ActivateUserUseCase

class ActivateUserUseCaseStub(graphqlUseCase: GraphqlUseCase<ActivateUserPojo>): ActivateUserUseCase(graphqlUseCase) {

    var response = ActivateUserPojo()

    override suspend fun executeOnBackground(): ActivateUserPojo {
        return response
    }
}