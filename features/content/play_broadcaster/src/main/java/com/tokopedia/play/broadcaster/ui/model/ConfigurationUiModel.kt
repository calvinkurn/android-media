package com.tokopedia.play.broadcaster.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
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
    val customFace: CustomFace,
    val presets: List<Preset>,
) : Parcelable {

    @Parcelize
    data class CustomFace(
        val assetAndroid: String,
        val menu: List<Menu>,
    ) : Parcelable {

        @Parcelize
        data class Menu(
            val name: String,
            val minValue: Double,
            val maxValue: Double,
            val defaultValue: Double,
            val value: Double,
        ) : Parcelable
    }

    @Parcelize
    data class Preset(
        val name: String,
        val active: Boolean,
        val minValue: Double,
        val maxValue: Double,
        val defaultValue: Double,
        val value: Double,
        val urlIcon: String,
        val assetLink: String,
    ) : Parcelable
}
