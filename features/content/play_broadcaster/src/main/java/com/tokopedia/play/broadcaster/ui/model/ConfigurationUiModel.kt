package com.tokopedia.play.broadcaster.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.domain.model.Config
import java.util.*

/**
 * Created by mzennis on 14/06/20.
 */
@Parcelize
data class ConfigurationUiModel(
    val streamAllowed: Boolean,
    val shortVideoAllowed: Boolean,
    val channelId: String,
    val channelStatus: ChannelStatus,
    val durationConfig: DurationConfigUiModel,
    val productTagConfig: ProductTagConfigUiModel,
    val coverConfig: CoverConfigUiModel,
    val countDown: Long, // second
    val scheduleConfig: BroadcastScheduleConfigUiModel,
    val tnc: List<TermsAndConditionUiModel>,
    val beautificationConfig: BeautificationConfigUiModel,
) : Parcelable

@Parcelize
data class DurationConfigUiModel(
    val remainingDuration: Long, // millis
    val maxDuration: Long, // millis
    val pauseDuration: Long, // millis
    val maxDurationDesc: String,
) : Parcelable

@Parcelize
data class ProductTagConfigUiModel(
    val maxProduct: Int,
    val minProduct: Int,
    val maxProductDesc: String,
    val errorMessage: String
) : Parcelable

@Parcelize
data class CoverConfigUiModel(
    val maxChars: Int
) : Parcelable

@Parcelize
data class BroadcastScheduleConfigUiModel(
    val minimum: Date,
    val maximum: Date,
    val default: Date
) : Parcelable

@Parcelize
data class BeautificationConfigUiModel(
    val license: String,
    val model: String,
    val faceFilters: List<FaceFilterUiModel>,
    val presets: List<FaceFilterUiModel>,
) : Parcelable {

    val isUnknown: Boolean
        get() = this == Empty

    val isBeautificationApplied: Boolean
        get() = faceFilters.any { it.isChecked } || presets.any { it.isChecked }

    val isBeautificationOptionSelected: Boolean
        get() = selectedFaceFilter != null || presets != null

    val selectedFaceFilter: FaceFilterUiModel?
        get() = faceFilters.firstOrNull { it.isSelected }

    val selectedPreset: FaceFilterUiModel?
        get() = presets.firstOrNull { it.isSelected }

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
                license = "",
                model = "",
                faceFilters = emptyList(),
                presets = emptyList(),
            )
    }
}
