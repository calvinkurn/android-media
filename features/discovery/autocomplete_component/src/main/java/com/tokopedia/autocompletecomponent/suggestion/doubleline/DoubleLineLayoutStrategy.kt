package com.tokopedia.autocompletecomponent.suggestion.doubleline

import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.unifyprinciples.Typography

internal interface DoubleLineLayoutStrategy {

    fun bindIconTitle(titleImageView: AppCompatImageView, autoCompleteIconTitleReimagine: AppCompatImageView, item: BaseSuggestionDataView)

    fun bindAdsLabel(adsTypography: Typography, dotImage: AppCompatImageView, item: BaseSuggestionDataView)

    fun bindTitle(titleView: Typography)

    fun bindIconImage(iconImage: AppCompatImageView, item: SuggestionDoubleLineDataDataView)
}
