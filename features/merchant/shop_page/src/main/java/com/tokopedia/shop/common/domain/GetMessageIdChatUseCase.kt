package com.tokopedia.shop.common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.constant.GqlQueryConstant.SHOP_REPUTATION_QUERY_STRING
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopMessageChatExist
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMessageIdChatUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<ShopMessageChatExist>() {

    private val Chat_Existing_Chat = "query getExistingChat(\$shopIds: Int!){\n" +
            "     chatExistingChat(shop_ids: \$shopIds) {\n" +
            "         messageId\n" +
            "     }\n" +
            " }"

    var params = mapOf<String, Any>()
    var isFromCacheFirst: Boolean = true
    val request by lazy{
        GraphqlRequest(Chat_Existing_Chat, ShopMessageChatExist.Response::class.java, params)
    }

    override suspend fun executeOnBackground(): ShopMessageChatExist {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())
        gqlUseCase.addRequest(request)
        val gqlResponse = gqlUseCase.executeOnBackground()

        val gqlError = gqlResponse.getError(ShopMessageChatExist.Response::class.java)
        if (gqlError?.isNotEmpty() != true){
            return gqlResponse.getData(ShopMessageChatExist.Response::class.java)
        } else {
            throw MessageErrorException(gqlError.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SHOP_IDS = "shopIds"

        @JvmStatic
        fun createParams(shopId: String): Map<String, Any> = mapOf(PARAM_SHOP_IDS to listOf(shopId.toIntOrZero()))
    }
}