package com.tokopedia.play.broadcaster.ui.model


/**
 * Created by mzennis on 15/06/20.
 */
data class ShareUiModel(
    val id: String,
    val title: String,
    val description: String,
    val slug: String,
    val imageUrl: String,
    val redirectUrl: String
)