package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.domain.model.ShopInfoLocation
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * @author by milhamj on 2020-03-13.
 */

@GqlQuery("GetShopLocationGqlQuery", GetShopLocationUseCase.QUERY)
class GetShopLocationUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
) : BaseGqlUseCase<ShippingLoc>() {

    override suspend fun executeOnBackground(): ShippingLoc {
        val gqlRequest = GraphqlRequest(
            GetShopLocationGqlQuery(), ShopInfoLocation::class.java, params.parameters
        )
        val gqlResponse: GraphqlResponse = gqlRepository.response(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(ShopInfoLocation::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<ShopInfoLocation>()
            return data.shopInfoByID.result.firstOrNull()?.shippingLoc ?: ShippingLoc()
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        const val QUERY = """
            query shopInfoByID(${'$'}shopID: Int!) {
              shopInfoByID(input: {shopIDs: [${'$'}shopID], fields: ["other-shiploc"]}) {
                result {
                  shippingLoc {
                    provinceID
                  }
                }
              }
            }
        """
        private const val SHOP_ID = "shopID"

        fun getRequestParams(shopId: String): RequestParams = RequestParams.create().apply {
            putLong(SHOP_ID, shopId.toLongOrZero())
        }
    }
}