package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.mapper.ShopInfoMapper
import com.tokopedia.sellerhome.domain.model.GetShopClosedInfoResponse
import com.tokopedia.sellerhome.domain.model.GetShopInfoResponse
import com.tokopedia.sellerhome.domain.model.ShopInfoResultResponse
import com.tokopedia.sellerhome.domain.model.TotalTokomemberResponse
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import com.tokopedia.usecase.RequestParams


@GqlQuery("GetTotalTokomemberGqlQuery", GetTotalTokoMemberUseCase.QUERY)
class GetTotalTokoMemberUseCase(
    private val gqlRepository: GraphqlRepository
) : GraphqlUseCase<TotalTokomemberResponse>() {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetTotalTokomemberGqlQuery())
        setTypeClass(TotalTokomemberResponse::class.java)
    }

    suspend fun execute(shopId: Long): TotalTokomemberResponse {
        val requestParams = RequestParams.create().apply {
            putLong(SHOP_ID, shopId)
        }
        setRequestParams(requestParams.parameters)

        val response = executeOnBackground()
        val resultStatus = response.membershipGetSumUserCardMember?.resultStatus

        return when {
            resultStatus?.code.orEmpty() == STATUS_SUCCESS -> response
            resultStatus?.code.orEmpty() != STATUS_SUCCESS && !resultStatus?.message.isNullOrEmpty() -> throw MessageErrorException(
                resultStatus?.message?.joinToString(",")
            )
            else -> throw MessageErrorException(ERROR_MESSAGE)
        }
    }

    companion object {
        const val QUERY = """
            query membershipGetSumUserCardMember(${'$'}shopID: Int!){
              membershipGetSumUserCardMember(shopID: ${'$'}shopID) {
                resultStatus {
                  code
                  message
                  reason
                }
                sumUserCardMember {
                  sumUserCardMember
                  sumUserCardMemberStr
                  isShown
                }
              }
            }
        """
        private const val SHOP_ID = "shopID"
        private const val ERROR_MESSAGE = "Failed to get total member"
        private const val STATUS_SUCCESS = "200"
    }
}
