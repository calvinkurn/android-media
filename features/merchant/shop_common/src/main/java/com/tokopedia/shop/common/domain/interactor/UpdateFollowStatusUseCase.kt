package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShopResponse
import com.tokopedia.usecase.coroutines.UseCase
import java.util.HashMap
import javax.inject.Inject

class UpdateFollowStatusUseCase @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<FollowShopResponse>() {

    companion object {
        const val SOURCE_NPL_TNC = "npl-tnc"
        const val ACTION_FOLLOW = "follow"
        const val ACTION_UNFOLLOW = "unfollow"
        private const val PARAM_INPUT = "input"
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_ACTION = "action"
        private const val PARAM_SOURCE = "pageSource"

        @JvmStatic
        fun createParams(shopId: String, action: String): HashMap<String, Any> {
            return hashMapOf(
                    PARAM_INPUT to hashMapOf(
                            PARAM_SHOP_ID to shopId,
                            PARAM_ACTION to action
                    )
            )
        }

        @JvmStatic
        fun createParams(shopId: String, action: String, pageSource: String): HashMap<String, Any> {
            return hashMapOf(
                    PARAM_INPUT to hashMapOf(
                            PARAM_SHOP_ID to shopId,
                            PARAM_ACTION to action,
                            PARAM_SOURCE to pageSource
                    )
            )
        }
    }

    private val query = """
            mutation followShop(${'$'}input: ParamFollowShop!) {
              followShop(input: ${'$'}input) {
                success
                message
                isFirstTime
                isFollowing
                buttonLabel
                toaster {
                  toasterText
                  buttonType
                  buttonLabel
                  url
                  appLink
                }
              }
            }
        """

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): FollowShopResponse {
        val request = GraphqlRequest(query, FollowShopResponse::class.java, params)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(FollowShopResponse::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(FollowShopResponse::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}