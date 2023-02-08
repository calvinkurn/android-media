package com.tokopedia.play.broadcaster.shorts.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsMapper
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play_common.domain.usecase.broadcaster.PlayBroadcastUpdateChannelUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsRepositoryImpl @Inject constructor(
    private val getWhiteListNewUseCase: GetWhiteListNewUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val createChannelUseCase: CreateChannelUseCase,
    private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
    private val getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase,
    private val setChannelTagsUseCase: SetChannelTagsUseCase,
    private val mapper: PlayShortsMapper,
    private val dispatchers: CoroutineDispatchers
) : PlayShortsRepository {

    override suspend fun getAccountList(): List<ContentAccountUiModel> = withContext(dispatchers.io) {
        val response = getWhiteListNewUseCase.execute(type = GetWhiteListNewUseCase.WHITELIST_ENTRY_POINT)

        return@withContext mapper.mapAuthorList(response)
    }

    override suspend fun getShortsConfiguration(
        authorId: String,
        authorType: String
    ): PlayShortsConfigUiModel = withContext(dispatchers.io) {
        val response = getConfigurationUseCase.execute(authorId, authorType)

        mapper.mapShortsConfig(response)
    }

    override suspend fun createShorts(authorId: String, authorType: String): String = withContext(dispatchers.io) {
        val response = createChannelUseCase.apply {
            params = CreateChannelUseCase.createParams(
                authorId = authorId,
                authorType = authorType,
                type = CreateChannelUseCase.Type.Shorts,
            )
        }.executeOnBackground()

        return@withContext response.id
    }

    override suspend fun uploadTitle(title: String, shortsId: String, authorId: String) {
        withContext(dispatchers.io) {
            updateChannelUseCase.apply {
                setQueryParams(
                    PlayBroadcastUpdateChannelUseCase.createUpdateTitleRequest(
                        channelId = shortsId,
                        authorId = authorId,
                        title = title
                    )
                )
            }.executeOnBackground()
        }
    }

    override suspend fun getTagRecommendation(
        creationId: String,
    ): Set<PlayTagUiModel> = withContext(dispatchers.io) {
        val response = getRecommendedChannelTagsUseCase.apply {
            setChannelId(creationId)
        }.executeOnBackground()

        mapper.mapTagRecommendation(response)
    }

    override suspend fun saveTag(
        shortsId: String,
        tags: Set<String>
    ): Boolean = withContext(dispatchers.io) {
        setChannelTagsUseCase.apply {
            setParams(shortsId, tags)
        }.executeOnBackground().recommendedTags.success
    }
}
