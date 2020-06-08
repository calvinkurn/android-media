package com.tokopedia.play.broadcaster.ui.model


/**
 * Created by mzennis on 08/06/20.
 */
data class ChannelSetupUiModel(
        val title: String,
        val coverUrl: String,
        val selectedProductList: List<ProductContentUiModel>
)