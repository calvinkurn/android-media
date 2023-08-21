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
import com.tokopedia.productcard.utils.ViewId
import com.tokopedia.productcard.utils.ViewStubId
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.findViewById
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.glideClear
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.PRODUCT_CARD_ENABLE_INTERACTION
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.CardUnify2.Companion.ANIMATE_OVERLAY
import com.tokopedia.unifycomponents.CardUnify2.Companion.ANIMATE_OVERLAY_BOUNCE
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerController
import com.tokopedia.video_widget.VideoPlayerView
import kotlin.LazyThreadSafetyMode.NONE

class ProductCardGridView : ConstraintLayout, IProductCardView {

    private val cartExtension = ProductCardCartExtension(this)
    private val video: VideoPlayerController by lazy {
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
    private val imageThreeDots: ImageView? by lazy(NONE) {
        findViewById(R.id.imageThreeDots)
    }
    private val buttonSimilarProduct: UnifyButton? by lazy(NONE) {
        findViewById(ViewStubId(R.id.buttonSeeSimilarProductStub), ViewId(R.id.buttonSeeSimilarProduct))
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
        findViewById(R.id.productCardImage)
    }
    private val mediaAnchorProduct: Space? by lazy(NONE) {
        findViewById(R.id.mediaAnchorProduct)
    }
    private val videoProduct: VideoPlayerView? by lazy(NONE) {
        findViewById(R.id.videoProduct)
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
    private val remoteConfig : RemoteConfig by lazy(NONE) {
        FirebaseRemoteConfigImpl(context)
    }
    private val productCardFooterLayoutContainer: FrameLayout? by lazy(NONE) {
        findViewById(R.id.productCardFooterLayoutContainer)
    }
    private val labelRepositionBackground: ImageView? by lazy(NONE) {
        findViewById(R.id.labelRepositionBackground)
    }
    private val labelReposition: Typography? by lazy(NONE) {
        findViewById(R.id.labelReposition)
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
    private val buttonSeeOtherProduct: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonSeeOtherProduct)
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

        View.inflate(context, R.layout.product_card_grid_layout, this)

        val footerView = createFooterView()
        productCardFooterLayoutContainer?.addView(footerView)
    }

    private fun initAttributes(attrs: AttributeSet?) {
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

    private fun inflateFooterView() =
        if (isUsingViewStub)
            inflate(context, R.layout.product_card_footer_with_viewstub_layout, null)
        else
            inflate(context, R.layout.product_card_footer_layout, null)

    override fun setProductModel(productCardModel: ProductCardModel) {
        productCardModel.layoutStrategy.renderProductCardShadow(
            productCardModel,
            this,
            cardViewProductCard,
            true
        )

        renderProductCardRibbon(productCardModel, true)

        imageProduct?.loadImage(productCardModel.productImageUrl)

        productCardModel.layoutStrategy.setupImageRatio(
            constraintLayoutProductCard,
            imageProduct,
            mediaAnchorProduct,
            videoProduct,
            productCardModel,
        )

        productCardModel.layoutStrategy.renderOverlayLabel(
            labelOverlayBackground,
            labelOverlay,
            labelOverlayStatus,
            productCardModel,
        )

        productCardModel.layoutStrategy.renderCampaignLabel(
            labelCampaignBackground,
            textViewLabelCampaign,
            productCardModel,
        )

        productCardModel.layoutStrategy.renderLabelBestSeller(labelBestSeller, productCardModel)

        productCardModel.layoutStrategy.renderLabelBestSellerCategorySide(
            textCategorySide,
            productCardModel,
        )

        productCardModel.layoutStrategy.renderLabelBestSellerCategoryBottom(
            textCategoryBottom,
            productCardModel,
        )

        outOfStockOverlay?.showWithCondition(productCardModel.isOutOfStock)

        productCardModel.layoutStrategy.renderProductStatusLabel(labelProductStatus, productCardModel)

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        imageVideoIdentifier?.showWithCondition(productCardModel.hasVideo)

        renderProductCardContent(
            productCardModel,
            productCardModel.isWideContent,
            productCardModel.isWideContent,
        )

        productCardModel
            .layoutStrategy
            .renderStockBar(productCardModel, this)

        renderProductCardFooter(productCardModel, isProductCardList = false)

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

        productCardModel.layoutStrategy.renderLabelReposition(
            labelRepositionBackground,
            labelReposition,
            productCardModel,
        )

        productCardModel.layoutStrategy.renderCardHeight(this, cardViewProductCard)
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

    fun setAddToCartOnClickListener(addToCartClickListener: (View) -> Unit) {
        cartExtension.addToCartClickListener = addToCartClickListener
    }

    fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) {
        cartExtension.addToCartNonVariantClickListener = addToCartNonVariantClickListener
    }

    fun setSimilarProductClickListener(similarProductClickListener: (View) -> Unit) {
        buttonSimilarProduct?.setOnClickListener(similarProductClickListener)
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
    }

    private fun setCardHeightMatchParent() {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = MATCH_PARENT
        cardViewProductCard?.layoutParams = layoutParams
    }

    override fun recycle() {
        imageProduct?.glideClear()
        imageFreeOngkirPromo?.glideClear()
        labelCampaignBackground?.glideClear()
        labelOverlayBackground?.glideClear()
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

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        cardViewProductCard?.setOnClickListener(l)
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        super.setOnLongClickListener(l)
        cardViewProductCard?.setOnLongClickListener(l)
    }
}
