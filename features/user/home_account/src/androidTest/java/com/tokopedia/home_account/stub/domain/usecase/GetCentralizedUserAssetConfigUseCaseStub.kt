package com.tokopedia.home_account.stub.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import com.tokopedia.home_account.domain.usecase.GetCentralizedUserAssetConfigUseCase

class GetCentralizedUserAssetConfigUseCaseStub(
    graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetCentralizedUserAssetConfigUseCase(graphqlRepository, dispatcher) {

    var response = CentralizedUserAssetDataModel()

    override suspend fun execute(params: String): CentralizedUserAssetDataModel {
        return response
    }
}