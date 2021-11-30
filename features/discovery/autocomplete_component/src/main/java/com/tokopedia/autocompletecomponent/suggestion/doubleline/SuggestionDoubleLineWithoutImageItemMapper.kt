package com.tokopedia.autocompletecomponent.suggestion.doubleline

import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToDoubleLineWithoutImageVisitableList(
    searchTerm: String,
    position: Int,
    dimension90: String
): SuggestionDoubleLineWithoutImageDataDataView {
    return SuggestionDoubleLineWithoutImageDataDataView(
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
            label = label,
            labelType = labelType,
            urlTracker = urlTracker,
            searchTerm = searchTerm,
            position = position,
            dimension90 = dimension90,
            trackingOption = trackingOption,
            componentId = componentId,
        )
    )
}