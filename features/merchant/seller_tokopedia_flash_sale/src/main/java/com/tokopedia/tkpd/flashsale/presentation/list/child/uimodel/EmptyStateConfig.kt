package com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel

data class EmptyStateConfig(
    val imageUrl: String,
    val title: String,
    val description: String,
    val primaryCtaText: String = "",
    val primaryCtaAction: () -> Unit = {}
)
