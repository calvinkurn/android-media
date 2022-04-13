package com.tokopedia.imagepicker_insta.common.ui.model

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */
data class FeedAccountUiModel(
    val name: String,
    val iconUrl: String,
    val type: Type,
) {
    enum class Type {
        BUYER, SELLER,
    }
}