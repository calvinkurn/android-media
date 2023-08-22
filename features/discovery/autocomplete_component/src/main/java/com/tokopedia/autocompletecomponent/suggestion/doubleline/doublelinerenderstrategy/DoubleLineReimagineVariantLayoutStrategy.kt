package com.tokopedia.autocompletecomponent.suggestion.doubleline.doublelinerenderstrategy

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifyprinciples.Typography

class DoubleLineReimagineVariantLayoutStrategy : DoubleLineLayoutStrategy {
    override fun bindIconTitle(badgeImageView: AppCompatImageView, item: BaseSuggestionDataView) {
        badgeImageView.shouldShowWithAction(item.iconTitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(badgeImageView, item.iconTitle)
        }
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
        action: () -> Unit
    ) {
        titleView.changeTypeToDisplay3()
        action()
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
            renderImageLayoutReimagine(iconImage, context)
            iconImage.loadImageCircle(item.data.imageUrl)
        } else {
            iconImage.loadImageRounded(item.data.imageUrl, contextResource.getDimension(R.dimen.autocomplete_product_suggestion_image_radius))
        }
    }

    private fun renderImageLayoutReimagine(iconImage: AppCompatImageView, context: Context){
        iconImage.background = ContextCompat.getDrawable(context, R.drawable.autocomplete_oval_image_border)
    }
}
