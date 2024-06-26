package com.tokopedia.play_common.domain.usecase.broadcaster

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.domain.model.ChannelId
import com.tokopedia.play_common.domain.query.FieldsToUpdate
import com.tokopedia.play_common.domain.query.QueryParamBuilder
import com.tokopedia.play_common.domain.query.QueryParams
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.error.DefaultErrorThrowable
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
                if (throwable is UnknownHostException || throwable is SocketTimeoutException) throw throwable
                else {
                    if (retryCount++ < MAX_RETRY) withRetry()
                    else throw throwable
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

        private const val MAX_RETRY = 5

        fun createUpdateFullCoverRequest(
            channelId: String,
            authorId: String,
            coverUrl: String
        ): QueryParams {
            val params = mapOf(
                UpdateChannelUseCase.PARAMS_CHANNEL_ID to channelId,
                FieldsToUpdate.AuthorID.fieldName to authorId,
                FieldsToUpdate.Cover.fieldName to coverUrl
            )

            return QueryParamBuilder()
                .setParams(params)
                .setFields(listOf(FieldsToUpdate.Cover, FieldsToUpdate.AuthorID))
                .build()
        }

        fun createUpdateTitleRequest(
            channelId: String,
            authorId: String,
            title: String
        ): QueryParams {
            val params = mapOf(
                UpdateChannelUseCase.PARAMS_CHANNEL_ID to channelId,
                FieldsToUpdate.AuthorID.fieldName to authorId,
                FieldsToUpdate.Title.fieldName to title
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
                UpdateChannelUseCase.PARAMS_CHANNEL_ID to channelId,
                FieldsToUpdate.Status.fieldName to status.value.toIntOrZero(),
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
                UpdateChannelUseCase.PARAMS_CHANNEL_ID to channelId,
                FieldsToUpdate.Status.fieldName to PlayChannelStatusType.Draft.value.toIntOrZero()
            )
            return QueryParamBuilder()
                .setParams(params)
                .setFields(listOf(FieldsToUpdate.Status))
                .build()
        }
    }
}
