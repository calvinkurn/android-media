package com.tokopedia.autocompletecomponent.suggestion.chips

import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionChildItem
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToSuggestionChipWidgetDataView(
    searchTerm: String,
    position: Int,
    dimension90: String
): SuggestionChipWidgetDataView {
    return SuggestionChipWidgetDataView(
        data = BaseSuggestionDataView(
            template = template,
            type = type,
            applink = applink,
            url = url,
            title = title,
            subtitle = subtitle,
            iconTitle = iconTitle,
            iconSubtitle = iconSubtitle,
            shortcutUrl = shortcutUrl,
            shortcutImage = shortcutImage,
            imageUrl = imageUrl,
            urlTracker = urlTracker,
            searchTerm = searchTerm,
            position = position,
            trackingCode = tracking.code,
            discountPercentage = discountPercentage,
            originalPrice = originalPrice,
            dimension90 = dimension90,
            childItems = suggestionChildItems.convertToChildItems(searchTerm, dimension90),
            trackingOption = trackingOption,
            componentId = componentId,
        )
    )
}

private fun List<SuggestionChildItem>.convertToChildItems(
    searchTerm: String,
    dimension90: String,
): List<BaseSuggestionDataView.ChildItem> {
    return mapIndexed { index, item ->
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
}