package com.tokopedia.autocompletecomponent.initialstate.renderstrategy

import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography

class InitialStateReimagineLayoutStrategy : InitialStateRenderStrategy {
    override fun bindTitle(titleView: Typography, item: BaseItemInitialStateSearch) {
        titleView.shouldShowWithAction(item.title.isNotEmpty()) {
            titleView.text = MethodChecker.fromHtml(item.title).toString()
        }

        setTitleTypographyType(titleView)
    }

    private fun setTitleTypographyType(title: Typography) {
        title.setWeight(Typography.REGULAR)
    }

    override fun bindIconTitle(badgeImageView: AppCompatImageView, item: BaseItemInitialStateSearch) {
        badgeImageView.shouldShowWithAction(item.iconTitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(badgeImageView, item.iconTitle)
        }
    }
}
