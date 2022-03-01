package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionChildItem
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocompletecomponent.suggestion.doubleline.ShopAdsDataView
import com.tokopedia.topads.sdk.domain.model.CpmData

fun SuggestionItem.convertToBaseSuggestion(
    searchTerm: String,
    position: Int,
    dimension90: String,
): BaseSuggestionDataView =
    BaseSuggestionDataView(
        template = template,
        type = type,
        applink = applink,
        url = url,
        title = title,
        subtitle = subtitle,
        iconTitle = iconTitle,
        iconSubtitle = iconSubtitle,
        shortcutImage = shortcutImage,
        imageUrl = imageUrl,
        label = label,
        labelType = labelType,
        urlTracker = urlTracker,
        trackingCode = tracking.code,
        discountPercentage = discountPercentage,
        originalPrice = originalPrice,
        trackingOption = trackingOption,
        componentId = componentId,
        searchTerm = searchTerm,
        position = position,
        dimension90 = dimension90,
        childItems = suggestionChildItems.convertToChildItems(searchTerm, dimension90),
    )

private fun List<SuggestionChildItem>.convertToChildItems(
    searchTerm: String,
    dimension90: String,
): List<BaseSuggestionDataView.ChildItem> =
    mapIndexed { index, item ->
        BaseSuggestionDataView.ChildItem(
            template = item.template,
            type = item.type,
            applink = item.applink,
            url = item.url,
            title = item.title,
            searchTerm = searchTerm,
            dimension90 = dimension90,
            position = index + 1
        )
    }

fun SuggestionItem.convertToBaseSuggestionShopAds(
    searchTerm: String,
    position: Int,
    dimension90: String,
    cpmData: CpmData,
): BaseSuggestionDataView =
    BaseSuggestionDataView(
        template = template,
        type = TYPE_SHOP,
        applink = cpmData.applinks,
        title = cpmData.cpm.name,
        iconTitle = cpmData.cpm.badges.firstOrNull()?.imageUrl ?: "",
        imageUrl = cpmData.cpm.cpmImage.fullEcs,
        position = position,
        dimension90 = dimension90,
        searchTerm = searchTerm,
        componentId = componentId,
        trackingOption = trackingOption,
        shopAdsDataView = ShopAdsDataView(
            clickUrl = cpmData.adClickUrl,
            impressionUrl = cpmData.cpm.cpmImage.fullUrl,
            imageUrl = cpmData.cpm.cpmImage.fullEcs,
        )
    )