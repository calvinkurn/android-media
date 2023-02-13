package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.types.ContentCommonUserType.VALUE_TYPE_ID_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.VALUE_TYPE_ID_USER
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
               ${"$$PARAMS_STATUS"}: Int!,
               ${"$$PARAMS_GROUP_ID"}: String,
               ${"$$PARAMS_TYPE"}: Int!
           ){
            broadcasterCreateChannel(req: {
               $PARAMS_AUTHOR_ID: ${"$$PARAMS_AUTHOR_ID"},
               $PARAMS_AUTHOR_TYPE: ${"$$PARAMS_AUTHOR_TYPE"}, 
               $PARAMS_STATUS: ${"$$PARAMS_STATUS"},
               $PARAMS_GROUP_ID: ${"$$PARAMS_GROUP_ID"},
               $PARAMS_TYPE: ${"$$PARAMS_TYPE"}
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

        private const val PARAMS_AUTHOR_ID = "authorID"
        private const val PARAMS_AUTHOR_TYPE = "authorType"
        private const val PARAMS_STATUS = "status"
        private const val PARAMS_GROUP_ID = "groupID"
        private const val PARAMS_TYPE = "type"

        private const val VALUE_LIVE_STREAM_GROUP_ID_SHOP = "1"
        private const val VALUE_LIVE_STREAM_GROUP_ID_USER = "69"
        private const val VALUE_SHORTS_GROUP_ID_SHOP = "75"
        private const val VALUE_SHORTS_GROUP_ID_USER = "76"

        fun createParams(
            authorId: String,
            authorType: String,
            type: Type,
            status: PlayChannelStatusType = PlayChannelStatusType.Draft,
        ): Map<String, Any> = mapOf(
            PARAMS_AUTHOR_ID to authorId,
            PARAMS_AUTHOR_TYPE to when (authorType) {
                TYPE_USER -> VALUE_TYPE_ID_USER
                TYPE_SHOP -> VALUE_TYPE_ID_SHOP
                else -> 0
            },
            PARAMS_STATUS to status.value.toIntOrZero(),
            PARAMS_GROUP_ID to getGroupId(type, authorType).value,
            PARAMS_TYPE to type.value,
        )

        private fun getGroupId(type: Type, authorType: String): GroupId {
            return when(type) {
                Type.Livestream -> {
                    when(authorType) {
                        TYPE_SHOP -> GroupId.LivestreamShop
                        TYPE_USER -> GroupId.LivestreamUGC
                        else -> GroupId.Unknown
                    }
                }
                Type.Shorts -> {
                    when(authorType) {
                        TYPE_SHOP -> GroupId.ShortsShop
                        TYPE_USER -> GroupId.ShortsUGC
                        else -> GroupId.Unknown
                    }
                }
                else -> GroupId.Unknown
            }
        }
    }

    enum class Type(val value: Int) {
        Livestream(1), Shorts(3)
    }

    enum class GroupId(val value: String) {
        LivestreamUGC(VALUE_LIVE_STREAM_GROUP_ID_USER),
        LivestreamShop(VALUE_LIVE_STREAM_GROUP_ID_SHOP),
        ShortsUGC(VALUE_SHORTS_GROUP_ID_USER),
        ShortsShop(VALUE_SHORTS_GROUP_ID_SHOP),
        Unknown(""),
    }
}
