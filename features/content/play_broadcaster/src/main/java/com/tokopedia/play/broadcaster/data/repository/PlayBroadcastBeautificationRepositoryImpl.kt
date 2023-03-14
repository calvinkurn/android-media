package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.data.api.BeautificationAssetApi
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastBeautificationRepository
import com.tokopedia.play.broadcaster.domain.usecase.beautification.SetBeautificationConfigUseCase
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import com.tokopedia.play.broadcaster.util.asset.AssetHelper
import com.tokopedia.play.broadcaster.util.asset.AssetPathHelper
import com.tokopedia.play.broadcaster.util.asset.FileUtil
import com.tokopedia.play.broadcaster.util.asset.checker.AssetChecker
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
    private val assetChecker: AssetChecker,
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

    override suspend fun downloadLicense(url: String): Boolean = withContext(dispatchers.io) {
        val licenseName = AssetHelper.getFileNameFromLink(url)

        if(assetChecker.isLicenseAvailable(licenseName)) {
            true
        } else {
            assetManager.deleteDirectory(assetPathHelper.licenseDir)

            val responseBody = beautificationAssetApi.downloadAsset(url)

            assetManager.save(
                responseBody = responseBody,
                fileName = licenseName,
                folderPath = assetPathHelper.licenseDir,
            )
        }
    }

    override suspend fun downloadModel(url: String): Boolean = withContext(dispatchers.io) {
        if(assetChecker.isModelAvailable(url)) {
            true
        } else {
            assetManager.deleteDirectory(assetPathHelper.modelDir)

            val responseBody = beautificationAssetApi.downloadAsset(url)

            assetManager.unzipAndSave(
                responseBody = responseBody,
                fileName = AssetHelper.getFileNameFromLinkWithoutExtension(url),
                filePath = assetPathHelper.modelDir,
                folderPath = assetPathHelper.effectRootDir,
            )
        }
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
