package com.tokopedia.dilayanitokopedia.home.domain.usecase

import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeAnchorTabResponse
import com.tokopedia.dilayanitokopedia.home.domain.query.GetAnchorTabQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

/**
 * using same gql as Home Icon in HomeIconRepository
 * https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2053540091/HPB+Home+-+API+GQL+GraphQL+getHomeIconV2
 * Created by irpan on 10/01/23.
 */
class GetHomeAnchorTabUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetHomeAnchorTabResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetAnchorTabQuery)
        setTypeClass(GetHomeAnchorTabResponse::class.java)
    }

    suspend fun execute(): GetHomeAnchorTabResponse.GetHomeIconV2 {
        return executeOnBackground().response
    }
}
