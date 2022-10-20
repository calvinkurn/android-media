package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.domain.entity.ShopTotalFollowers
import com.tokopedia.seller.menu.common.domain.usecase.GetShopTotalFollowersUseCase
import com.tokopedia.sellerhome.domain.mapper.ShopInfoMapper
import com.tokopedia.sellerhome.domain.model.GetShopClosedInfoResponse
import com.tokopedia.sellerhome.domain.model.GetShopInfoResponse
import com.tokopedia.sellerhome.domain.model.ShopInfoResultResponse
import com.tokopedia.sellerhome.domain.model.TotalTokomemberResponse
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase


@GqlQuery("GetTotalTokomemberGqlQuery", GetTotalTokoMemberUseCase.QUERY)
class GetTotalTokoMemberUseCase(private val gqlRepository: GraphqlRepository) :
    UseCase<Long>() {

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): Long {
        val gqlRequest = GraphqlRequest(
            GetTotalTokomemberGqlQuery(),
            TotalTokomemberResponse::class.java,
            params
        )
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
            .build()
        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val data: TotalTokomemberResponse = gqlResponse.getData(TotalTokomemberResponse::class.java)
        val resultStatus = data.membershipGetSumUserCardMember?.resultStatus

        return when {
            data.membershipGetSumUserCardMember?.sumUserCardMember != null -> data.membershipGetSumUserCardMember
                .sumUserCardMember.sumUserCardMember.orZero()
            !resultStatus?.message.isNullOrEmpty() -> throw MessageErrorException(
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
        fun createRequestParams(shopId: Long) = HashMap<String, Any>().apply {
            put(SHOP_ID, shopId)
        }
    }
}
