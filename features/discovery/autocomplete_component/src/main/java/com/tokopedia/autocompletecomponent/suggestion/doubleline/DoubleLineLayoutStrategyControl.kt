package com.tokopedia.autocompletecomponent.suggestion.doubleline

import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifyprinciples.Typography

internal class DoubleLineLayoutStrategyControl : DoubleLineLayoutStrategy {

    override fun bindIconTitle(titleImageView: AppCompatImageView, autoCompleteIconTitleReimagine: AppCompatImageView, item: BaseSuggestionDataView) {
        titleImageView.shouldShowWithAction(item.iconTitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(titleImageView, item.iconTitle)
        }
        autoCompleteIconTitleReimagine.hide()
    }

    override fun bindAdsLabel(
        adsTypography: Typography,
        dotImage: AppCompatImageView,
        item: BaseSuggestionDataView
    ) {
        val isAds = item.shopAdsDataView != null
        adsTypography.showWithCondition(isAds)
        dotImage.hide()
    }

    override fun bindTitle(
        titleView: Typography,
    ) {
        titleView.changeTypeToDisplay2()
    }

    private fun Typography.changeTypeToDisplay2() {
        this.setType(Typography.DISPLAY_2)
    }

    override fun bindIconImage(
        iconImage: AppCompatImageView,
        item: SuggestionDoubleLineDataDataView
    ) {
        val contextResource = iconImage.context.resources ?: return
        if (item.data.isCircleImage()) {
            renderBackgroundImageLayout(iconImage)
            iconImage.loadImageCircle(item.data.imageUrl)
        } else {
            iconImage.loadImageRounded(
                item.data.imageUrl,
                contextResource.getDimension(R.dimen.autocomplete_product_suggestion_image_radius)
            )
        }
    }

    private fun renderBackgroundImageLayout(iconImage: AppCompatImageView) {
        iconImage.background = null
    }
}
