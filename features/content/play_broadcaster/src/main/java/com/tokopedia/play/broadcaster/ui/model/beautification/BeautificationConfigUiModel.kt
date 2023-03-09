package com.tokopedia.play.broadcaster.ui.model.beautification

import android.os.Parcelable
import com.tokopedia.play.broadcaster.domain.model.Config
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Jonathan Darwin on March 08, 2023
 */
@Parcelize
data class BeautificationConfigUiModel(
    val license: String,
    val model: String,
    val faceFilters: List<FaceFilterUiModel>,
    val presets: List<PresetFilterUiModel>,
) : Parcelable {

    val isUnknown: Boolean
        get() = this == Empty

    val isBeautificationApplied: Boolean
        get() = faceFilters.any { it.isChecked } || presets.any { it.active && !it.isRemoveEffect }

    val selectedFaceFilter: FaceFilterUiModel?
        get() = faceFilters.firstOrNull { it.isSelected }

    val selectedPreset: PresetFilterUiModel?
        get() = presets.firstOrNull { it.active }

    fun convertToDTO() = Config.BeautificationConfig(
        license = license,
        model = model,
        customFace = Config.BeautificationConfig.CustomFace(
            assetAndroid = faceFilters.firstOrNull()?.assetLink.orEmpty(),
            menu = faceFilters.map { faceFilter ->
                Config.BeautificationConfig.CustomFace.Menu(
                    name = faceFilter.name,
                    minValue = faceFilter.minValue,
                    maxValue = faceFilter.maxValue,
                    defaultValue = faceFilter.defaultValue,
                    value = faceFilter.value
                )
            }
        ),
        presets = presets.map { preset ->
            Config.BeautificationConfig.Preset(
                name = preset.name,
                active = preset.active,
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
                license = "",
                model = "",
                faceFilters = emptyList(),
                presets = emptyList(),
            )
    }
}
