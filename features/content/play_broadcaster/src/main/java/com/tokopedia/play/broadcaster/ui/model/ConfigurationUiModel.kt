package com.tokopedia.play.broadcaster.ui.model


/**
 * Created by mzennis on 14/06/20.
 */
data class ConfigurationUiModel(
        val streamAllowed: Boolean,
        val haveOnGoingLive: Boolean,
        val channelId: Int,
        val durationConfig: DurationConfigUiModel,
        val productTagConfig: ProductTagConfigUiModel
)

data class DurationConfigUiModel(
        val duration: Long,
        val pauseDuration: Long,
        val errorMessage: String
)

data class ProductTagConfigUiModel(
        val maxProduct: Int,
        val minProduct: Int,
        val errorMessage: String
)