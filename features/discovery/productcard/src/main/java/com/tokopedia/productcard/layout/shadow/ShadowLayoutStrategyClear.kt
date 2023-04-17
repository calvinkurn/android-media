package com.tokopedia.productcard.layout.shadow

import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.CARD_MARGIN
import com.tokopedia.productcard.utils.MEDIA_MARGIN
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.video_widget.VideoPlayerView

internal class ShadowLayoutStrategyClear: ShadowLayoutStrategy {

    override fun renderProductCardShadow(
        productCardModel: ProductCardModel,
        containerView: ViewGroup,
        cardView: CardUnify2?,
        isProductCardGrid: Boolean,
    ) {
        cardView ?: return
        cardView.cardType = CardUnify2.TYPE_CLEAR
        cardView.useCompatPadding = false

        val margin = CARD_MARGIN.toPx()
        containerView.setMargin(margin, margin, margin, margin)

        if (isProductCardGrid) {
            cardView.setMargin(cardViewLeftMargin(productCardModel), 0, 0, 0)

            val mediaMargin = MEDIA_MARGIN.toPx().toInt()
            cardView.findViewById<ImageView?>(R.id.productCardImage)
                ?.setPadding(mediaMargin, mediaMargin, mediaMargin, 0)
            cardView.findViewById<VideoPlayerView?>(R.id.videoProduct)
                ?.setPadding(mediaMargin, mediaMargin, mediaMargin, 0)
        }
    }

    private fun cardViewLeftMargin(productCardModel: ProductCardModel) =
        (if (productCardModel.showRibbon) 3 else 0).toPx()
}
