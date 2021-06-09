package com.tokopedia.autocomplete.suggestion.topshop

import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionTopShop
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionTopShopProduct

fun SuggestionItem.convertToTopShopWidgetVisitableList(position: Int, listTopShop: List<SuggestionTopShop>): SuggestionTopShopWidgetDataView {
    return SuggestionTopShopWidgetDataView(
            template = template,
            title = title,
            position = position,
            listSuggestionTopShopCardData = listTopShop.convertToTopShopCardDataView()
    )
}

fun List<SuggestionTopShop>.convertToTopShopCardDataView(): List<SuggestionTopShopCardDataView> {
    val list = mutableListOf<SuggestionTopShopCardDataView>()
    for (item in this) {
        val model = SuggestionTopShopCardDataView(
            type = item.type,
            id = item.id,
            applink = item.applink,
            url = item.url,
            title = item.title,
            subtitle = item.subtitle,
            iconTitle = item.iconTitle,
            iconSubtitle = item.iconSubtitle,
            urlTracker = item.urlTracker,
            imageUrl = item.imageUrl,
            productData = item.topShopProducts.convertToTopShopProductDataView()
        )
        list.add(model)
    }
    return list
}

fun List<SuggestionTopShopProduct>.convertToTopShopProductDataView(): List<SuggestionTopShopCardDataView.SuggestionTopShopProductDataView> {
    val list = mutableListOf<SuggestionTopShopCardDataView.SuggestionTopShopProductDataView>()
    for (item in this) {
        val product = SuggestionTopShopCardDataView.SuggestionTopShopProductDataView(
            imageUrl = item.imageUrl
        )
        list.add(product)
    }
    return list
}