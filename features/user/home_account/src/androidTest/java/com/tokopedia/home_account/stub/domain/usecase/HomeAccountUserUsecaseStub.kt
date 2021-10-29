package com.tokopedia.home_account.stub.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.home_account.domain.usecase.HomeAccountUserUsecase

class HomeAccountUserUsecaseStub(
    graphqlRepository: GraphqlRepository,
    rawQueries: Map<String, String>
) : HomeAccountUserUsecase(graphqlRepository, rawQueries) {

    var response = UserAccountDataModel()

    override suspend fun executeOnBackground(): UserAccountDataModel {
        return response
    }
}