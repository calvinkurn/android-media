package com.tokopedia.home_account.stub.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.TokopointsBalanceDataModel
import com.tokopedia.home_account.domain.usecase.GetTokopointsBalanceAndPointUseCase
import javax.inject.Inject

class GetTokopointsBalanceAndPointUseCaseStub @Inject constructor(
    graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetTokopointsBalanceAndPointUseCase(graphqlRepository, dispatcher) {

    var response = TokopointsBalanceDataModel()

    override suspend fun execute(params: Unit): TokopointsBalanceDataModel{
        return response
    }
}