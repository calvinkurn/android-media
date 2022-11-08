package com.tokopedia.play.broadcaster.shorts.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
class PlayShortsRepositoryImpl @Inject constructor(
    private val mapper: PlayShortsMapper,
    private val dispatchers: CoroutineDispatchers,
    private val getWhiteListNewUseCase: GetWhiteListNewUseCase,
) : PlayShortsRepository {

    override suspend fun getAccountList(): List<ContentAccountUiModel> = withContext(dispatchers.io) {
        val response = getWhiteListNewUseCase.execute(type = GetWhiteListNewUseCase.WHITELIST_ENTRY_POINT)

        return@withContext mapper.mapAuthorList(response)
    }
}
