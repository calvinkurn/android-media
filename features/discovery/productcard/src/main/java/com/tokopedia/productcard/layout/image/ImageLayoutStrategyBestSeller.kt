package com.tokopedia.productcard.layout.image

import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.productcard.ProductCardModel
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
        IMAGE_SIZE_DP.toPx()

    override fun setImageSizeListView(mediaAnchorProduct: Space?) {
        val layoutParams = mediaAnchorProduct?.layoutParams
        layoutParams?.width = IMAGE_SIZE_DP.toPx()
        layoutParams?.height = IMAGE_SIZE_DP.toPx()
        mediaAnchorProduct?.layoutParams = layoutParams
    }

    override fun imageCornerRadius(): Float = IMAGE_RADIUS_DP.toPx().toFloat()

    companion object {
        private const val IMAGE_SIZE_DP = 108
        private const val IMAGE_RADIUS_DP = 12
    }
}
