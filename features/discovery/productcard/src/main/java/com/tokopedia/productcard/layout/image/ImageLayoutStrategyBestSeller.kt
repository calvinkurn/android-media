package com.tokopedia.productcard.layout.image

import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.SQUARE_IMAGE_RATIO
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.video_widget.VideoPlayerView

internal class ImageLayoutStrategyBestSeller: ImageLayoutStrategy {

    override fun setupImageRatio(
        constraintLayoutProductCard: ConstraintLayout?,
        imageProduct: ImageView?,
        mediaAnchorProduct: Space?,
        videoProduct: VideoPlayerView?,
        productCardModel: ProductCardModel
    ) {
        com.tokopedia.productcard.utils.setupImageRatio(
            constraintLayoutProductCard,
            imageProduct,
            mediaAnchorProduct,
            videoProduct,
            SQUARE_IMAGE_RATIO,
        )
    }

    override fun getImageHeight(imageWidth: Int, productCardModel: ProductCardModel): Int =
        BEST_SELLER_IMAGE_SIZE_DP.toPx()

    override fun setImageSize(mediaAnchorProduct: Space?) {
        val layoutParams = mediaAnchorProduct?.layoutParams
        layoutParams?.width = BEST_SELLER_IMAGE_SIZE_DP.toPx()
        layoutParams?.height = BEST_SELLER_IMAGE_SIZE_DP.toPx()
        mediaAnchorProduct?.layoutParams = layoutParams
    }

    companion object {
        private const val BEST_SELLER_IMAGE_SIZE_DP = 108
    }
}
