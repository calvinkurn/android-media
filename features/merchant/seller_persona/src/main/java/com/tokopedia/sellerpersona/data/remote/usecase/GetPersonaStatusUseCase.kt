package com.tokopedia.sellerpersona.data.remote.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerpersona.data.remote.model.GetPersonaStatusResponse
import com.tokopedia.sellerpersona.data.remote.model.PersonaStatusModel
import com.tokopedia.usecase.RequestParams
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

    suspend fun execute(shopId: String, page: String): PersonaStatusModel {
        setRequestParams(createParam(shopId, page).parameters)
        //return executeOnBackground().data
        return PersonaStatusModel(
            persona = "mom-and-pop",
            status = "1"
        )
    }

    private fun createParam(shopId: String, page: String): RequestParams {
        return RequestParams().apply {
            putLong(KEY_SHOP_ID, shopId.toLongOrZero())
            putString(KEY_PAGE, page)
        }
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
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_PAGE = "page"
    }
}