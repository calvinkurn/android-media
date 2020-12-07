package com.tokopedia.play_common.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play_common.domain.model.ChannelId
import com.tokopedia.play_common.domain.model.UpdateChannelResponse
import com.tokopedia.play_common.domain.query.FieldsToUpdate
import com.tokopedia.play_common.domain.query.QueryParamBuilder
import com.tokopedia.play_common.domain.query.QueryParams
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.usecase.coroutines.UseCase


/**
 * Created by mzennis on 26/06/20.
 */
class UpdateChannelUseCase(private val graphqlRepository: GraphqlRepository) : UseCase<ChannelId>() {

    private var mQueryParams = QueryParams()

    override suspend fun executeOnBackground(): ChannelId {
        val gqlRequest = GraphqlRequest(
                mQueryParams.query,
                UpdateChannelResponse::class.java,
                mQueryParams.params
        )
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<UpdateChannelResponse>(UpdateChannelResponse::class.java)
        return response.updateChannel
    }

    fun setQueryParams(queryParams: QueryParams) {
        mQueryParams = queryParams
    }

    companion object {

        const val PARAMS_CHANNEL_ID = "channelId"

        fun createUpdateStatusRequest(
                channelId: String,
                authorId: String,
                status: PlayChannelStatusType
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.AuthorID.fieldName to authorId,
                    FieldsToUpdate.Status.fieldName to status.value.toInt()
            )
            return QueryParamBuilder()
                    .setParams(params)
                    .setFields(listOf(FieldsToUpdate.Status, FieldsToUpdate.AuthorID))
                    .build()
        }
    }
}