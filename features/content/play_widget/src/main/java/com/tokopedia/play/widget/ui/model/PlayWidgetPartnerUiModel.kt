package com.tokopedia.play.widget.ui.model


/**
 * Created by mzennis on 05/10/20.
 */
data class PlayWidgetPartnerUiModel(
    val id: String,
    val name: String,
    val type: PartnerType,
    val avatarUrl: String,
    val badgeUrl: String,
    val appLink: String,
)
