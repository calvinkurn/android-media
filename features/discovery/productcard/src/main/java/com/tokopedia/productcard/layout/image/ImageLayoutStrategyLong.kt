package com.tokopedia.productcard.layout.image

import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.LONG_IMAGE_RATIO
import com.tokopedia.productcard.utils.SQUARE_IMAGE_RATIO
import com.tokopedia.productcard.utils.setupImageRatio
import com.tokopedia.video_widget.VideoPlayerView

internal class ImageLayoutStrategyLong : ImageLayoutStrategy {
    override fun setupImageRatio(
        constraintLayoutProductCard: ConstraintLayout?,
        imageProduct: ImageView?,
        mediaAnchorProduct: Space?,
        videoProduct: VideoPlayerView?,
        productCardModel: ProductCardModel,
    ) {
        val ratio = if (productCardModel.willShowBigImage()) {
            LONG_IMAGE_RATIO
        } else {
            SQUARE_IMAGE_RATIO
        }
        setupImageRatio(
            constraintLayoutProductCard,
            imageProduct,
            mediaAnchorProduct,
            videoProduct,
            ratio,
        )
    }

    override fun getImageHeight(
        imageWidth: Int,
        productCardModel: ProductCardModel,
    ): Int {
        return if (productCardModel.willShowBigImage()) {
            val ratioList = LONG_IMAGE_RATIO.split(":")
            val imageRatio: Float = try {
                ratioList[1].toFloat() / ratioList[0].toFloat()
            } catch (ignored: Exception) {
                1f
            }
            (imageWidth * imageRatio).toInt()
        } else {
            imageWidth
        }
    }
}
