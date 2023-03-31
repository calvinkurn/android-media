package com.tokopedia.productcard.layout.shadow

import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.productcard.R
import com.tokopedia.video_widget.VideoPlayerView

internal class ShadowLayoutStrategyNone : ShadowLayoutStrategy {
    override fun renderProductCardShadow(productCardLayout: CardUnify2?) {
        val cardLayout = productCardLayout ?: return
        cardLayout.cardType = CardUnify2.TYPE_BORDER
        cardLayout.useCompatPadding = false
        val margin = CARD_MARGIN.toPx()
        cardLayout.setMargin(margin, margin, margin, margin)
        val mediaMargin = MEDIA_MARGIN.toPx().toInt()
        productCardLayout.findViewById<ImageView?>(R.id.productCardImage)
            ?.setPadding(mediaMargin, mediaMargin, mediaMargin, 0)
        productCardLayout.findViewById<VideoPlayerView?>(R.id.videoProduct)
            ?.setPadding(mediaMargin, mediaMargin, mediaMargin, 0)
    }

    companion object {
        const val CARD_MARGIN = 4
        const val MEDIA_MARGIN = 1.25f
    }
}
