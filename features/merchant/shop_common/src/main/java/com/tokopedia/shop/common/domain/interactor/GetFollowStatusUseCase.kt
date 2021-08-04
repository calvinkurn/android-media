package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatusResponse
import com.tokopedia.usecase.coroutines.UseCase
import java.util.HashMap
import javax.inject.Inject

class GetFollowStatusUseCase @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<FollowStatusResponse>() {

    companion object {
        const val SOURCE_NPL_TNC = "npl-tnc"
        const val SOURCE_SHOP_PAGE = "shop-page"
        const val SOURCE_PDP = "pdp"
        private const val PARAM_INPUT = "input"
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_SOURCE = "source"

        @JvmStatic
        fun createParams(shopId: String): HashMap<String, Any> {
            return hashMapOf(
                    PARAM_INPUT to hashMapOf(
                            PARAM_SHOP_ID to shopId
                    )
            )
        }

        @JvmStatic
        fun createParams(shopId: String, source: String): HashMap<String, Any> {
            return hashMapOf(
                    PARAM_INPUT to hashMapOf(
                            PARAM_SHOP_ID to shopId,
                            PARAM_SOURCE to source
                    )
            )
        }
    }

    private val query = """
            query followStatus(${'$'}input: ParamFollowStatus!) {
                followStatus(input:${'$'}input) {
                    status {
                      userIsFollowing
                      userNeverFollow
                      userFirstFollow
                    }
                    followButton {
                      buttonLabel
                      voucherIconURL
                      coachmarkText
                    }
                    error {
                      message
                    }
                }
             }
        """

    var params: HashMap<String, Any> = HashMap()
    val request by lazy{
        GraphqlRequest(query, FollowStatusResponse::class.java, params)
    }

    override suspend fun executeOnBackground(): FollowStatusResponse {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(FollowStatusResponse::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(FollowStatusResponse::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}