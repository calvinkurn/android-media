package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.experiments.ProductCardStrategy
import com.tokopedia.productcard.experiments.ProductCardStrategyFactory
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.video_widget.VideoPlayerController
import com.tokopedia.productcard.reimagine.ProductCardModel as ReimagineProductCardModel

class ProductCardGridView : ProductConstraintLayout, IProductCardView {

    private val productCardStrategy: ProductCardStrategy

    override val additionalMarginStart: Int
        get() = productCardStrategy.additionalMarginStart()

    constructor(context: Context) : super(context) {
        productCardStrategy = ProductCardStrategyFactory.gridStrategy(this)

        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        productCardStrategy = ProductCardStrategyFactory.gridStrategy(this, attrs)

        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        productCardStrategy = ProductCardStrategyFactory.gridStrategy(this, attrs)

        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int? = null) {
        productCardStrategy.init(context, attrs, defStyleAttr)
    }

    override fun setProductModel(productCardModel: ProductCardModel) {
        productCardStrategy.setProductModel(productCardModel)
    }

    fun setImageProductViewHintListener(impressHolder: ImpressHolder, viewHintListener: ViewHintListener) {
        productCardStrategy.setImageProductViewHintListener(impressHolder, viewHintListener)
    }

    fun setThreeDotsOnClickListener(threeDotsClickListener: (View) -> Unit) {
        productCardStrategy.setThreeDotsOnClickListener(threeDotsClickListener)
    }

    fun setAddToCartOnClickListener(addToCartClickListener: (View) -> Unit) {
        productCardStrategy.setAddToCartOnClickListener(addToCartClickListener)
    }

    fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) {
        productCardStrategy.setAddToCartNonVariantClickListener(addToCartNonVariantClickListener)
    }

    fun setSimilarProductClickListener(similarProductClickListener: (View) -> Unit) {
        productCardStrategy.setSimilarProductClickListener(similarProductClickListener)
    }

    fun setAddVariantClickListener(addVariantClickListener: (View) -> Unit) {
        productCardStrategy.setAddVariantClickListener(addVariantClickListener)
    }

    fun setNotifyMeOnClickListener(notifyMeClickListener: (View) -> Unit) {
        productCardStrategy.setNotifyMeOnClickListener(notifyMeClickListener)
    }

    fun setThreeDotsWishlistOnClickListener(threeDotsClickListener: (View) -> Unit) {
        productCardStrategy.setThreeDotsWishlistOnClickListener(threeDotsClickListener)
    }

    fun setAddToCartWishlistOnClickListener(addToCartWishlistClickListener: (View) -> Unit) {
        productCardStrategy.setAddToCartWishlistOnClickListener(addToCartWishlistClickListener)
    }

    fun setSeeSimilarProductWishlistOnClickListener(seeSimilarProductWishlistClickListener: (View) -> Unit) {
        productCardStrategy.setSeeSimilarProductWishlistOnClickListener(seeSimilarProductWishlistClickListener)
    }

    fun setSeeOtherProductOnClickListener(seeOtherProductOnClickListener: (View) -> Unit) {
        productCardStrategy.setSeeOtherProductOnClickListener(seeOtherProductOnClickListener)
    }

    override fun getCardMaxElevation() = productCardStrategy.getCardMaxElevation()

    override fun getCardRadius() = productCardStrategy.getCardRadius()

    fun applyCarousel() {
        productCardStrategy.applyCarousel()
    }

    override fun recycle() {
        productCardStrategy.recycle()
    }

    override fun getThreeDotsButton(): View? = productCardStrategy.getThreeDotsButton()

    override fun getNotifyMeButton(): UnifyButton? = productCardStrategy.getNotifyMeButton()

    override fun getShopBadgeView(): View? = productCardStrategy.getShopBadgeView()

    override fun getProductImageView(): ImageView? {
        return productCardStrategy.getProductImageView()
    }

    override fun getVideoPlayerController(): VideoPlayerController {
        return productCardStrategy.getVideoPlayerController()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        productCardStrategy.setOnClickListener(l)
    }

    fun setOnClickListener(l: ProductCardClickListener) {
        productCardStrategy.setOnClickListener(l)
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        super.setOnLongClickListener(l)
        productCardStrategy.setOnLongClickListener(l)
    }

    fun setGenericCtaButtonOnClickListener(onClickListener: OnClickListener) {
        productCardStrategy.setGenericCtaButtonOnClickListener(onClickListener)
    }

    fun setGenericCtaSecondaryButtonOnClickListener(onClickListener: OnClickListener) {
        productCardStrategy.setGenericCtaSecondaryButtonOnClickListener(onClickListener)
    }


    /**
     * Used to re-render the generic cta button.
     * Not recommended for common use. Please copy model and rerender the whole card if possible.
     * Only created from request by Discovery team to prevent flashing UI when updating product card.
     * Please update the adapter's list productCardModel too to prevent wrong recycling render
     * if you use RecyclerView.
     * @param productCardModel ProductCardModel(Old) with updated CTA data
     */
    fun reRenderGenericCtaButton(productCardModel: ProductCardModel) {
        val reimagineModel = ReimagineProductCardModel.from(productCardModel)
        productCardStrategy.reRenderGenericCtaButton(reimagineModel)
    }
}
