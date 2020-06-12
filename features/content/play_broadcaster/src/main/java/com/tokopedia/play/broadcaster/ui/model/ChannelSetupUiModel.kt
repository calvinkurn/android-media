package com.tokopedia.play.broadcaster.ui.model

import android.net.Uri


/**
 * Created by mzennis on 08/06/20.
 */
data class ChannelSetupUiModel(
        val title: String,
        val coverUrl: String,
        val coverUri: Uri?,
        val selectedProductList: List<ProductContentUiModel>
)