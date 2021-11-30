package com.tokopedia.autocompletecomponent.suggestion.singleline

import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToSingleLineVisitableList(
    searchTerm: String,
    position: Int,
    dimension90: String,
): SuggestionSingleLineDataDataView {
    return SuggestionSingleLineDataDataView(
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
            dimension90 = dimension90,
            trackingOption = trackingOption,
            componentId = componentId,
        ),
    )
}