package com.tokopedia.shop_showcase.shop_showcase_management.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListBuyer.ShopShowcaseListBuyerResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetShopShowcaseListBuyerUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase): UseCase<ShopShowcaseListBuyerResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    companion object {
        private const val SHOP_ID = "shopId"
        private const val HIDE_NO_COUNT = "hideNoCount"
        private const val HIDE_SHOWCASE_GROUP = "hideShowcaseGroup"
        private const val IS_OWNER = "isOwner"
        private const val MUTATION = "query shopShowcasesByShopID(\$shopId: String!, \$hideNoCount: Boolean!, \$hideShowcaseGroup: Boolean!, \$isOwner: Boolean!) {\n" +
                "  shopShowcasesByShopID(shopId: \$shopId, hideNoCount: \$hideNoCount, hideShowcaseGroup: \$hideShowcaseGroup, isOwner: \$isOwner) {\n" +
                "    result {\n" +
                "      id\n" +
                "      name\n" +
                "      count\n" +
                "      type\n" +
                "      highlighted\n" +
                "      alias\n" +
                "      uri\n" +
                "      useAce\n" +
                "      badge\n" +
                "      aceDefaultSort\n" +
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"

        object BuyerQueryParam {
            const val HIDE_NO_COUNT_VALUE = true
            const val HIDE_SHOWCASE_GROUP_VALUE = false
            const val IS_OWNER_VALUE = false
        }

        object SellerQueryParam {
            const val HIDE_NO_COUNT_VALUE = false
            const val HIDE_SHOWCASE_GROUP_VALUE = true
            const val IS_OWNER_VALUE = true
        }

        fun createRequestParam(shopId: String, isOwner: Boolean): RequestParams = RequestParams.create().apply {
            putString(SHOP_ID, shopId)
            if (isOwner) {
                putBoolean(HIDE_NO_COUNT, SellerQueryParam.HIDE_NO_COUNT_VALUE)
                putBoolean(HIDE_SHOWCASE_GROUP, SellerQueryParam.HIDE_SHOWCASE_GROUP_VALUE)
                putBoolean(IS_OWNER, SellerQueryParam.IS_OWNER_VALUE)
            } else {
                putBoolean(HIDE_NO_COUNT, BuyerQueryParam.HIDE_NO_COUNT_VALUE)
                putBoolean(HIDE_SHOWCASE_GROUP, BuyerQueryParam.HIDE_SHOWCASE_GROUP_VALUE)
                putBoolean(IS_OWNER, BuyerQueryParam.IS_OWNER_VALUE)
            }
        }
    }

    override suspend fun executeOnBackground(): ShopShowcaseListBuyerResponse {
        val listShowcase = GraphqlRequest(MUTATION, ShopShowcaseListBuyerResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(listShowcase)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopShowcaseListBuyerResponse::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<ShopShowcaseListBuyerResponse>(ShopShowcaseListBuyerResponse::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }
}