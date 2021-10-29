package com.tokopedia.home_account.stub.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.WalletEligibleDataModel
import com.tokopedia.home_account.domain.usecase.GetWalletEligibleUseCase

class GetWalletEligibleUseCaseStub(
    graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetWalletEligibleUseCase(graphqlRepository, dispatcher) {

    var response = WalletEligibleDataModel()

    override suspend fun execute(params: Map<String, Any>): WalletEligibleDataModel {
        return response
    }
}