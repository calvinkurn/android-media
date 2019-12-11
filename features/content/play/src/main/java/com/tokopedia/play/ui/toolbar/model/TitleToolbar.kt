package com.tokopedia.play.ui.toolbar.model


/**
 * Created by mzennis on 2019-12-11.
 */
data class TitleToolbar(
        val partnerId: String = "",
        val partnerName: String = "",
        val partnerType: String = "",
        val isAlreadyFavorite: Boolean = false
)