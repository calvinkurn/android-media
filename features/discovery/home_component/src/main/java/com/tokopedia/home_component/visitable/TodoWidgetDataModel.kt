package com.tokopedia.home_component.visitable

/**
 * Created by frenzel
 */
data class TodoWidgetDataModel(
    val id: Long = 0L,
    val title: String = "",
    val dueDate: String = "",
    val description: String = "",
    val price: String = "",
    val slashedPrice: String = "",
    val cardApplink: String = "",
    val ctaApplink: String = "",
    val imageUrl: String = "",
    val recommendationType: String = "",
    val buType: String = "",
)
