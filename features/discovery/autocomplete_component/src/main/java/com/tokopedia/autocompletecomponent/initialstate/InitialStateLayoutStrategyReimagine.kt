package com.tokopedia.autocompletecomponent.initialstate

import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography

internal class InitialStateLayoutStrategyReimagine : InitialStateLayoutStrategy {
    override fun bindTitle(titleView: Typography, item: BaseItemInitialStateSearch) {
        titleView.shouldShowWithAction(item.title.isNotEmpty()) {
            titleView.text = MethodChecker.fromHtml(item.title).toString()
        }

        setTitleTypographyType(titleView)
    }

    private fun setTitleTypographyType(title: Typography) {
        title.setWeight(Typography.REGULAR)
    }

    override fun bindIconTitle(titleImageView: AppCompatImageView, autoCompleteIconTitleReimagine: AppCompatImageView, item: BaseItemInitialStateSearch) {
        autoCompleteIconTitleReimagine.shouldShowWithAction(item.iconTitle.isNotEmpty()) {
            autoCompleteIconTitleReimagine.loadImage(item.iconTitle)
        }
        titleImageView.hide()
    }
}
