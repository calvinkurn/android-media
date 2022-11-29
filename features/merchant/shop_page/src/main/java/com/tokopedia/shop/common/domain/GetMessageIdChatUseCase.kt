package com.tokopedia.shop.common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopinfo.ChatExistingChat
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetMessageIdChatUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ChatExistingChat>() {

    private val _chatExistingChat = "query getExistingChat(\$shopIds: Int!){\n" +
        "     chatExistingChat(toShopId: \$shopIds) {\n" +
        "         messageId\n" +
        "     }\n" +
        " }"

    var params = mapOf<String, Any>()
    var isFromCacheFirst: Boolean = true
    val request by lazy {
        GraphqlRequest(_chatExistingChat, ChatExistingChat::class.java, params)
    }

    override suspend fun executeOnBackground(): ChatExistingChat {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build()
        )
        gqlUseCase.addRequest(request)
        val gqlResponse = gqlUseCase.executeOnBackground()

        val gqlError = gqlResponse.getError(ChatExistingChat::class.java)
        if (gqlError?.isNotEmpty() != true) {
            return gqlResponse.getData(ChatExistingChat::class.java)
        } else {
            throw MessageErrorException(gqlError.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SHOP_IDS = "shopIds"

        @JvmStatic
        fun createParams(shopId: String): Map<String, Any> = mapOf(PARAM_SHOP_IDS to shopId.toIntOrZero())
    }
}
