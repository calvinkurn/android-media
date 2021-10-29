package com.tokopedia.home_account.stub.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.domain.usecase.GetBalanceAndPointUseCase

class GetBalanceAndPointUseCaseStub(
    graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetBalanceAndPointUseCase(graphqlRepository, dispatcher) {

    var response = BalanceAndPointDataModel()

    override suspend fun execute(params: String): BalanceAndPointDataModel {
        return response
    }
}