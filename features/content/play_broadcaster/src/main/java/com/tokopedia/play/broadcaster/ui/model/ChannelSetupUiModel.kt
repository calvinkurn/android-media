package com.tokopedia.play.broadcaster.ui.model


/**
 * Created by mzennis on 08/06/20.
 */
data class ChannelSetupUiModel(
        val cover: PlayCoverUiModel,
        val selectedProductList: List<ProductContentUiModel>
)