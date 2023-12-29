package com.tokopedia.play.broadcaster.model.beautification

import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationAssetStatus
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.PresetFilterUiModel

/**
 * Created By : Jonathan Darwin on April 10, 2023
 */
class BeautificationConfigUiModelBuilder {

    fun buildBeautificationConfig(
        licenseLink: String = "licenseLink",
        modelLink: String = "modelLink",
        customFaceAssetLink: String = "customFaceAssetLink",
        faceFiltersSize: Int = 5,
        presetsSize: Int = 5,
        presetActivePosition: Int = 1,
        assetStatus: BeautificationAssetStatus = BeautificationAssetStatus.Available,
    ) = BeautificationConfigUiModel(
        licenseLink = licenseLink,
        modelLink = modelLink,
        customFaceAssetLink = customFaceAssetLink,
        faceFilters = List(faceFiltersSize) {
            FaceFilterUiModel(
                id = if (it == 0) "none" else it.toString(),
                name = "Face Filter $it",
                active = it == 1,
                minValue = 0.0,
                maxValue = 1.0,
                defaultValue = 0.1 * it,
                value = 0.1 * it,
                isSelected = false,
            )
        },
        presets = List(presetsSize) {
            PresetFilterUiModel(
                id = if (it == 0) "none" else it.toString(),
                name = "Preset $it",
                active = it == presetActivePosition,
                minValue = 0.0,
                maxValue = 1.0,
                defaultValue = 0.1 * it,
                value = 0.1 * it,
                iconUrl = "iconUrl $it",
                assetLink = "assetLink $it",
                assetStatus = if (it == 0) BeautificationAssetStatus.Available else assetStatus,
                isSelected = false,
            )
        }
    )
}
