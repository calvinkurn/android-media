package com.tokopedia.search.result.product.globalnavwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.domain.model.SearchProductModel.GlobalNavItem
import com.tokopedia.search.result.domain.model.SearchProductModel.GlobalSearchNavigation
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

@Suppress("LongParameterList")
class GlobalNavDataView(
    val source: String = "",
    val title: String = "",
    val keyword: String = "",
    val navTemplate: String = "",
    val background: String = "",
    val seeAllApplink: String = "",
    val seeAllUrl: String = "",
    val isShowTopAds: Boolean = false,
    val itemList: List<Item> = listOf(),
): Visitable<ProductListTypeFactory?>, ImpressHolder() {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    @Suppress("LongParameterList")
    class Item(
        val categoryName: String = "",
        val name: String = "",
        val info: String = "",
        val imageUrl: String = "",
        val applink: String = "",
        val url: String = "",
        val subtitle: String = "",
        val strikethrough: String = "",
        val backgroundUrl: String = "",
        val logoUrl: String = "",
        val position: Int = 0,
    ) {

        fun getGlobalNavItemAsObjectDataLayer(creativeName: String?): Any {
            return DataLayer.mapOf(
                    "id", name,
                    "name", "/search result - widget",
                    "creative", creativeName,
                    "position", position.toString()
            )
        }
    }

    companion object {
        fun create(globalSearchNavigation: GlobalSearchNavigation): GlobalNavDataView? {
            val data = globalSearchNavigation.data
            if (data.globalNavItems.isEmpty()) return null

            return GlobalNavDataView(
                source = data.source,
                title = data.title,
                keyword = data.keyword,
                navTemplate = data.navTemplate,
                background = data.background,
                seeAllApplink = data.seeAllApplink,
                seeAllUrl = data.seeAllUrl,
                isShowTopAds = data.isShowTopAds,
                itemList = convertToViewModel(data.globalNavItems)
            )
        }

        private fun convertToViewModel(globalNavItems: List<GlobalNavItem>): List<Item> {
            return globalNavItems.mapIndexed { index, globalNavItem ->
                val position = index + 1

                Item(
                    globalNavItem.categoryName,
                    globalNavItem.name,
                    globalNavItem.info,
                    globalNavItem.imageUrl,
                    globalNavItem.applink,
                    globalNavItem.url,
                    globalNavItem.subtitle,
                    globalNavItem.strikethrough,
                    globalNavItem.backgroundUrl,
                    globalNavItem.logoUrl,
                    position,
                )
            }
        }
    }
}