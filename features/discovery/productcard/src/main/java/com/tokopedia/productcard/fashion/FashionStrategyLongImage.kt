package com.tokopedia.productcard.fashion

import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.productcard.utils.LONG_IMAGE_RATIO
import com.tokopedia.video_widget.VideoPlayerView

internal class FashionStrategyLongImage: FashionStrategyReposition() {
    override fun setupImageRatio(
        constraintLayoutProductCard: ConstraintLayout?,
        imageProduct: ImageView?,
        mediaAnchorProduct: Space?,
        videoProduct: VideoPlayerView?,
    ) {
        com.tokopedia.productcard.utils.setupImageRatio(
            constraintLayoutProductCard,
            imageProduct,
            mediaAnchorProduct,
            videoProduct,
            LONG_IMAGE_RATIO,
        )
    }

    override fun getImageHeight(imageWidth: Int): Int {
        val ratioList = LONG_IMAGE_RATIO.split(":")
        val imageRatio: Float = try {
            ratioList[1].toFloat() / ratioList[0].toFloat()
        } catch (e: Exception) {
            1f
        }

        return (imageWidth * imageRatio).toInt()
    }
}