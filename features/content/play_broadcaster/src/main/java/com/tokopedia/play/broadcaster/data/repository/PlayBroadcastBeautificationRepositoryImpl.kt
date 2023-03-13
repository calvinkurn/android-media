package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.data.api.BeautificationAssetApi
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastBeautificationRepository
import com.tokopedia.play.broadcaster.domain.usecase.beautification.SetBeautificationConfigUseCase
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import com.tokopedia.play.broadcaster.util.asset.AssetPathHelper
import com.tokopedia.play.broadcaster.util.asset.manager.AssetManager
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 13, 2023
 */
class PlayBroadcastBeautificationRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val setBeautificationConfigUseCase: SetBeautificationConfigUseCase,
    private val beautificationAssetApi: BeautificationAssetApi,
    private val assetManager: AssetManager,
    private val assetPathHelper: AssetPathHelper,
) : PlayBroadcastBeautificationRepository {

    override suspend fun saveBeautificationConfig(
        authorId: String,
        authorType: String,
        beautificationConfig: BeautificationConfigUiModel
    ): Boolean = withContext(dispatchers.io) {
        /** TODO: for mocking purpose, delete this soon when GQL is available in prod */
        return@withContext true

        setBeautificationConfigUseCase.execute(
            authorId = authorId,
            authorType = authorType,
            beautificationConfig = beautificationConfig
        ).wrapper.success
    }

    override suspend fun downloadPresetAsset(url: String, fileName: String): Boolean = withContext(dispatchers.io) {
        val responseBody = beautificationAssetApi.downloadAsset(url)

        assetManager.unzipAndSave(
            responseBody = responseBody,
            fileName = fileName,
            filePath = assetPathHelper.getPresetFilePath(fileName),
            folderPath = assetPathHelper.presetDir,
        )
    }
}
