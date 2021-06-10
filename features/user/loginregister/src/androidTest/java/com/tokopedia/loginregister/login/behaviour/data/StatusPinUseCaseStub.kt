package com.tokopedia.loginregister.login.behaviour.data

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.login.domain.StatusPinUseCase
import com.tokopedia.loginregister.login.domain.pojo.StatusPinPojo

class StatusPinUseCaseStub(rawQueries: Map<String, String>,
                           graphqlRepository: GraphqlRepository): StatusPinUseCase(rawQueries, graphqlRepository) {

    var response = StatusPinPojo()

    override suspend fun executeOnBackground(): StatusPinPojo {
        return response
    }
}