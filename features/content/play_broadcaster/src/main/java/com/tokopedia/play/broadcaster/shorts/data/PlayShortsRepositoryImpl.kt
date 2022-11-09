package com.tokopedia.play.broadcaster.shorts.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetConfigurationUseCase
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsMapper
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsRepositoryImpl @Inject constructor(
    private val getWhiteListNewUseCase: GetWhiteListNewUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val mapper: PlayShortsMapper,
    private val dispatchers: CoroutineDispatchers,
) : PlayShortsRepository {

    override suspend fun getAccountList(): List<ContentAccountUiModel> = withContext(dispatchers.io) {
        val response = getWhiteListNewUseCase.execute(type = GetWhiteListNewUseCase.WHITELIST_ENTRY_POINT)

        return@withContext mapper.mapAuthorList(response)
    }
}
