package com.tokopedia.home.beranda.domain.model

class TargetedTickerUiModel(
    val id: Int,
    val title: String,
    val content: String,
    val type: Int,
    val priority: Int,
    val action: Action
) {

    data class Action(
        val label: String,
        val type: String,
        val appLink: String,
        val url: String,
    )
}
