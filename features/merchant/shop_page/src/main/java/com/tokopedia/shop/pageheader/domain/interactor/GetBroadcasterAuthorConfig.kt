package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.content.common.types.ContentCommonUserType.VALUE_TYPE_ID_SHOP
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by jegul on 24/07/20
 */
class GetBroadcasterAuthorConfig(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<Broadcaster.Config>() {

    var params: RequestParams = RequestParams.EMPTY
    private val query = """
            query BroadcasterGetAuthorConfig(
                ${"$$PARAMS_AUTHOR_ID"}: Int64!, 
                ${"$$PARAMS_AUTHOR_TYPE"}: Int!, 
                ${"$$PARAMS_WITH_CHANNEL_STATE"}: Boolean
            ) {
              broadcasterGetAuthorConfig(
                 $PARAMS_AUTHOR_ID: ${"$$PARAMS_AUTHOR_ID"}, 
                 $PARAMS_AUTHOR_TYPE: ${"$$PARAMS_AUTHOR_TYPE"}, 
                 $PARAMS_WITH_CHANNEL_STATE: ${"$$PARAMS_WITH_CHANNEL_STATE"}
              ) {
                streamAllowed
                shortVideoAllowed
                hasContent
              }
            }
        """
    val request by lazy {
        GraphqlRequest(query, Broadcaster::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): Broadcaster.Config {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
        )

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(Broadcaster::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(Broadcaster::class.java) as Broadcaster).config
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAMS_AUTHOR_ID = "authorID"
        private const val PARAMS_AUTHOR_TYPE = "authorType"
        private const val PARAMS_WITH_CHANNEL_STATE = "withChannelState"
        private const val VALUE_WITH_CHANNEL_STATE = true

        @JvmStatic
        fun createParams(
            authorId: String,
        ): RequestParams = RequestParams.create().apply {
            putObject(PARAMS_AUTHOR_ID, authorId)
            putObject(PARAMS_AUTHOR_TYPE, VALUE_TYPE_ID_SHOP)
            putObject(PARAMS_WITH_CHANNEL_STATE, VALUE_WITH_CHANNEL_STATE)
        }
    }
}
