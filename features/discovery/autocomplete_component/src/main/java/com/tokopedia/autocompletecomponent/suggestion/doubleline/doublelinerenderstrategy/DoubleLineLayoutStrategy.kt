package com.tokopedia.autocompletecomponent.suggestion.doubleline.doublelinerenderstrategy

import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.unifyprinciples.Typography

interface DoubleLineLayoutStrategy {

    fun bindShopBadge(badgeImageView: AppCompatImageView, item: BaseSuggestionDataView)

    fun bindAdsLabel(adsTypography: Typography, dotImage: AppCompatImageView, item: BaseSuggestionDataView)

    fun bindTitle(titleView: Typography, action: () -> Unit)

    fun bindIconImage(iconImage: AppCompatImageView, item: SuggestionDoubleLineDataDataView)
}
