package com.tokopedia.play.ui.toolbar.model


/**
 * Created by mzennis on 2019-12-11.
 */
data class TitleToolbar(
        val partnerId: Long,
        val partnerName: String,
        val partnerType: PartnerType,
        val isAlreadyFavorite: Boolean = false
)