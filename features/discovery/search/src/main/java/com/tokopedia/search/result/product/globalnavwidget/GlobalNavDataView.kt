package com.tokopedia.search.result.product.globalnavwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.domain.model.SearchProductModel
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
    val componentId: String = "",
    val trackingOption: String = "0",
    val dimension90: String = "",
    val info: String = "",
): Visitable<ProductListTypeFactory?>, ImpressHolder(),
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption.toIntOrZero(),
        keyword = keyword,
        valueName = title,
        componentId = componentId,
        applink = seeAllApplink,
        dimension90 = dimension90,
    ) {

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
        val componentId: String = "",
        val trackingOption: String = "0",
        val keyword: String = "",
        val dimension90: String = "",
    ) : SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption.toIntOrZero(),
        keyword = keyword,
        valueName = name,
        componentId = componentId,
        applink = applink,
        dimension90 = dimension90,
    )  {

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
        fun create(
            globalSearchNavigation: GlobalSearchNavigation,
            dimension90: String,
        ): GlobalNavDataView? {
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
                componentId = data.componentId,
                trackingOption = data.trackingOption,
                itemList = convertToViewModel(data, dimension90),
                dimension90 = dimension90,
                info = data.info
            )
        }

        private fun convertToViewModel(
            data: SearchProductModel.GlobalNavData,
            dimension90: String,
        ): List<Item> {
            val globalNavItems = data.globalNavItems

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
                    globalNavItem.componentId,
                    data.trackingOption,
                    data.keyword,
                    dimension90,
                )
            }
        }
    }
}