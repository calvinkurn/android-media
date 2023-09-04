package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.detail.domain.mapper.CategoryRecommendationMapper
import com.tokopedia.product.addedit.detail.domain.model.GetCategoryRecommendationResponse
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetCategoryRecommendationUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val mapper: CategoryRecommendationMapper
) : UseCase<List<ListItemUnify>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<ListItemUnify> {
        val gqlRequest = GraphqlRequest(QUERY, GetCategoryRecommendationResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.response(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetCategoryRecommendationResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetCategoryRecommendationResponse>(GetCategoryRecommendationResponse::class.java)
            val categoryRecommendationData = data.categoryRecommendationDataModel
            return mapper.mapRemoteModelToUiModel(categoryRecommendationData)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {

        private const val QUERY = "query GetJarvisRecommendation(\$productName: String) {\n" +
                "  getJarvisRecommendation(product_name: \$productName) {\n" +
                "    categories {\n" +
                "      id\n" +
                "      name\n" +
                "      confidence_score\n" +
                "      precision\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val PARAM_PRODUCT_NAME = "productName"

        @JvmOverloads
        fun createRequestParams(productName: String = ""):
                RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_PRODUCT_NAME, productName)
            return requestParams
        }

    }
}
