package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.data.api.BeautificationAssetApi
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastBeautificationRepository
import com.tokopedia.play.broadcaster.domain.usecase.beautification.SetBeautificationConfigUseCase
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import com.tokopedia.byteplus.effect.util.asset.AssetHelper
import com.tokopedia.byteplus.effect.util.asset.checker.AssetChecker
import com.tokopedia.byteplus.effect.util.asset.manager.AssetManager
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
    private val assetHelper: AssetHelper,
    private val assetChecker: AssetChecker,
) : PlayBroadcastBeautificationRepository {

    override suspend fun saveBeautificationConfig(
        authorId: String,
        authorType: String,
        beautificationConfig: BeautificationConfigUiModel
    ): Boolean = withContext(dispatchers.io) {
        setBeautificationConfigUseCase(
            SetBeautificationConfigUseCase.RequestParam(
                authorId = authorId,
                authorType = authorType,
                beautificationConfig = beautificationConfig
            )
        ).wrapper.success
    }

    override suspend fun downloadLicense(url: String): Boolean = withContext(dispatchers.io) {
        val licenseName = assetHelper.getFileNameFromLink(url)

        if (assetChecker.isLicenseAvailable(licenseName)) {
            true
        } else {
            assetManager.deleteDirectory(assetHelper.licenseDir)

            val responseBody = beautificationAssetApi.downloadAsset(url)

            assetManager.save(
                responseBody = responseBody,
                fileName = licenseName,
                folderPath = assetHelper.licenseDir,
            )
        }
    }

    override suspend fun downloadModel(url: String): Boolean = withContext(dispatchers.io) {
        if (assetChecker.isModelAvailable()) {
            true
        } else {
            assetManager.deleteDirectory(assetHelper.modelDir)

            val responseBody = beautificationAssetApi.downloadAsset(url)

            assetManager.unzipAndSave(
                responseBody = responseBody,
                fileName = assetHelper.getFileNameFromLinkWithoutExtension(url),
                filePath = assetHelper.modelDir,
                folderPath = assetHelper.effectRootDir,
            )
        }
    }

    override suspend fun downloadCustomFace(url: String): Boolean = withContext(dispatchers.io) {
        if (assetChecker.isCustomFaceAvailable()) {
            true
        } else {
            assetManager.deleteDirectory(assetHelper.customFaceDir)

            val responseBody = beautificationAssetApi.downloadAsset(url)

            assetManager.unzipAndSave(
                responseBody = responseBody,
                fileName = assetHelper.getFileNameFromLink(url),
                filePath = assetHelper.composeMakeupDir,
                folderPath = assetHelper.composeMakeupDir,
            )
        }
    }

    override suspend fun downloadPresetAsset(url: String, fileName: String): Boolean = withContext(dispatchers.io) {
        if (assetChecker.isPresetFileAvailable(fileName)) {
            true
        } else {
            assetManager.deleteDirectory(assetHelper.getPresetFilePath(fileName))
            val responseBody = beautificationAssetApi.downloadAsset(url)

            assetManager.unzipAndSave(
                responseBody = responseBody,
                fileName = fileName,
                filePath = assetHelper.getPresetFilePath(fileName),
                folderPath = assetHelper.presetDir,
            )
        }
    }
}
