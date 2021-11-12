package com.tokopedia.buyerorderdetail.stub.common.graphql.coroutines.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class MultiRequestGraphqlUseCaseStub @Inject constructor(
    graphqlRepository: GraphqlRepository
) : MultiRequestGraphqlUseCase(graphqlRepository) {
    override suspend fun executeOnBackground(): GraphqlResponse {
        throw MessageErrorException()
    }
}