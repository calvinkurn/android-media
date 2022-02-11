package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.glideClear
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadImageRounded
import com.tokopedia.productcard.utils.renderLabelBestSeller
import com.tokopedia.productcard.utils.renderLabelBestSellerCategoryBottom
import com.tokopedia.productcard.utils.renderLabelBestSellerCategorySide
import com.tokopedia.productcard.utils.renderLabelCampaign
import com.tokopedia.productcard.utils.renderStockBar
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.product_card_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_footer_layout.view.*
import kotlinx.android.synthetic.main.product_card_list_layout.view.*

class ProductCardListView: BaseCustomView, IProductCardView {

    private val cartExtension = ProductCardCartExtension(this)

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

        val isShowCampaign = productCardModel.isShowLabelCampaign()
        renderLabelCampaign(
                isShowCampaign,
                labelCampaignBackground,
                textViewLabelCampaign,
                productCardModel
        )

        val isShowBestSeller = productCardModel.isShowLabelBestSeller()
        renderLabelBestSeller(
            isShowBestSeller,
            labelBestSeller,
            productCardModel
        )

        val isShowCategorySide = productCardModel.isShowLabelCategorySide()
        renderLabelBestSellerCategorySide(
            isShowCategorySide,
            textCategorySide,
            productCardModel
        )

        val isShowCategoryBottom = productCardModel.isShowLabelCategoryBottom()
        renderLabelBestSellerCategoryBottom(
            isShowCategoryBottom,
            textCategoryBottom,
            productCardModel
        )

        val isShowCampaignOrBestSeller = isShowCampaign || isShowBestSeller
        spaceCampaignBestSeller?.showWithCondition(isShowCampaignOrBestSeller)

        outOfStockOverlay?.showWithCondition(productCardModel.isOutOfStock)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        imageVideoIdentifier?.showWithCondition(productCardModel.hasVideo)

        renderProductCardContent(productCardModel, isWideContent = true)

        renderStockBar(progressBarStock, textViewStockLabel, productCardModel)

        renderProductCardFooter(productCardModel, isProductCardList = true)

        imageThreeDots?.showWithCondition(productCardModel.hasThreeDots)

        cartExtension.setProductModel(productCardModel)

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
        cartExtension.addToCartClickListener = addToCartClickListener
    }

    fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) {
        cartExtension.addToCartNonVariantClickListener = addToCartNonVariantClickListener
    }

    fun setAddVariantClickListener(addVariantClickListener: (View) -> Unit) {
        buttonAddVariant?.setOnClickListener(addVariantClickListener)
    }

    fun setNotifyMeOnClickListener(notifyMeClickListener: (View) -> Unit) {
        buttonNotify?.setOnClickListener(notifyMeClickListener)
    }

    fun setThreeDotsWishlistOnClickListener(threeDotsClickListener: (View) -> Unit) {
        buttonThreeDotsWishlist?.setOnClickListener(threeDotsClickListener)
    }

    fun setAddToCartWishlistOnClickListener(addToCartWishlistClickListener: (View) -> Unit) {
        buttonAddToCartWishlist?.setOnClickListener(addToCartWishlistClickListener)
    }

    fun setSeeSimilarProductWishlistOnClickListener(seeSimilarProductWishlistClickListener: (View) -> Unit) {
        buttonSeeSimilarProductWishlist?.setOnClickListener(seeSimilarProductWishlistClickListener)
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
        cartExtension.clear()
    }

    override fun getThreeDotsButton(): View? = imageThreeDots

    override fun getNotifyMeButton(): UnifyButton? = buttonNotify

    override fun getShopBadgeView(): View? = imageShopBadge

    override fun getProductImageView(): ImageView? {
        return imageProduct
    }

    override fun getProductVideoView(): ProductVideoView? {
        return videoProduct
    }

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
        buttonAddToCart?.text = context.getString(R.string.product_card_text_add_to_cart_grid)
    }

    fun wishlistPage_disableButtonAddToCart(){
        buttonAddToCart?.isEnabled = false
        buttonAddToCart?.text = context.getString(R.string.product_card_text_add_to_cart_grid)
    }

    fun wishlistPage_setOutOfStock(){
        buttonAddToCart?.isEnabled = false
        buttonAddToCart?.buttonVariant = UnifyButton.Variant.FILLED
        buttonAddToCart?.text = context.getString(R.string.product_card_out_of_stock)
    }
}
