package com.tokopedia.autocompletecomponent.suggestion.doubleline

import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToDoubleLineVisitableList(
    searchTerm: String,
    position: Int,
    dimension90: String
): SuggestionDoubleLineDataDataView {
    return SuggestionDoubleLineDataDataView(
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
            trackingCode = tracking.code,
            trackingOption = trackingOption,
            componentId = componentId,
        )
    )
}