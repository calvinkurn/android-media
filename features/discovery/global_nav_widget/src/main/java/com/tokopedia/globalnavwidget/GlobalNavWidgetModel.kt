package com.tokopedia.globalnavwidget

data class GlobalNavWidgetModel(
        val source: String = "",
        val keyword: String = "",
        val title: String = "",
        val navTemplate: String = "",
        val background: String = "",
        val clickSeeAllApplink: String = "",
        val clickSeeAllUrl: String = "",
        val itemList: List<Item> = listOf()
) {

    data class Item(
            val categoryName: String = "",
            val name: String = "",
            val info: String = "",
            val imageUrl: String = "",
            val clickItemApplink: String = "",
            val clickItemUrl: String = "",
            val subtitle: String = "",
            val strikethrough: String = "",
            val backgroundUrl: String = "",
            val logoUrl: String = "",
            val position: Int = 0
    )
}