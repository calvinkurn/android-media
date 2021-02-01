package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 28/01/21
 */
sealed class PlayPinnedUiModel {

    data class PinnedMessage(
            val id: String,
            val applink: String,
            val partnerName: String,
            val title: String,
    ) : PlayPinnedUiModel()

    data class PinnedProduct(
            val partnerName: String,
            val title: String,
            val hasPromo: Boolean,
            val shouldShow: Boolean,
            val productTags: PlayProductTagsUiModel
    ) : PlayPinnedUiModel()

    object NoPinned : PlayPinnedUiModel()
}

val PlayPinnedUiModel.PinnedMessage.shouldShow: Boolean
    get() = id.isNotEmpty() && !id.contentEquals("0") && title.isNotEmpty()