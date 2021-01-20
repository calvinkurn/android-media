package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.pageheader.data.model.FollowStatusResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetFollowStatusUseCase @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<FollowStatusResponse>() {

    companion object {
        private const val PARAM_SHOP_IDS = "shopId"

        @JvmStatic
        fun createParams(
                shopId: String
        ): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, shopId)
        }
    }

    private val query = """
            query followStatus(${'$'}shopId: String!) {
                followStatus(input:{shopID:${'$'}shopId}) {
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

    var params: RequestParams = RequestParams.EMPTY
    val request by lazy{
        GraphqlRequest(query, FollowStatusResponse::class.java, params.parameters)
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