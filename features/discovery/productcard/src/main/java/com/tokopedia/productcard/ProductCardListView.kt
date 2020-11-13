package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.utils.loadImageRounded
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.product_card_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_list_layout.view.*

class ProductCardListView: BaseCustomView, IProductCardView {

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.product_card_list_layout, this)
    }

    override fun setProductModel(productCardModel: ProductCardModel) {
        imageProduct?.loadImageRounded(productCardModel.productImageUrl)

        renderLabelCampaign(labelCampaignBackground, textViewLabelCampaign, productCardModel)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        renderProductCardContent(productCardModel)

        renderStockPercentage(productCardModel)
        renderStockLabel(productCardModel)

        imageThreeDots?.showWithCondition(productCardModel.hasThreeDots)

        buttonDeleteProduct?.showWithCondition(productCardModel.hasDeleteProductButton)

        buttonRemoveFromWishlist?.showWithCondition(productCardModel.hasRemoveFromWishlistButton)

        buttonAddToCart?.showWithCondition(productCardModel.hasAddToCartButton)

        setAddToCartButtonText(productCardModel)

        constraintLayoutProductCard?.post {
            imageThreeDots?.expandTouchArea(
                    getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                    getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16),
                    getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                    getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16)
            )
        }
    }

    fun setImageProductViewHintListener(impressHolder: ImpressHolder, viewHintListener: ViewHintListener) {
        imageProduct?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    fun setThreeDotsOnClickListener(threeDotsClickListener: (View) -> Unit) {
        imageThreeDots?.setOnClickListener(threeDotsClickListener)
    }

    fun setDeleteProductOnClickListener(deleteProductClickListener: (View) -> Unit) {
        buttonDeleteProduct?.setOnClickListener(deleteProductClickListener)
    }

    fun setRemoveWishlistOnClickListener(removeWishlistClickListener: (View) -> Unit) {
        buttonRemoveFromWishlist?.setOnClickListener(removeWishlistClickListener)
    }

    fun setAddToCartOnClickListener(addToCartClickListener: (View) -> Unit) {
        buttonAddToCart?.setOnClickListener(addToCartClickListener)
    }

    private fun View.renderStockPercentage(productCardModel: ProductCardModel) {
        progressBarStock?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
            progressBarStock.progress = productCardModel.stockBarPercentage
        }
    }

    private fun View.renderStockLabel(productCardModel: ProductCardModel) {
        textViewStockLabel?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
            textViewStockLabel.text = productCardModel.stockBarLabel
        }
    }

    private fun setAddToCartButtonText(productCardModel: ProductCardModel) {
        if (productCardModel.addToCardText.isNotEmpty())
            buttonAddToCart?.text = productCardModel.addToCardText
    }

    override fun getCardMaxElevation() = cardViewProductCard?.maxCardElevation ?: 0f

    override fun getCardRadius() = cardViewProductCard?.radius ?: 0f

    fun applyCarousel() {
        setCardHeightMatchParent()
        resizeImageProductSize()
    }

    private fun setCardHeightMatchParent() {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        cardViewProductCard?.layoutParams = layoutParams
    }

    private fun resizeImageProductSize() {
        val layoutParams = imageProduct?.layoutParams
        layoutParams?.width = getDimensionPixelSize(R.dimen.product_card_carousel_list_image_size)
        layoutParams?.height = getDimensionPixelSize(R.dimen.product_card_carousel_list_image_size)
        imageProduct?.layoutParams = layoutParams
    }

    override fun recycle() {
        imageProduct?.glideClear()
        imageFreeOngkirPromo?.glideClear()
    }

    override fun getThreeDotsButton(): View? = imageThreeDots

    /**
     * Special cases for specific pages
     * */
    fun wishlistPage_hideCTAButton(isVisible: Boolean) {
        buttonAddToCart?.showWithCondition(isVisible)
        buttonRemoveFromWishlist?.showWithCondition(isVisible)
        progressBarStock?.showWithCondition(!isVisible)
        textViewStockLabel?.showWithCondition(!isVisible)
    }

    fun wishlistPage_enableButtonAddToCart(){
        buttonAddToCart?.isEnabled = true
        buttonAddToCart?.buttonVariant = UnifyButton.Variant.GHOST
        buttonAddToCart?.text = context.getString(R.string.product_card_text_add_to_cart_list)
    }

    fun wishlistPage_disableButtonAddToCart(){
        buttonAddToCart?.isEnabled = false
        buttonAddToCart?.text = context.getString(R.string.product_card_text_add_to_cart_list)
    }

    fun wishlistPage_setOutOfStock(){
        buttonAddToCart?.isEnabled = false
        buttonAddToCart?.buttonVariant = UnifyButton.Variant.FILLED
        buttonAddToCart?.text = context.getString(R.string.product_card_out_of_stock)
    }
}
