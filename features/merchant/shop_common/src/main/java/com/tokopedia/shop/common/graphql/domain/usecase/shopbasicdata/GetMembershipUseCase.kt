package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetMembershipUseCase(private val gqlQuery: String,
                           private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<MembershipStampProgress>() {

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = true

    companion object {
        private const val PARAM_SHOP_ID = "shopId"

        @JvmStatic
        fun createRequestParams(shopId: Int): RequestParams = RequestParams.create().apply {
            putInt(PARAM_SHOP_ID, shopId)
        }
    }

    override suspend fun executeOnBackground(): MembershipStampProgress {
        val gqlRequest = GraphqlRequest(gqlQuery, MembershipStampProgress::class.java, params.parameters)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val data: MembershipStampProgress? = gqlResponse.getSuccessData<MembershipStampProgress>()
        val error = gqlResponse.getError(MembershipStampProgress::class.java) ?: listOf()

        if (error.isNotEmpty()) {
            throw MessageErrorException(gqlResponse.getError(MembershipStampProgress::class.java).first().message)
        } else if (data == null) {
            throw RuntimeException()
        }

        return data
    }


}