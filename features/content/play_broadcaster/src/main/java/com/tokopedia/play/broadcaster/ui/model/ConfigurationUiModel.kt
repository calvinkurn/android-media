package com.tokopedia.play.broadcaster.ui.model

import android.os.Parcelable
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by mzennis on 14/06/20.
 */
@Parcelize
data class ConfigurationUiModel(
    val streamAllowed: Boolean,
    val shortVideoAllowed: Boolean,
    val hasContent: Boolean,
    val channelId: String,
    val channelStatus: ChannelStatus,
    val durationConfig: DurationConfigUiModel,
    val productTagConfig: ProductTagConfigUiModel,
    val coverConfig: CoverConfigUiModel,
    val countDown: Long, // second
    val scheduleConfig: BroadcastScheduleConfigUiModel,
    val tnc: List<TermsAndConditionUiModel>,
    val beautificationConfig: BeautificationConfigUiModel,
    val showSaveButton: Boolean,
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
