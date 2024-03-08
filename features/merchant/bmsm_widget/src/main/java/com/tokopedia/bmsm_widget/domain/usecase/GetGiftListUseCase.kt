package com.tokopedia.bmsm_widget.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.bmsm_widget.domain.mapper.GwpGiftListMapper
import com.tokopedia.bmsm_widget.domain.model.GwpGiftResponse
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 06/12/23.
 */

@GqlQuery("GetGiftListGqlQuery", GQL_QUERY)
class GetGiftListUseCase @Inject constructor(
    private val mapper: GwpGiftListMapper,
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
) {

    private val classType = GwpGiftResponse::class.java

    suspend operator fun invoke(
        offerId: String, warehouseId: String
    ): List<ProductGiftUiModel> {
        val params = createParam(offerId, warehouseId)
        val gqlRequest = GraphqlRequest(GetGiftListGqlQuery(), classType, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.response(listOf(gqlRequest))
        val errors: List<GraphqlError>? = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GwpGiftResponse>(classType)
            return mapper.mapToUiModel(data)
        } else {
            throw RuntimeException(errors.firstOrNull()?.message.orEmpty())
        }
    }

    private fun createParam(offerId: String, warehouseId: String): RequestParams {
        return RequestParams.create()
    }
}

private const val GQL_QUERY = """
    
"""