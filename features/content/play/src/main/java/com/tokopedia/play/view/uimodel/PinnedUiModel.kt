package com.tokopedia.play.view.uimodel

/**
 * Created by jegul on 16/12/19
 */
sealed class PinnedUiModel

data class PinnedMessageUiModel(
        val applink: String?,
        val partnerName: String,
        val title: String
) : PinnedUiModel()

data class PinnedProductUiModel(
        val partnerName: String,
        val title: String,
        val hasPromo: Boolean
) : PinnedUiModel()

object PinnedRemoveUiModel : PinnedUiModel()