package com.tokopedia.productcard.experiments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.video_widget.VideoPlayerController

internal interface IReimagineGridViewStrategy {
    fun additionalMarginStart(): Int = 0
}

internal interface IGridViewStrategy {
    fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) { }
    fun setSimilarProductClickListener(similarProductClickListener: (View) -> Unit) { }
    fun setAddVariantClickListener(addVariantClickListener: (View) -> Unit) { }
    fun setNotifyMeOnClickListener(notifyMeClickListener: (View) -> Unit) { }
    fun setThreeDotsWishlistOnClickListener(threeDotsClickListener: (View) -> Unit) { }
    fun setAddToCartWishlistOnClickListener(addToCartWishlistClickListener: (View) -> Unit) { }
    fun setSeeSimilarProductWishlistOnClickListener(seeSimilarProductWishlistClickListener: (View) -> Unit) { }
    fun setDeleteProductOnClickListener(deleteProductClickListener: (View) -> Unit) { }
    fun setRemoveWishlistOnClickListener(removeWishlistClickListener: (View) -> Unit) { }
    fun setSeeOtherProductOnClickListener(seeOtherProductOnClickListener: (View) -> Unit) { }
    fun getCardMaxElevation(): Float = 0f
    fun getCardRadius(): Float = 0f
    fun applyCarousel() { }
    fun getThreeDotsButton(): View? = null
    fun getNotifyMeButton(): UnifyButton? = null
    fun getShopBadgeView(): View? = null
    fun getProductImageView(): ImageView? = null
}

internal interface ProductCardStrategy:
    IGridViewStrategy,
    IReimagineGridViewStrategy {

    fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int? = null)

    fun setProductModel(productCardModel: ProductCardModel)

    fun recycle()

    fun setImageProductViewHintListener(
        impressHolder: ImpressHolder,
        viewHintListener: ViewHintListener
    )
    fun setOnClickListener(l: View.OnClickListener?)
    fun setOnLongClickListener(l: View.OnLongClickListener?) { }
    fun setThreeDotsOnClickListener(l: View.OnClickListener?) { }
    fun setAddToCartOnClickListener(l: View.OnClickListener?) { }
    fun getVideoPlayerController(): VideoPlayerController = VideoPlayerController.empty()
}
