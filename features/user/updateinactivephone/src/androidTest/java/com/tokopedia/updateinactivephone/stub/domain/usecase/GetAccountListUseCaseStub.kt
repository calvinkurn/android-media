package com.tokopedia.updateinactivephone.stub.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListUseCase

class GetAccountListUseCaseStub constructor(
    repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetAccountListUseCase(repository, dispatcher) {

    var response = AccountListDataModel()

    override suspend fun execute(params: Map<String, Any>): AccountListDataModel {
        return response
    }
}