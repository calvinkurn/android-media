package com.tokopedia.play.broadcaster.shorts.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsMapper
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import kotlinx.coroutines.delay
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
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
//        delay(500)
//
//        listOf(
//            ContentAccountUiModel(
//                id = "123",
//                name = "Shop",
//                iconUrl = "",
//                badge = "",
//                type = "content-shop",
//                hasUsername = true,
//                enable = true,
//            ),
//            ContentAccountUiModel(
//                id = "456",
//                name = "UGC",
//                iconUrl = "",
//                badge = "",
//                type = "content-user",
//                hasUsername = true,
//                enable = false,
//            )
//        )

        /** TODO: will uncomment this later */
        val response = getWhiteListNewUseCase.execute(type = GetWhiteListNewUseCase.WHITELIST_ENTRY_POINT)

        return@withContext mapper.mapAuthorList(response)
    }

    override suspend fun getShortsConfiguration(
        authorId: String,
        authorType: String
    ): PlayShortsConfigUiModel = withContext(dispatchers.io) {
//        delay(500)
//
//        PlayShortsConfigUiModel(
//            shortsId = "123",
//            shortsAllowed = true,
//            tncList = List(3) {
//                TermsAndConditionUiModel("Desc $it")
//            },
//            maxTitleCharacter = 24,
//            maxTaggedProduct = 30,
//        )

        /** TODO: will uncomment this later */
        val response = getConfigurationUseCase.execute(authorId, authorType)

        mapper.mapShortsConfig(response)
    }

    override suspend fun createShorts(authorId: String, authorType: String): String = withContext(dispatchers.io) {
//        delay(1000)
//        return@withContext "123"

        /** TODO: will uncomment this later */
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
//        delay(1000)

        /** TODO: will uncomment this later */
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
//        true

        /** TODO: will uncomment this later */
        setChannelTagsUseCase.apply {
            setParams(shortsId, tags)
        }.executeOnBackground().recommendedTags.success
    }
}
