package com.tokopedia.autocompletecomponent.suggestion.doubleline

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifyprinciples.Typography

class DoubleLineLayoutStrategyReimagine : DoubleLineLayoutStrategy {
    override fun bindIconTitle(titleImageView: AppCompatImageView, autoCompleteIconTitleReimagine: AppCompatImageView, item: BaseSuggestionDataView) {
        autoCompleteIconTitleReimagine.shouldShowWithAction(item.iconTitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(autoCompleteIconTitleReimagine, item.iconTitle)
        }
        titleImageView.hide()
    }

    override fun bindAdsLabel(
        adsTypography: Typography,
        dotImage: AppCompatImageView,
        item: BaseSuggestionDataView
    ) {
        val isAds = item.shopAdsDataView != null

        adsTypography.showWithCondition(isAds)
        dotImage.showWithCondition(isAds)
    }

    override fun bindTitle(
        titleView: Typography,
    ) {
        titleView.changeTypeToDisplay3()
    }

    private fun Typography.changeTypeToDisplay3() {
        this.setType(Typography.DISPLAY_3)
    }

    override fun bindIconImage(
        iconImage: AppCompatImageView,
        item: SuggestionDoubleLineDataDataView
    ) {
        val context = iconImage.context ?: return
        val contextResource = iconImage.context.resources ?: return
        if (item.data.isCircleImage()) {
            renderBackgroundImageLayout(iconImage, context)
            iconImage.loadImageCircle(item.data.imageUrl)
        } else {
            iconImage.loadImageRounded(item.data.imageUrl, contextResource.getDimension(R.dimen.autocomplete_product_suggestion_image_radius))
        }
    }

    private fun renderBackgroundImageLayout(iconImage: AppCompatImageView, context: Context) {
        iconImage.background = ContextCompat.getDrawable(context, R.drawable.autocomplete_oval_image_border)
    }
}
