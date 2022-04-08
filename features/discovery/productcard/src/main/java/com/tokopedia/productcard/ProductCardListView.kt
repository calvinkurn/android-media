package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.tokopedia.productcard.video.ProductCardVideo
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography
import kotlin.LazyThreadSafetyMode.NONE

class ProductCardListView: BaseCustomView, IProductCardView {

    private val cartExtension = ProductCardCartExtension(this)
    private val video: ProductCardVideo by lazy{
        ProductCardVideo(this)
    }
    private val cardViewProductCard: CardUnify2? by lazy(NONE) {
        findViewById(R.id.cardViewProductCard)
    }
    private val constraintLayoutProductCard: ConstraintLayout? by lazy(NONE) {
        findViewById(R.id.constraintLayoutProductCard)
    }
    private val outOfStockOverlay: View? by lazy(NONE) {
        findViewById(R.id.outOfStockOverlay)
    }
    private val labelProductStatus: Label? by lazy(NONE) {
        findViewById(R.id.labelProductStatus)
    }
    private val textTopAds: Typography? by lazy(NONE) {
        findViewById(R.id.textTopAds)
    }
    private val imageVideoIdentifier: ImageView? by lazy(NONE) {
        findViewById(R.id.imageVideoIdentifier)
    }
    private val progressBarStock: ProgressBarUnify? by lazy(NONE) {
        findViewById(R.id.progressBarStock)
    }
    private val textViewStockLabel: Typography? by lazy(NONE) {
        findViewById(R.id.textViewStockLabel)
    }
    private val imageThreeDots: ImageView? by lazy(NONE) {
        findViewById(R.id.imageThreeDots)
    }
    private val labelCampaignBackground: ImageView? by lazy(NONE) {
        findViewById(R.id.labelCampaignBackground)
    }
    private val textViewLabelCampaign: Typography? by lazy(NONE) {
        findViewById(R.id.textViewLabelCampaign)
    }
    private val labelBestSeller: Typography? by lazy(NONE) {
        findViewById(R.id.labelBestSeller)
    }
    private val textCategorySide: Typography? by lazy(NONE) {
        findViewById(R.id.textCategorySide)
    }
    private val textCategoryBottom: Typography? by lazy(NONE) {
        findViewById(R.id.textCategoryBottom)
    }
    private val imageProduct: ImageView? by lazy(NONE) {
        findViewById(R.id.imageProduct)
    }
    private val buttonAddVariant: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonAddVariant)
    }
    private val buttonNotify: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonNotify)
    }
    private val buttonThreeDotsWishlist: FrameLayout? by lazy(NONE) {
        findViewById(R.id.buttonThreeDotsWishlist)
    }
    private val buttonAddToCartWishlist: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonAddToCartWishlist)
    }
    private val buttonSeeSimilarProductWishlist: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonSeeSimilarProductWishlist)
    }
    private val imageShopBadge: ImageView? by lazy(NONE) {
        findViewById(R.id.imageShopBadge)
    }
    private val imageFreeOngkirPromo: ImageView? by lazy(NONE) {
        findViewById(R.id.imageFreeOngkirPromo)
    }
    private val buttonAddToCart: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonAddToCart)
    }
    private val buttonDeleteProduct: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonDeleteProduct)
    }
    private val buttonRemoveFromWishlist: FrameLayout? by lazy(NONE) {
        findViewById(R.id.buttonRemoveFromWishlist)
    }
    private val spaceCampaignBestSeller: Space? by lazy(NONE) {
        findViewById(R.id.spaceCampaignBestSeller)
    }

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
        video.setProductModel(productCardModel)

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
        video.clear()
    }

    override fun getThreeDotsButton(): View? = imageThreeDots

    override fun getNotifyMeButton(): UnifyButton? = buttonNotify

    override fun getShopBadgeView(): View? = imageShopBadge

    override fun getProductImageView(): ImageView? {
        return imageProduct
    }

    override fun getProductCardVideo(): ProductCardVideo {
        return video
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
        val buttonAddToCart = buttonAddToCart ?: return
        buttonAddToCart.isEnabled = true
        buttonAddToCart.buttonVariant = UnifyButton.Variant.GHOST
        buttonAddToCart.text = context.getString(R.string.product_card_text_add_to_cart_grid)
    }

    fun wishlistPage_disableButtonAddToCart(){
        val buttonAddToCart = buttonAddToCart ?: return
        buttonAddToCart.isEnabled = false
        buttonAddToCart.text = context.getString(R.string.product_card_text_add_to_cart_grid)
    }

    fun wishlistPage_setOutOfStock(){
        val buttonAddToCart = buttonAddToCart ?: return
        buttonAddToCart.isEnabled = false
        buttonAddToCart.buttonVariant = UnifyButton.Variant.FILLED
        buttonAddToCart.text = context.getString(R.string.product_card_out_of_stock)
    }

    override fun setCardInteraction(animation: Int) {
        cardViewProductCard?.animateOnPress = animation
    }
}
