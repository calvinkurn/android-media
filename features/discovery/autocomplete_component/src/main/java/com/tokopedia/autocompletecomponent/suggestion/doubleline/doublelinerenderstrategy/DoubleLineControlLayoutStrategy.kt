package com.tokopedia.autocompletecomponent.suggestion.doubleline.doublelinerenderstrategy

import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.doubleline.SuggestionDoubleLineDataDataView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.unifyprinciples.Typography

class DoubleLineControlLayoutStrategy : DoubleLineLayoutStrategy {

    override fun bindShopBadge(badgeImageView: AppCompatImageView, item: BaseSuggestionDataView) {
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
    }

    override fun bindTitle(
        titleView: Typography,
        action: () -> Unit
    ) {
        action()
    }

    override fun bindIconImage(
        iconImage: AppCompatImageView,
        item: SuggestionDoubleLineDataDataView
    ) {
        val contextResource = iconImage.context.resources ?: return
        if (item.data.isCircleImage()) {
            iconImage.loadImageCircle(item.data.imageUrl)
        } else {
            iconImage.loadImageRounded(
                item.data.imageUrl,
                contextResource.getDimension(R.dimen.autocomplete_product_suggestion_image_radius)
            )
        }
    }
}
