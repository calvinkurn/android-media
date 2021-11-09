package com.tokopedia.home_account.stub.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.home_account.domain.usecase.HomeAccountUserUsecase
import javax.inject.Inject

class HomeAccountUserUsecaseStub @Inject constructor(
    graphqlRepository: GraphqlRepository
) : HomeAccountUserUsecase(graphqlRepository, mapOf()) {

    var response = UserAccountDataModel()

    override suspend fun executeOnBackground(): UserAccountDataModel {
        return response
    }
}