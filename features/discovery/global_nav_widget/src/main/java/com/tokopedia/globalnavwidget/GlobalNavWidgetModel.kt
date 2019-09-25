package com.tokopedia.globalnavwidget

data class GlobalNavWidgetModel(
        val title: String = "",
        val keyword: String = "",
        val navTemplate: String = "",
        val clickSeeAllApplink: String = "",
        val itemList: List<Item> = listOf()
) {

    data class Item(
            val name: String = "",
            val info: String = "",
            val imageUrl: String = "",
            val clickItemApplink: String = ""
    )
}