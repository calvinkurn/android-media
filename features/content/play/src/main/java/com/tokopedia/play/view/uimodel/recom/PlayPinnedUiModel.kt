package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 28/01/21
 */
data class PinnedMessageUiModel(
        val id: String,
        val applink: String,
        val partnerName: String,
        val title: String,
)

data class PinnedProductUiModel(
        val partnerName: String,
        val title: String,
        val hasPromo: Boolean,
        val shouldShow: Boolean,
        val productTags: PlayProductTagsUiModel
)

val PinnedMessageUiModel.shouldShow: Boolean
    get() = id.isNotEmpty() && !id.contentEquals("0") && title.isNotEmpty()