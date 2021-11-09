package com.tokopedia.home_account.stub.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.CoBrandCCBalanceDataModel
import com.tokopedia.home_account.domain.usecase.GetCoBrandCCBalanceAndPointUseCase
import javax.inject.Inject

class GetCoBrandCCBalanceAndPointUseCaseStub @Inject constructor(
    graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetCoBrandCCBalanceAndPointUseCase(graphqlRepository, dispatcher) {

    var response = CoBrandCCBalanceDataModel()

    override suspend fun execute(params: Unit): CoBrandCCBalanceDataModel {
        return response
    }
}