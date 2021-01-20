package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play.broadcaster.util.error.DefaultNetworkThrowable
import com.tokopedia.play.broadcaster.util.handler.DefaultUseCaseHandler
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.domain.UpdateChannelUseCase.Companion.PARAMS_CHANNEL_ID
import com.tokopedia.play_common.domain.model.ChannelId
import com.tokopedia.play_common.domain.query.FieldsToUpdate
import com.tokopedia.play_common.domain.query.QueryParamBuilder
import com.tokopedia.play_common.domain.query.QueryParams
import com.tokopedia.play_common.types.PlayChannelStatusType
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


/**
 * Created by mzennis on 06/11/20.
 */
class PlayBroadcastUpdateChannelUseCase @Inject constructor(private val updateChannelUseCase: UpdateChannelUseCase) {

    private var mQueryParams = QueryParams()

    suspend fun executeOnBackground(): ChannelId {
        updateChannelUseCase.setQueryParams(mQueryParams)

        var response: ChannelId? = null

        var retryCount = 0
        suspend fun withRetry() {
            try {
                response = updateChannelUseCase.executeOnBackground()
            } catch (throwable: Throwable) {
                if (throwable is UnknownHostException || throwable is SocketTimeoutException) throw DefaultNetworkThrowable()
                else {
                    if (retryCount++ < DefaultUseCaseHandler.MAX_RETRY) withRetry()
                }
            }
        }

        withRetry()

        return response ?: throw DefaultErrorThrowable()
    }

    fun setQueryParams(queryParams: QueryParams) {
        mQueryParams = queryParams
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

            return QueryParamBuilder()
                    .setParams(params)
                    .setFields(listOf(FieldsToUpdate.Title, FieldsToUpdate.Cover, FieldsToUpdate.AuthorID))
                    .build()
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
            return QueryParamBuilder()
                    .setParams(params)
                    .setFields(listOf(FieldsToUpdate.Title, FieldsToUpdate.AuthorID))
                    .build()
        }

        fun createUpdateBroadcastScheduleRequest(
                channelId: String,
                status: PlayChannelStatusType,
                date: String
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.Status.fieldName to status.value.toInt(),
                    FieldsToUpdate.Schedule.fieldName to date,
            )
            return QueryParamBuilder()
                    .setParams(params)
                    .setFields(listOf(FieldsToUpdate.Status, FieldsToUpdate.Schedule))
                    .build()
        }

        fun createDeleteBroadcastScheduleRequest(
                channelId: String
        ): QueryParams {
            val params = mapOf(
                    PARAMS_CHANNEL_ID to channelId,
                    FieldsToUpdate.Status.fieldName to PlayChannelStatusType.Draft.value.toInt()
            )
            return QueryParamBuilder()
                    .setParams(params)
                    .setFields(listOf(FieldsToUpdate.Status))
                    .build()
        }
    }
}