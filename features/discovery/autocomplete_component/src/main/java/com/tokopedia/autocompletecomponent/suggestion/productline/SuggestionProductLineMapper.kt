package com.tokopedia.autocompletecomponent.suggestion.productline

import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem

fun SuggestionItem.convertToSuggestionProductLineDataView(
    searchTerm: String,
    position: Int,
    dimension90: String
): SuggestionProductLineDataDataView {
    return SuggestionProductLineDataDataView(
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
            trackingOption = trackingOption,
        )
    )
}