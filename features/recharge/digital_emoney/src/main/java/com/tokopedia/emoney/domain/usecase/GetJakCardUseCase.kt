package com.tokopedia.emoney.domain.usecase

import com.tokopedia.emoney.domain.query.UpdateBalanceEmoneyDKIJakcard
import com.tokopedia.emoney.domain.request.JakCardRequest
import com.tokopedia.emoney.domain.response.JakCardResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetJakCardUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
): GraphqlUseCase<JakCardResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(UpdateBalanceEmoneyDKIJakcard())
        setTypeClass(JakCardResponse::class.java)
    }

    suspend fun execute(params: JakCardRequest): JakCardResponse {
        setRequestParams(params.toMapParam())
        return executeOnBackground()
    }
}
