package com.tokopedia.home_component.widget.tab

data class MegaTabItem(
    val title: String = "",
    val imageUrl: String = ""
) {

    fun hasContentEmpty() = title.isEmpty() && imageUrl.isEmpty()
}
