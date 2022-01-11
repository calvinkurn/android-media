package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionChildItem
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

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