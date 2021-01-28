package com.tokopedia.play.broadcaster.ui.model

import java.util.*


/**
 * Created by mzennis on 14/06/20.
 */
data class ConfigurationUiModel(
        val streamAllowed: Boolean,
        val channelId: String,
        val channelType: ChannelType,
        val remainingTime: Long, // millis
        val timeElapsed: String,
        val durationConfig: DurationConfigUiModel,
        val productTagConfig: ProductTagConfigUiModel,
        val coverConfig: CoverConfigUiModel,
        val countDown: Long, // second
        val scheduleConfig: BroadcastScheduleConfigUiModel
)

data class DurationConfigUiModel(
        val duration: Long, // millis
        val pauseDuration: Long, // millis
        val maxDurationDesc: String,
        val errorMessage: String
)

data class ProductTagConfigUiModel(
        val maxProduct: Int,
        val minProduct: Int,
        val maxProductDesc: String,
        val errorMessage: String
)

data class CoverConfigUiModel(
        val maxChars: Int
)

data class BroadcastScheduleConfigUiModel(
        val minimum: Date,
        val maximum: Date,
        val default: Date
)