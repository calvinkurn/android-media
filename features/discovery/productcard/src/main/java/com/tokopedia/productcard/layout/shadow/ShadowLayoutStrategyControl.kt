package com.tokopedia.productcard.layout.shadow

import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.video_widget.VideoPlayerView

internal class ShadowLayoutStrategyControl : ShadowLayoutStrategy {
    override fun renderProductCardShadow(
        productCardModel: ProductCardModel,
        containerView: ViewGroup,
        cardView: CardUnify2?,
        isProductCardGrid: Boolean,
    ) {
        cardView ?: return
        cardView.cardType = CardUnify2.TYPE_SHADOW
        cardView.useCompatPadding = true

        containerView.setMargin(0, 0, 0, 0)

        cardView.setMargin(0, 0, 0, 0)

        if (isProductCardGrid) {
            cardView.findViewById<ImageView?>(R.id.productCardImage)
                ?.setPadding(0, 0, 0, 0)
            cardView.findViewById<VideoPlayerView?>(R.id.videoProduct)
                ?.setPadding(0, 0, 0, 0)
        }
    }
}
