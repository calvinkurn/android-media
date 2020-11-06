package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.domain.UpdateChannelUseCase.Companion.PARAMS_CHANNEL_ID
import com.tokopedia.play_common.domain.model.ChannelId
import com.tokopedia.play_common.domain.query.FieldsToUpdate
import com.tokopedia.play_common.domain.query.QueryParamBuilder
import com.tokopedia.play_common.domain.query.QueryParams
import javax.inject.Inject


/**
 * Created by mzennis on 06/11/20.
 */
class UpdateChannelUseCase @Inject constructor(private val updateChannelUseCase: UpdateChannelUseCase) {

    private var mQueryParams = QueryParams()

    suspend fun executeOnBackground(): ChannelId {
        updateChannelUseCase.setQueryParams(mQueryParams)
        return updateChannelUseCase.executeOnBackground()
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
    }
}