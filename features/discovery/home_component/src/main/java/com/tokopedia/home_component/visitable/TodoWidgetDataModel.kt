package com.tokopedia.home_component.visitable

/**
 * Created by frenzel
 */
data class TodoWidgetDataModel(
    val id: Long = 0L,
    val title: String = "",
    val dataSource: String = "",
    val dueDate: String = "",
    val contextInfo: String = "",
    val price: String = "",
    val slashedPrice: String = "",
    val discountPercentage: String = "",
    val cardApplink: String = "",
    val ctaType: String = "",
    val ctaMode: String = "",
    val ctaText: String = "",
    val ctaApplink: String = "",
    val imageUrl: String = "",
    val feParam: String = ""
)
