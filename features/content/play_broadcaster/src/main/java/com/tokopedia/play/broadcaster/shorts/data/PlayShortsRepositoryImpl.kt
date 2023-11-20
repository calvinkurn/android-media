package com.tokopedia.play.broadcaster.shorts.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.usecase.GetWhiteListUseCase
import com.tokopedia.play.broadcaster.domain.usecase.CreateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetConfigurationUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.model.OnboardAffiliateRequestModel
import com.tokopedia.play.broadcaster.shorts.domain.usecase.BroadcasterCheckIsAffiliateUseCase
import com.tokopedia.play.broadcaster.shorts.domain.usecase.OnBoardAffiliateUseCase
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsMapper
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.shortsaffiliate.BroadcasterCheckAffiliateResponseUiModel
import com.tokopedia.play.broadcaster.ui.model.shortsaffiliate.OnboardAffiliateUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play_common.domain.usecase.broadcaster.PlayBroadcastUpdateChannelUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsRepositoryImpl @Inject constructor(
    private val getWhiteListUseCase: GetWhiteListUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val createChannelUseCase: CreateChannelUseCase,
    private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
    private val getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase,
    private val setChannelTagsUseCase: SetChannelTagsUseCase,
    private val mapper: PlayShortsMapper,
    private val dispatchers: CoroutineDispatchers,
    private val broadcasterCheckAffiliateUseCase: BroadcasterCheckIsAffiliateUseCase,
    private val onboardAffiliateUseCase: OnBoardAffiliateUseCase,
) : PlayShortsRepository {

    override suspend fun getAccountList(): List<ContentAccountUiModel> = withContext(dispatchers.io) {
        val response = getWhiteListUseCase(GetWhiteListUseCase.WhiteListType.EntryPoint)

        return@withContext mapper.mapAuthorList(response)
    }

    override suspend fun getShortsConfiguration(
        authorId: String,
        authorType: String
    ): PlayShortsConfigUiModel = withContext(dispatchers.io) {
        val response = getConfigurationUseCase(
            GetConfigurationUseCase.RequestParam(
                authorId = authorId,
                authorType = authorType,
            )
        )

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
    ): PlayTagUiModel = withContext(dispatchers.io) {
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

    override suspend fun getBroadcasterCheckAffiliate(): BroadcasterCheckAffiliateResponseUiModel {
        return withContext(dispatchers.io) {
            mapper.mapBroadcasterCheckAffiliate(broadcasterCheckAffiliateUseCase(Unit))
        }
    }

    override suspend fun submitOnboardAffiliateTnc(request: OnboardAffiliateRequestModel): OnboardAffiliateUiModel {
        return withContext(dispatchers.io) {
            val response = mapper.mapOnboardAffiliate(onboardAffiliateUseCase(request))
            if (response.errorMessage.isNotEmpty()) throw Throwable(response.errorMessage)
            response
        }
    }

}
