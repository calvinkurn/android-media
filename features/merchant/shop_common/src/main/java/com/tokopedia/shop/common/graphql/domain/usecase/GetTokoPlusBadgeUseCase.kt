package com.tokopedia.shop.common.graphql.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.common.data.source.cloud.model.TokoPlusBadgeResponse
import com.tokopedia.shop.common.graphql.domain.mapper.TokoPlusBadgeMapper
import com.tokopedia.shop.common.view.model.TokoPlusBadgeUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 01/07/22.
 */

@GqlQuery("GetTokoPlusBadgeGqlQuery", GetTokoPlusBadgeUseCase.QUERY)
class GetTokoPlusBadgeUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val mapper: TokoPlusBadgeMapper
) : GraphqlUseCase<TokoPlusBadgeResponse>(graphqlRepository) {

    suspend fun execute(shopId: String): TokoPlusBadgeUiModel {
        val parameters = createParam(shopId).parameters
        setRequestParams(parameters)
        setGraphqlQuery(GetTokoPlusBadgeGqlQuery())
        setTypeClass(TokoPlusBadgeResponse::class.java)

        val data = executeOnBackground()
        return mapper.mapToUiModel(data)
    }

    private fun createParam(shopId: String): RequestParams {
        return RequestParams.create().apply {
            putLong(PARAM_SHOP_ID, shopId.toLongOrZero())
        }
    }

    companion object {
        internal const val QUERY = """
            query restrictValidateRestriction(${'$'}shopId: Int!) {
              restrictValidateRestriction(input: {source: "seller_dashboard", dataRequest: [], version: 2, metaRequest: [{restriction_name: "seller_bebas_ongkir_shop", dataRequest: [{shop: {shopID: ${'$'}shopId}, user: {ignoreUserValidation: true}}]}, {restriction_name: "bebas_ongkir_shop_plus_only", dataRequest: [{shop: {shopID: ${'$'}shopId}, user: {ignoreUserValidation: true}}]}]}) {
                metaResponse {
                  restrictionName
                  dataResponse {
                    status
                    metadata {
                      bebasOngkirMetadata {
                        badgeURL
                      }
                    }
                  }
                }
              }
            }
        """

        private const val PARAM_SHOP_ID = "shopId"
    }
}