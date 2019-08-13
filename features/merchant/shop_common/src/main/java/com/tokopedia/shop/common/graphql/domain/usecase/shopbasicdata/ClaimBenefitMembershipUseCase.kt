package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class ClaimBenefitMembershipUseCase(private val gqlQuery: String,
                                    private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<MembershipClaimBenefitResponse>() {

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = true

    companion object {
        private const val PARAM_QUEST_ID = "questUserId"

        @JvmStatic
        fun createRequestParams(questId: Int): RequestParams = RequestParams.create().apply {
            putInt(PARAM_QUEST_ID, questId)
        }
    }

    override suspend fun executeOnBackground(): MembershipClaimBenefitResponse {
        val gqlRequest = GraphqlRequest(gqlQuery, MembershipClaimBenefitResponse::class.java, params.parameters)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val data: MembershipClaimBenefitResponse? = gqlResponse.getSuccessData<MembershipClaimBenefitResponse>()
        val error = gqlResponse.getError(MembershipClaimBenefitResponse::class.java) ?: listOf()

        if (error.isNotEmpty()) {
            throw MessageErrorException(gqlResponse.getError(MembershipClaimBenefitResponse::class.java).first().message)
        } else if (data == null) {
            throw RuntimeException()
        }

        return data
    }
}