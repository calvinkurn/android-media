package com.tokopedia.play.broadcaster.ui.model


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
        val countDown: Long // second
)

data class DurationConfigUiModel(
        val duration: Long, // second
        val pauseDuration: Long, // second
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