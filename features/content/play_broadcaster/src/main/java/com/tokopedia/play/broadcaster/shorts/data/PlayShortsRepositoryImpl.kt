package com.tokopedia.play.broadcaster.shorts.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase
import com.tokopedia.play.broadcaster.domain.usecase.CreateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetConfigurationUseCase
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsMapper
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import kotlinx.coroutines.delay
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
    private val mapper: PlayShortsMapper,
    private val dispatchers: CoroutineDispatchers
) : PlayShortsRepository {

    override suspend fun getAccountList(): List<ContentAccountUiModel> = withContext(dispatchers.io) {
        listOf(
            ContentAccountUiModel(
                id = "123",
                name = "Shop",
                iconUrl = "",
                badge = "",
                type = "content-shop",
                hasUsername = true,
                hasAcceptTnc = true,
            ),
            ContentAccountUiModel(
                id = "456",
                name = "UGC",
                iconUrl = "",
                badge = "",
                type = "content-user",
                hasUsername = true,
                hasAcceptTnc = true,
            )
        )

        /** TODO: will uncomment this later */
//        val response = getWhiteListNewUseCase.execute(type = GetWhiteListNewUseCase.WHITELIST_ENTRY_POINT)
//
//        return@withContext mapper.mapAuthorList(response)
    }

    override suspend fun getShortsConfiguration(
        authorId: String,
        authorType: String
    ): PlayShortsConfigUiModel = withContext(dispatchers.io) {
        PlayShortsConfigUiModel(
            shortsId = "123",
            shortsAllowed = true,
            tncList = List(3) {
                TermsAndConditionUiModel("Desc $it")
            },
            maxTitleCharacter = 24,
            maxTaggedProduct = 30,
        )

        /** TODO: will uncomment this later */
//        val response = getConfigurationUseCase.execute(authorId, authorType)
//
//        mapper.mapShortsConfig(response)
    }

    override suspend fun createShorts(authorId: String, authorType: String): String = withContext(dispatchers.io) {
        delay(1000)
        return@withContext "123"

        /** TODO: will uncomment this later */
//        val response = createChannelUseCase.apply {
//            params = CreateChannelUseCase.createParams(
//                authorId = authorId,
//                authorType = authorType,
//                type = CreateChannelUseCase.Type.Shorts,
//            )
//        }.executeOnBackground()
//
//        return@withContext response.id
    }

    override suspend fun uploadTitle(title: String, shortsId: String, authorId: String) {
        /** TODO: will uncomment this later */
//        withContext(dispatchers.io) {
//            updateChannelUseCase.apply {
//                setQueryParams(
//                    PlayBroadcastUpdateChannelUseCase.createUpdateTitleRequest(
//                        channelId = shortsId,
//                        authorId = authorId,
//                        title = title
//                    )
//                )
//            }.executeOnBackground()
//        }
    }
}
