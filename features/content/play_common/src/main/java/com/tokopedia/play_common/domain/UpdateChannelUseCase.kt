package com.tokopedia.play_common.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.util.LoggingUtils
import com.tokopedia.network.exception.MessageErrorException
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
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val error = gqlResponse.getError(UpdateChannelResponse::class.java)
        if (error == null || error.isEmpty()) {
            val response = gqlResponse.getData<UpdateChannelResponse>(UpdateChannelResponse::class.java)
            return response.updateChannel
        } else {
            val errorMessage = error.mapNotNull { it.message }.joinToString(separator = ", ")
            LoggingUtils.logGqlErrorBackend("executeOnBackground", listOf(gqlRequest).toString()
                ,errorMessage, gqlResponse.httpStatusCode.toString())
            throw MessageErrorException(errorMessage, gqlResponse.httpStatusCode.toString())
        }
    }

    fun setQueryParams(queryParams: QueryParams) {
        mQueryParams = queryParams
    }

    companion object {

        const val PARAMS_CHANNEL_ID = "channelId"

        fun createUpdateStatusWithActiveMediaRequest(
            channelId: String,
            authorId: String,
            status: PlayChannelStatusType,
            activeMediaId: String,
        ): QueryParams {
            val params = mapOf(
                PARAMS_CHANNEL_ID to channelId,
                FieldsToUpdate.ActiveMediaId.fieldName to activeMediaId,
                FieldsToUpdate.AuthorID.fieldName to authorId,
                FieldsToUpdate.Status.fieldName to status.value.toLong()
            )
            return QueryParamBuilder()
                .setParams(params)
                .setFields(listOf(FieldsToUpdate.Status, FieldsToUpdate.AuthorID, FieldsToUpdate.ActiveMediaId))
                .build()
        }

        fun createUpdateStatusRequest(
                channelId: String,
                authorId: String,
                status: PlayChannelStatusType
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.AuthorID.fieldName to authorId,
                    FieldsToUpdate.Status.fieldName to status.value.toLong()
            )
            return QueryParamBuilder()
                    .setParams(params)
                    .setFields(listOf(FieldsToUpdate.Status, FieldsToUpdate.AuthorID))
                    .build()
        }
    }
}
