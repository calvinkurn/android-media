package com.tokopedia.home_account.stub.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.domain.usecase.GetCoBrandCCBalanceAndPointUseCase

class GetCoBrandCCBalanceAndPointUseCaseStub(
    graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetCoBrandCCBalanceAndPointUseCase(graphqlRepository, dispatcher) {

    var response = BalanceAndPointDataModel()

    override suspend fun execute(params: Unit): BalanceAndPointDataModel {
        return response
    }
}