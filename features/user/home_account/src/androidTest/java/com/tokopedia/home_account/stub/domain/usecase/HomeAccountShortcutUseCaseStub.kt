package com.tokopedia.home_account.stub.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.model.ShortcutResponse
import com.tokopedia.home_account.domain.usecase.HomeAccountShortcutUseCase
import javax.inject.Inject

class HomeAccountShortcutUseCaseStub @Inject constructor(
    graphqlRepository: GraphqlRepository
) : HomeAccountShortcutUseCase(graphqlRepository, mapOf()) {

    var response = ShortcutResponse()

    override suspend fun executeOnBackground(): ShortcutResponse {
        return response
    }
}