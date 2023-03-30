package com.tokopedia.productcard.layout.image

import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.video_widget.VideoPlayerView

interface ImageLayoutStrategy {
    fun setupImageRatio(
        constraintLayoutProductCard: ConstraintLayout?,
        imageProduct: ImageView?,
        mediaAnchorProduct: Space?,
        videoProduct: VideoPlayerView?,
        productCardModel: ProductCardModel,
    )

    fun getImageHeight(imageWidth: Int, productCardModel: ProductCardModel): Int
}
