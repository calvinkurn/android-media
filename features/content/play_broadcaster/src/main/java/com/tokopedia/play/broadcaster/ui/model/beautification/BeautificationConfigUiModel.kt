package com.tokopedia.play.broadcaster.ui.model.beautification

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.play.broadcaster.domain.model.Config
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Jonathan Darwin on March 08, 2023
 */
@Parcelize
data class BeautificationConfigUiModel(
    val licenseLink: String,
    val modelLink: String,
    val customFaceAssetLink: String,
    val faceFilters: List<FaceFilterUiModel>,
    val presets: List<PresetFilterUiModel>,
) : Parcelable {

    val isUnknown: Boolean
        get() = this == Empty

    val isBeautificationApplied: Boolean
        get() = (faceFilters.any { it.active } && !selectedFaceFilter?.isRemoveEffect.orFalse()) ||
            presets.any { it.isSelected && it.value != 0.0 && !it.isRemoveEffect }

    val faceFiltersWithoutNoneOption: List<FaceFilterUiModel>
        get() = faceFilters.filter { !it.isRemoveEffect }

    val selectedFaceFilter: FaceFilterUiModel?
        get() = faceFilters.firstOrNull { it.isSelected }

    val selectedPreset: PresetFilterUiModel?
        get() = presets.firstOrNull { it.isSelected }

    fun convertToDTO() = Config.BeautificationConfig(
        license = licenseLink,
        model = modelLink,
        customFace = Config.BeautificationConfig.CustomFace(
            assetAndroid = customFaceAssetLink,
            menu = faceFilters.map { faceFilter ->
                Config.BeautificationConfig.CustomFace.Menu(
                    id = faceFilter.id,
                    name = faceFilter.name,
                    active = faceFilter.active,
                    minValue = faceFilter.minValue,
                    maxValue = faceFilter.maxValue,
                    defaultValue = faceFilter.defaultValue,
                    value = faceFilter.value
                )
            }
        ),
        presets = presets.map { preset ->
            Config.BeautificationConfig.Preset(
                id = preset.id,
                name = preset.name,
                active = preset.isSelected,
                minValue = preset.minValue,
                maxValue = preset.maxValue,
                defaultValue = preset.defaultValue,
                value = preset.value,
                urlIcon = preset.iconUrl,
                assetLink = preset.assetLink,
            )
        }
    )

    companion object {
        val Empty: BeautificationConfigUiModel
            get() = BeautificationConfigUiModel(
                licenseLink = "",
                modelLink = "",
                customFaceAssetLink = "",
                faceFilters = emptyList(),
                presets = emptyList(),
            )
    }
}
