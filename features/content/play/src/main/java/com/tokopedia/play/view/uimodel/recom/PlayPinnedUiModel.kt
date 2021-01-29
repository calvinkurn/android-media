package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 28/01/21
 */
sealed class PlayPinnedUiModel {

    data class PinnedMessage(
            val applink: String,
            val partnerName: String,
            val title: String,
            val shouldShow: Boolean
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