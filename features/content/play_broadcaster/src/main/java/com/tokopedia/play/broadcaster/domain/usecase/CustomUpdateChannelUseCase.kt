package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.util.handler.DefaultUseCaseHandler
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.domain.model.ChannelId
import com.tokopedia.play_common.domain.model.UpdateChannelResponse
import javax.inject.Inject


/**
 * Created by mzennis on 06/11/20.
 */
class CustomUpdateChannelUseCase @Inject constructor(val graphqlRepository: GraphqlRepository): UpdateChannelUseCase(graphqlRepository) {

    override suspend fun executeOnBackground(): ChannelId {
        val gqlResponse = DefaultUseCaseHandler(
                gqlRepository = graphqlRepository,
                query = mQueryParams.query,
                typeOfT = UpdateChannelResponse::class.java,
                params = mQueryParams.params,
                gqlCacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
        ).executeWithRetry()
        val response = gqlResponse.getData<UpdateChannelResponse>(UpdateChannelResponse::class.java)
        return response.updateChannel
    }

    companion object {

        fun createUpdateFullCoverRequest(
                channelId: String,
                authorId: String,
                coverTitle: String,
                coverUrl: String
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.AuthorID.fieldName to authorId,
                    FieldsToUpdate.Title.fieldName to coverTitle,
                    FieldsToUpdate.Cover.fieldName to coverUrl
            )

            val query = buildQueryString(listOf(FieldsToUpdate.Title, FieldsToUpdate.Cover, FieldsToUpdate.AuthorID))

            return QueryParams(query, params)
        }

        fun createUpdateCoverTitleRequest(
                channelId: String,
                authorId: String,
                coverTitle: String
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.AuthorID.fieldName to authorId,
                    FieldsToUpdate.Title.fieldName to coverTitle
            )

            val query = buildQueryString(listOf(FieldsToUpdate.Title, FieldsToUpdate.AuthorID))

            return QueryParams(query, params)
        }
    }
}