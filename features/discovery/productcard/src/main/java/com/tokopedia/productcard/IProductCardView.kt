package com.tokopedia.productcard

import android.view.View
import android.widget.ImageView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.video_widget.VideoPlayerController

interface IProductCardView {

    val additionalMarginStart: Int

    fun setProductModel(productCardModel: ProductCardModel)

    fun getCardMaxElevation(): Float

    fun getCardRadius(): Float

    fun recycle()

    fun getThreeDotsButton(): View?

    fun getNotifyMeButton(): UnifyButton?

    fun getShopBadgeView(): View?

    fun getProductImageView(): ImageView?

    fun getVideoPlayerController(): VideoPlayerController
}
