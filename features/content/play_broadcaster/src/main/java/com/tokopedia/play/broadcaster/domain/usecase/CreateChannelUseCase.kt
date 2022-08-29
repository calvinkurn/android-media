package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.types.ContentCommonUserType.VALUE_TYPE_ID_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.VALUE_TYPE_ID_USER
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play_common.domain.model.ChannelId
import com.tokopedia.play.broadcaster.domain.model.CreateChannelBroadcastResponse
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play.broadcaster.util.handler.DefaultUseCaseHandler
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 05/06/20.
 */
class CreateChannelUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<ChannelId>() {

    private val query = """
           mutation createChannel(
           ${"$$PARAMS_AUTHOR_ID"}: String!, 
           ${"$$PARAMS_AUTHOR_TYPE"}: Int!, 
           ${"$$PARAMS_STATUS"}: Int!
           ){
              broadcasterCreateChannel(req: {
                    $PARAMS_AUTHOR_ID: ${"$$PARAMS_AUTHOR_ID"},
                    $PARAMS_AUTHOR_TYPE: ${"$$PARAMS_AUTHOR_TYPE"}, 
                    $PARAMS_STATUS: ${"$$PARAMS_STATUS"}
              }){
                channelID
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): ChannelId {
        val gqlResponse = DefaultUseCaseHandler(
                gqlRepository = graphqlRepository,
                query = query,
                typeOfT = CreateChannelBroadcastResponse::class.java,
                params = params,
                gqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        ).executeWithRetry()
        val response = gqlResponse.getData<CreateChannelBroadcastResponse>(CreateChannelBroadcastResponse::class.java)
        return response.getChannelId
    }

    companion object {

        private const val PARAMS_AUTHOR_ID = "authorId"
        private const val PARAMS_AUTHOR_TYPE = "authorType"
        private const val PARAMS_STATUS = "status"

        fun createParams(
            authorId: String,
            authorType: String,
            status: PlayChannelStatusType = PlayChannelStatusType.Draft
        ): Map<String, Any> = mapOf(
            PARAMS_AUTHOR_ID to authorId,
            PARAMS_AUTHOR_TYPE to when (authorType) {
                TYPE_USER -> VALUE_TYPE_ID_USER
                TYPE_SHOP -> VALUE_TYPE_ID_SHOP
                else -> 0
            },
            PARAMS_STATUS to status.value.toInt()
        )
    }

}