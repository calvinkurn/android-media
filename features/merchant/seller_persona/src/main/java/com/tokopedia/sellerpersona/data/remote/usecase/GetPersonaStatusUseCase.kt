package com.tokopedia.sellerpersona.data.remote.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerpersona.data.remote.model.GetPersonaStatusResponse
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

@GqlQuery("GetPersonaStatusGqlQuery", GetPersonaStatusUseCase.QUERY)
class GetPersonaStatusUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetPersonaStatusResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetPersonaStatusGqlQuery())
        setTypeClass(GetPersonaStatusResponse::class.java)
    }

    companion object {
        const val QUERY = """
            query getPersonaStatus(${'$'}shopID: Int!, ${'$'}page: String!) {
              GetSellerDashboardPageLayout(shopID: ${'$'}shopID, page: ${'$'}page) {
                persona
                personaStatus
              }
            }
        """
    }
}