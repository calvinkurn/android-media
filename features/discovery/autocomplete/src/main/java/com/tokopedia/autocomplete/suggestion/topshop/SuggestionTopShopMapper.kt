package com.tokopedia.autocomplete.suggestion.topshop

import com.tokopedia.autocomplete.suggestion.SuggestionItem
import com.tokopedia.autocomplete.suggestion.SuggestionTopShop
import com.tokopedia.autocomplete.suggestion.SuggestionTopShopProduct

fun SuggestionItem.convertSuggestionItemToTopShopWidgetVisitableList(position: Int, listTopShop: List<SuggestionTopShop>): SuggestionTopShopWidgetViewModel {
    return SuggestionTopShopWidgetViewModel(
            template = template,
            title = title,
            position = position,
            listSuggestionTopShopCard = listTopShop.convertSuggestionTopShopToTopShopCardViewModel()
    )
}

fun List<SuggestionTopShop>.convertSuggestionTopShopToTopShopCardViewModel(): List<SuggestionTopShopCardViewModel> {
    val list = mutableListOf<SuggestionTopShopCardViewModel>()
    for (item in this) {
        val model = SuggestionTopShopCardViewModel(
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
            products = item.topShopProducts.convertSuggestionTopShopProductToTopShopProductViewModel()
        )
        list.add(model)
    }
    return list
}

fun List<SuggestionTopShopProduct>.convertSuggestionTopShopProductToTopShopProductViewModel(): List<SuggestionTopShopCardViewModel.SuggestionTopShopProductViewModel> {
    val list = mutableListOf<SuggestionTopShopCardViewModel.SuggestionTopShopProductViewModel>()
    for (item in this) {
        val product = SuggestionTopShopCardViewModel.SuggestionTopShopProductViewModel(
            imageUrl = item.imageUrl
        )
        list.add(product)
    }
    return list
}