package com.tokopedia.autocompletecomponent.initialstate.renderstrategy

import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.unifyprinciples.Typography

interface InitialStateRenderStrategy {

    fun bindTitle(titleView: Typography, item: BaseItemInitialStateSearch)

    fun bindShopBadge(badgeImageView: AppCompatImageView, item: BaseItemInitialStateSearch)
}
