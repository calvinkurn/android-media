package com.tokopedia.autocompletecomponent.initialstate

import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.unifyprinciples.Typography

internal interface InitialStateLayoutStrategy {

    fun bindTitle(titleView: Typography, item: BaseItemInitialStateSearch)

    fun bindIconTitle(titleImageView: AppCompatImageView, autoCompleteIconTitleReimagine: AppCompatImageView, item: BaseItemInitialStateSearch)
}
