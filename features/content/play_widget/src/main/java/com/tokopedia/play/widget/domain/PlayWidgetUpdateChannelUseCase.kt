package com.tokopedia.play.widget.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by jegul on 09/11/20
 */
class PlayWidgetUpdateChannelUseCase(
        graphqlRepository: GraphqlRepository
) : UseCase<String>() {

    private val updateChannelUseCase = UpdateChannelUseCase(graphqlRepository)

    internal fun setQueryParams(channelId: String, authorId: String, status: PlayChannelStatusType) {
        updateChannelUseCase.setQueryParams(
                UpdateChannelUseCase.createUpdateStatusRequest(channelId, authorId, status)
        )
    }

    override suspend fun executeOnBackground(): String {
        return updateChannelUseCase.executeOnBackground().id
    }
}