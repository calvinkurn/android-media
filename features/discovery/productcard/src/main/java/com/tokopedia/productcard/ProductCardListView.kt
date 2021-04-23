package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel.Companion.FIRE_HEIGHT
import com.tokopedia.productcard.ProductCardModel.Companion.FIRE_WIDTH
import com.tokopedia.productcard.ProductCardModel.Companion.WORDING_SEGERA_HABIS
import com.tokopedia.productcard.utils.*
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.product_card_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_list_layout.view.*
import kotlinx.android.synthetic.main.product_card_list_layout.view.buttonAddToCart
import kotlinx.android.synthetic.main.product_card_list_layout.view.buttonNotify
import kotlinx.android.synthetic.main.product_card_list_layout.view.cardViewProductCard
import kotlinx.android.synthetic.main.product_card_list_layout.view.constraintLayoutProductCard
import kotlinx.android.synthetic.main.product_card_list_layout.view.imageProduct
import kotlinx.android.synthetic.main.product_card_list_layout.view.imageThreeDots
import kotlinx.android.synthetic.main.product_card_list_layout.view.labelBestSeller
import kotlinx.android.synthetic.main.product_card_list_layout.view.labelCampaignBackground
import kotlinx.android.synthetic.main.product_card_list_layout.view.labelProductStatus
import kotlinx.android.synthetic.main.product_card_list_layout.view.progressBarStock
import kotlinx.android.synthetic.main.product_card_list_layout.view.textTopAds
import kotlinx.android.synthetic.main.product_card_list_layout.view.textViewLabelCampaign
import kotlinx.android.synthetic.main.product_card_list_layout.view.textViewStockLabel

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

        renderLabelBestSeller(labelBestSeller, productCardModel)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        renderProductCardContent(productCardModel)

        renderStockPercentage(productCardModel)
        renderStockLabel(productCardModel)

        imageThreeDots?.showWithCondition(productCardModel.hasThreeDots)

        buttonDeleteProduct?.showWithCondition(productCardModel.hasDeleteProductButton)

        buttonRemoveFromWishlist?.showWithCondition(productCardModel.hasRemoveFromWishlistButton)

        buttonAddToCart?.showWithCondition(productCardModel.hasAddToCartButton)
        buttonAddToCart?.buttonType = productCardModel.addToCartButtonType

        buttonNotify?.showWithCondition(productCardModel.hasNotifyMeButton)

        setAddToCartButtonText(productCardModel)

        constraintLayoutProductCard?.post {
            imageThreeDots?.expandTouchArea(
                    getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                    getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                    getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                    getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
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

    fun setNotifyMeOnClickListener(notifyMeClickListener: (View) -> Unit) {
        buttonNotify?.setOnClickListener(notifyMeClickListener)
    }

    private fun View.renderStockPercentage(productCardModel: ProductCardModel) {
        progressBarStock?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
            progressBarStock.setProgressIcon(icon = null)
            if (productCardModel.stockBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
                progressBarStock.setProgressIcon(
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_fire_filled),
                        width = context.resources.getDimension(FIRE_WIDTH).toInt(),
                        height = context.resources.getDimension(FIRE_HEIGHT).toInt())
            }
            progressBarStock.progressBarColorType = ProgressBarUnify.COLOR_RED
            progressBarStock.setValue(productCardModel.stockBarPercentage, false)
        }
    }

    private fun View.renderStockLabel(productCardModel: ProductCardModel) {
        textViewStockLabel?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
            textViewStockLabel.text = productCardModel.stockBarLabel
            if (productCardModel.stockBarLabelColor.isNotEmpty()) {
                textViewStockLabel.setTextColor(safeParseColor(productCardModel.stockBarLabelColor))
            } else {
                textViewStockLabel.setTextColor(MethodChecker.getColor(context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            }
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

    override fun getNotifyMeButton(): UnifyButton? = buttonNotify

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
