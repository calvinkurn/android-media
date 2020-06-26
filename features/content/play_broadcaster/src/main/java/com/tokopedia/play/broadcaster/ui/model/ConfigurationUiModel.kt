package com.tokopedia.play.broadcaster.ui.model


/**
 * Created by mzennis on 14/06/20.
 */
data class ConfigurationUiModel(
        val streamAllowed: Boolean,
        val channelId: String,
        val channelStatus: PlayChannelStatus,
        val durationConfig: DurationConfigUiModel,
        val productTagConfig: ProductTagConfigUiModel,
        val countDown: Long // second
)

data class DurationConfigUiModel(
        val duration: Long, // second
        val pauseDuration: Long, // second
        val errorMessage: String
)

data class ProductTagConfigUiModel(
        val maxProduct: Int,
        val minProduct: Int,
        val errorMessage: String
)