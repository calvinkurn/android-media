package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.productcard.layout.LayoutStrategyBestSeller
import com.tokopedia.productcard.layout.LayoutStrategyListView
import com.tokopedia.productcard.utils.ViewId
import com.tokopedia.productcard.utils.ViewStubId
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.findViewById
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.glideClear
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadImageRounded
import com.tokopedia.productcard.utils.renderStockBar
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.PRODUCT_CARD_ENABLE_INTERACTION
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.CardUnify2.Companion.ANIMATE_OVERLAY
import com.tokopedia.unifycomponents.CardUnify2.Companion.ANIMATE_OVERLAY_BOUNCE
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerController
import kotlin.LazyThreadSafetyMode.NONE

class ProductCardListView: ConstraintLayout, IProductCardView {

    private val cartExtension = ProductCardCartExtension(this)
    private val video: VideoPlayerController by lazy{
        VideoPlayerController(this, R.id.videoProduct, R.id.productCardImage)
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
    private val mediaAnchorProduct: Space? by lazy(NONE) {
        findViewById(R.id.mediaAnchorProduct)
    }
    private val imageProduct: ImageView? by lazy(NONE) {
        findViewById(R.id.productCardImage)
    }
    private val buttonAddVariant: UnifyButton? by lazy(NONE) {
        findViewById(ViewStubId(R.id.buttonAddVariantStub), ViewId(R.id.buttonAddVariant))
    }
    private val buttonNotify: UnifyButton? by lazy(NONE) {
        findViewById(ViewStubId(R.id.buttonNotifyStub), ViewId(R.id.buttonNotify))
    }
    private val buttonThreeDotsWishlist: FrameLayout? by lazy(NONE) {
        findViewById(R.id.buttonThreeDotsWishlist)
    }
    private val buttonAddToCartWishlist: UnifyButton? by lazy(NONE) {
        findViewById(
            ViewStubId(R.id.buttonAddToCartWishlistStub),
            ViewId(R.id.buttonAddToCartWishlist))
    }
    private val buttonSeeSimilarProductWishlist: UnifyButton? by lazy(NONE) {
        findViewById(
            ViewStubId(R.id.buttonSeeSimilarProductWishlistStub),
            ViewId(R.id.buttonSeeSimilarProductWishlist))
    }
    private val imageShopBadge: ImageView? by lazy(NONE) {
        findViewById(R.id.imageShopBadge)
    }
    private val imageFreeOngkirPromo: ImageView? by lazy(NONE) {
        findViewById(R.id.imageFreeOngkirPromo)
    }
    private val buttonAddToCart: UnifyButton? by lazy(NONE) {
        findViewById(ViewStubId(R.id.buttonAddToCartStub), ViewId(R.id.buttonAddToCart))
    }
    private val buttonDeleteProduct: UnifyButton? by lazy(NONE) {
        findViewById(ViewStubId(R.id.buttonDeleteProductStub), ViewId(R.id.buttonDeleteProduct))
    }
    private val buttonRemoveFromWishlist: FrameLayout? by lazy(NONE) {
        findViewById(R.id.buttonRemoveFromWishlist)
    }
    private val spaceCampaignBestSeller: Space? by lazy(NONE) {
        findViewById(R.id.spaceCampaignBestSeller)
    }
    private val remoteConfig : RemoteConfig by lazy(NONE) {
        FirebaseRemoteConfigImpl(context)
    }
    private val productCardFooterLayoutContainer: FrameLayout? by lazy(NONE) {
        findViewById(R.id.productCardFooterLayoutContainer)
    }
    private val buttonSeeOtherProduct: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonSeeOtherProduct)
    }
    private val labelOverlayBackground: ImageView? by lazy(NONE) {
        findViewById(R.id.labelOverlayBackground)
    }
    private val labelOverlay: Typography? by lazy(NONE) {
        findViewById(R.id.labelOverlay)
    }
    private val labelOverlayStatus: Label? by lazy(NONE) {
        findViewById(R.id.labelOverlayStatus)
    }
    private val spaceMediaAnchorToInfo: Space? by lazyThreadSafetyNone {
        findViewById(R.id.spaceMediaAnchorToProductInfo)
    }
    private var isUsingViewStub = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        initAttributes(attrs)

        View.inflate(context, R.layout.product_card_list_layout, this)

        val footerView = createFooterView()
        productCardFooterLayoutContainer?.addView(footerView)
    }

    private fun initAttributes(attrs: AttributeSet?){
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProductCardView, 0, 0)

        try {
            isUsingViewStub = typedArray.getBoolean(R.styleable.ProductCardView_useViewStub, false)
        } finally {
            typedArray.recycle()
        }
    }

    private fun createFooterView(): View = inflateFooterView().apply {
        layoutParams = LayoutParams(MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    private fun inflateFooterView(): View =
        if (isUsingViewStub)
            inflate(context, R.layout.product_card_footer_with_viewstub_layout, null)
        else
            inflate(context, R.layout.product_card_footer_layout, null)

    override fun setProductModel(productCardModel: ProductCardModel) {
        productCardModel.layoutStrategy.renderProductCardShadow(
            productCardModel,
            this,
            cardViewProductCard,
            false,
        )

        renderProductCardRibbon(productCardModel, false)

        imageProduct?.loadImageRounded(
            productCardModel.productImageUrl,
            productCardModel.layoutStrategy.imageCornerRadius()
        )

        productCardModel.layoutStrategy.setImageSizeListView(mediaAnchorProduct)

        productCardModel.layoutStrategy.renderOverlayLabel(
            labelOverlayBackground,
            labelOverlay,
            labelOverlayStatus,
            productCardModel,
        )

        productCardModel.layoutStrategy.renderCampaignLabel(
            labelCampaignBackground,
            textViewLabelCampaign,
            productCardModel
        )

        productCardModel.layoutStrategy.renderLabelBestSeller(
            labelBestSeller,
            productCardModel
        )

        productCardModel.layoutStrategy.renderLabelBestSellerCategorySide(
            textCategorySide,
            productCardModel
        )

        productCardModel.layoutStrategy.renderLabelBestSellerCategoryBottom(
            textCategoryBottom,
            productCardModel
        )

        productCardModel.layoutStrategy.renderSpaceCampaignBestSeller(
            spaceCampaignBestSeller,
            productCardModel,
        )

        outOfStockOverlay?.showWithCondition(productCardModel.isOutOfStock)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        imageVideoIdentifier?.showWithCondition(productCardModel.hasVideo)

        val isMergePriceSection = productCardModel.layoutStrategy !is LayoutStrategyBestSeller
            && productCardModel.layoutStrategy !is LayoutStrategyListView

        setMediaAnchorToInfoSpaceSize(productCardModel)

        renderProductCardContent(
            productCardModel = productCardModel,
            isMergePriceSection = isMergePriceSection,
            isMergeShippingSection = true,
        )

        renderStockBar(progressBarStock, textViewStockLabel, productCardModel)

        renderProductCardFooter(productCardModel, isProductCardList = true)

        imageThreeDots.shouldShowWithAction(productCardModel.hasThreeDots) {
            constraintLayoutProductCard?.post {
                imageThreeDots?.expandTouchArea(
                    it.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                    it.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                    it.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                    it.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
                )
            }
        }

        cartExtension.setProductModel(productCardModel)
        video.setVideoURL(productCardModel.customVideoURL)

        cardViewProductCard?.animateOnPress = cardViewAnimationOnPress(productCardModel)
    }

    private fun setMediaAnchorToInfoSpaceSize(productCardModel: ProductCardModel) {
        val layoutParams = spaceMediaAnchorToInfo?.layoutParams ?: return
        layoutParams.width = productCardModel.layoutStrategy.getMediaAnchorToInfoSpaceSize()
        spaceMediaAnchorToInfo?.layoutParams = layoutParams
    }

    private fun cardViewAnimationOnPress(productCardModel: ProductCardModel): Int {
        return if(productCardModel.cardInteraction != null) {
            val isOverlayBounce =
                remoteConfig.getBoolean(PRODUCT_CARD_ENABLE_INTERACTION, true)
                    && productCardModel.cardInteraction
            if (isOverlayBounce) ANIMATE_OVERLAY_BOUNCE else ANIMATE_OVERLAY
        } else productCardModel.animateOnPress
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

    fun setSeeOtherProductOnClickListener(seeOtherProductOnClickListener: (View) -> Unit) {
        buttonSeeOtherProduct?.setOnClickListener(seeOtherProductOnClickListener)
    }

    override fun getCardMaxElevation() = cardViewProductCard?.maxCardElevation ?: 0f

    override fun getCardRadius() = cardViewProductCard?.radius ?: 0f

    fun applyCarousel() {
        setCardHeightMatchParent()
        resizeImageProductSize()
    }

    private fun setCardHeightMatchParent() {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = MATCH_PARENT
        cardViewProductCard?.layoutParams = layoutParams
    }

    private fun resizeImageProductSize() {
        val layoutParams = mediaAnchorProduct?.layoutParams
        layoutParams?.width = getDimensionPixelSize(R.dimen.product_card_carousel_list_image_size)
        layoutParams?.height = getDimensionPixelSize(R.dimen.product_card_carousel_list_image_size)
        mediaAnchorProduct?.layoutParams = layoutParams
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

    override fun getVideoPlayerController(): VideoPlayerController {
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

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        cardViewProductCard?.setOnClickListener(l)
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        super.setOnLongClickListener(l)
        cardViewProductCard?.setOnLongClickListener(l)
    }
}
