package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class SingleGlobalNavViewModel(
        val source: String = "",
        val keyword: String = "",
        val title: String = "",
        val navTemplate: String = "",
        val background: String = "",
        val isShowTopAds: Boolean = false,
        val item: Item = Item()
): Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Item(
            val categoryName: String = "",
            val name: String = "",
            val info: String = "",
            val imageUrl: String = "",
            val clickItemApplink: String = "",
            val clickItemUrl: String = "",
            val subtitle: String = "",
            val logoUrl: String = ""
    ) {

        fun getGlobalNavItemAsObjectDataLayer(creativeName: String?): Any {
            return DataLayer.mapOf(
                    "id", name,
                    "name", "/search result - widget",
                    "creative", creativeName,
                    "position", 1
            )
        }
    }
}