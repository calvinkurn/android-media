package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
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
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.productcard.utils.renderStockBar
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.video_widget.VideoPlayerController
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerView
import kotlin.LazyThreadSafetyMode.NONE

class ProductCardGridView : BaseCustomView, IProductCardView {

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
    private val progressBarStock: ProgressBarUnify? by lazy(NONE) {
        findViewById(R.id.progressBarStock)
    }
    private val textViewStockLabel: Typography? by lazy(NONE) {
        findViewById(R.id.textViewStockLabel)
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
    private val productCardFooterLayoutContainer: FrameLayout by lazy(NONE) {
        findViewById(R.id.productCardFooterLayoutContainer)
    }
    private val labelRepositionBackground: ImageView by lazy(NONE) {
        findViewById(R.id.labelRepositionBackground)
    }
    private val labelReposition: Typography by lazy(NONE) {
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
    private var isUsingViewStub = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initWithAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initWithAttrs(attrs)
    }

    private fun init() {
        View.inflate(context, R.layout.product_card_grid_layout, this)

        val footerView = View.inflate(context, R.layout.product_card_footer_layout, null)
        footerView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        productCardFooterLayoutContainer.addView(footerView)
    }

    private fun initWithAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProductCardView, 0, 0)

        try {
            isUsingViewStub = typedArray.getBoolean(R.styleable.ProductCardView_useViewStub, false)
        } finally {
            typedArray.recycle()
        }

        View.inflate(context, R.layout.product_card_grid_layout, this)

        val footerView =
            if (isUsingViewStub)
                View.inflate(context, R.layout.product_card_footer_with_viewstub_layout, null)
            else
                View.inflate(context, R.layout.product_card_footer_layout, null)

        footerView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        productCardFooterLayoutContainer.addView(footerView)
    }

    override fun setProductModel(productCardModel: ProductCardModel) {
        imageProduct?.loadImage(productCardModel.productImageUrl)

        productCardModel.layoutStrategy.setupImageRatio(
            constraintLayoutProductCard,
            imageProduct,
            mediaAnchorProduct,
            videoProduct,
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

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        imageVideoIdentifier?.showWithCondition(productCardModel.hasVideo)

        renderProductCardContent(productCardModel, productCardModel.isWideContent)

        renderStockBar(progressBarStock, textViewStockLabel, productCardModel)

        renderProductCardFooter(productCardModel, isProductCardList = false)

        imageThreeDots.shouldShowWithAction(productCardModel.hasThreeDots) {
            constraintLayoutProductCard?.post {
                imageThreeDots?.expandTouchArea(
                    getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                    getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                    getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                    getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
                )
            }
        }

        cartExtension.setProductModel(productCardModel)
        video.setVideoURL(productCardModel.customVideoURL)

        cardViewProductCard?.animateOnPress = if(remoteConfig.getBoolean(RemoteConfigKey.PRODUCT_CARD_ENABLE_INTERACTION, true)
            && productCardModel.cardInteraction){
            CardUnify2.ANIMATE_OVERLAY_BOUNCE
        } else CardUnify2.ANIMATE_OVERLAY

        productCardModel.layoutStrategy.renderLabelReposition(
            labelRepositionBackground,
            labelReposition,
            productCardModel,
        )

        productCardModel.layoutStrategy.renderOverlayLabel(
            labelOverlayBackground,
            labelOverlay,
            labelOverlayStatus,
            productCardModel,
        )
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
