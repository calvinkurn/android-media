package com.tokopedia.productcard.experiments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Space
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout.inflate
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardCartExtension
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.renderProductCardContent
import com.tokopedia.productcard.renderProductCardFooter
import com.tokopedia.productcard.renderProductCardRibbon
import com.tokopedia.productcard.utils.ViewId
import com.tokopedia.productcard.utils.ViewStubId
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.findViewById
import com.tokopedia.productcard.utils.forceLightRed
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
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerController
import com.tokopedia.video_widget.VideoPlayerView
import kotlin.LazyThreadSafetyMode.NONE
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class GridViewStrategy(
    private val productCardView: ViewGroup,
): ProductCardStrategy {

    private val context: Context?
        get() = productCardView.context

    private fun <T> findViewById(@IdRes id: Int): T? = productCardView.findViewById(id)

    private fun <T> findViewById(viewStubId: ViewStubId, viewId: ViewId): T? =
        productCardView.findViewById(viewStubId, viewId)

    private val cartExtension = ProductCardCartExtension(productCardView)
    private val video: VideoPlayerController by lazy {
        VideoPlayerController(productCardView, R.id.videoProduct, R.id.productCardImage)
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
            ViewId(R.id.buttonAddToCartWishlist)
        )
    }
    private val buttonSeeSimilarProductWishlist: UnifyButton? by lazy(NONE) {
        findViewById(
            ViewStubId(R.id.buttonSeeSimilarProductWishlistStub),
            ViewId(R.id.buttonSeeSimilarProductWishlist)
        )
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

    private val progressBarStock: ProgressBarUnify? by lazy(NONE) {
        findViewById(R.id.progressBarStock)
    }
    private val textViewStockLabel: Typography? by lazy(NONE) {
        findViewById(R.id.textViewStockLabel)
    }
    private val productName: Typography? by lazy(NONE) {
        findViewById(R.id.textViewProductName)
    }
    private val productPrice: Typography? by lazy(NONE) {
        findViewById(R.id.textViewPrice)
    }
    private val labelDiscount: Label? by lazy(NONE) {
        findViewById(R.id.labelDiscount)
    }
    private val productSlashPrice: Typography? by lazy(NONE) {
        findViewById(R.id.textViewSlashedPrice)
    }
    private val soldCount: Typography? by lazy(NONE) {
        findViewById(R.id.textViewSales)
    }
    private val rating: Typography? by lazy(NONE) {
        findViewById(R.id.salesRatingFloat)
    }
    private val gimmick: Typography? by lazy(NONE) {
        findViewById(R.id.textViewGimmick)
    }
    private val salesRatingFloatLine: View? by lazy(NONE) {
        findViewById(R.id.salesRatingFloatLine)
    }
    private val textViewIntegrity: Typography? by lazy(NONE) {
        findViewById(R.id.textViewIntegrity)
    }
    private val textViewFulfillment: Typography? by lazy(NONE) {
        findViewById(R.id.textViewFulfillment)
    }
    private val buttonAddToCart: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonAddToCart)
    }

    private val labelPriceReposition: Label? by lazy(NONE) {
        findViewById(R.id.labelPriceReposition)
    }

    private val textViewShopLocation: Typography? by lazy(NONE) {
        findViewById(R.id.textViewShopLocation)
    }

    private var forceWidthPx: Int = 0

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        initAttributes(attrs)

        View.inflate(context, R.layout.product_card_grid_layout, productCardView)

        val footerView = createFooterView()
        productCardFooterLayoutContainer?.addView(footerView)

        cardViewProductCard?.updateLayoutParams {
            this.width = if (forceWidthPx > 0) forceWidthPx else MATCH_PARENT
        }
    }

    private fun initAttributes(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context
            ?.obtainStyledAttributes(attrs, R.styleable.ProductCardView, 0, 0)
            ?: return

        try {
            isUsingViewStub = typedArray.getBoolean(R.styleable.ProductCardView_useViewStub, false)
            forceWidthPx = typedArray.getDimensionPixelSize(R.styleable.ProductCardView_forceWidth, 0)
        } finally {
            typedArray.recycle()
        }
    }

    private fun createFooterView(): View = inflateFooterView().apply {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    private fun inflateFooterView() =
        if (isUsingViewStub) {
            inflate(
                context,
                R.layout.product_card_footer_with_viewstub_layout,
                null
            )
        } else {
            inflate(context, R.layout.product_card_footer_layout, null)
        }

    override fun setProductModel(productCardModel: ProductCardModel) {
        productCardModel.layoutStrategy.renderProductCardShadow(
            productCardModel,
            productCardView,
            cardViewProductCard,
            true
        )

        productCardView.renderProductCardRibbon(productCardModel, true)

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

        productCardView.renderProductCardContent(
            productCardModel = productCardModel,
            isWideContent = productCardModel.isWideContent,
        )

        productCardModel
            .layoutStrategy
            .renderStockBar(productCardModel, productCardView)

        productCardView
            .renderProductCardFooter(productCardModel, isProductCardList = false)

        imageThreeDots.shouldShowWithAction(productCardModel.hasThreeDots) {
            constraintLayoutProductCard?.post {
                imageThreeDots?.expandTouchArea(
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_8),
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_16),
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_8),
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_16)
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

        productCardModel.layoutStrategy.renderCardHeight(productCardView, cardViewProductCard)

        if (productCardModel.forceLightModeColor)
            forceLightModeColor()
    }

    private fun cardViewAnimationOnPress(productCardModel: ProductCardModel): Int {
        return if(productCardModel.cardInteraction != null) {
            val isOverlayBounce =
                remoteConfig.getBoolean(PRODUCT_CARD_ENABLE_INTERACTION, true)
                    && productCardModel.cardInteraction
            if (isOverlayBounce) ANIMATE_OVERLAY_BOUNCE else ANIMATE_OVERLAY
        } else productCardModel.animateOnPress
    }


    private fun forceLightModeColor() {
        val context = context ?: return

        cardViewProductCard?.cardBorderColor = ContextCompat.getColor(
            context,
            R.color.dms_static_light_NN200
        )
        cardViewProductCard?.cardType = CardUnify2.TYPE_BORDER_FROM_SHADOW
        cardViewProductCard?.setCardUnifyBackgroundColor(ContextCompat.getColor(context, R.color.dms_static_white))
        productName?.setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_NN950_96))
        productPrice?.setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_NN950_96))
        labelDiscount?.forceLightRed()
        productSlashPrice?.setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_NN950_44))
        soldCount?.setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_NN950_68))
        rating?.setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_NN950_68))
        gimmick?.setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_YN400))
        salesRatingFloatLine?.setBackgroundColor(ContextCompat.getColor(context, R.color.dms_static_light_NN950_32))
        textViewIntegrity?.setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_NN950_68))
        textViewFulfillment?.setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_NN950_68))
        textViewStockLabel?.setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_NN950_68))
        progressBarStock?.trackDrawable?.apply { setColor(ContextCompat.getColor(context, R.color.dms_static_light_NN100)) }
        buttonAddToCart?.applyColorMode(ColorMode.LIGHT_MODE)
        buttonAddVariant?.applyColorMode(ColorMode.LIGHT_MODE)
    }

    override fun setImageProductViewHintListener(impressHolder: ImpressHolder, viewHintListener: ViewHintListener) {
        imageProduct?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    override fun setThreeDotsOnClickListener(l: View.OnClickListener?) {
        imageThreeDots?.setOnClickListener(l)
    }

    override fun setAddToCartOnClickListener(l: View.OnClickListener?) {
        cartExtension.addToCartClickListener = { l?.onClick(it) }
    }

    override fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) {
        cartExtension.addToCartNonVariantClickListener = addToCartNonVariantClickListener
    }

    override fun setSimilarProductClickListener(similarProductClickListener: (View) -> Unit) {
        buttonSimilarProduct?.setOnClickListener(similarProductClickListener)
    }

    override fun setAddVariantClickListener(addVariantClickListener: (View) -> Unit) {
        buttonAddVariant?.setOnClickListener(addVariantClickListener)
    }

    override fun setNotifyMeOnClickListener(notifyMeClickListener: (View) -> Unit) {
        buttonNotify?.setOnClickListener(notifyMeClickListener)
    }

    override fun setThreeDotsWishlistOnClickListener(threeDotsClickListener: (View) -> Unit) {
        buttonThreeDotsWishlist?.setOnClickListener(threeDotsClickListener)
    }

    override fun setAddToCartWishlistOnClickListener(addToCartWishlistClickListener: (View) -> Unit) {
        buttonAddToCartWishlist?.setOnClickListener(addToCartWishlistClickListener)
    }

    override fun setSeeSimilarProductWishlistOnClickListener(seeSimilarProductWishlistClickListener: (View) -> Unit) {
        buttonSeeSimilarProductWishlist?.setOnClickListener(seeSimilarProductWishlistClickListener)
    }

    override fun setSeeOtherProductOnClickListener(seeOtherProductOnClickListener: (View) -> Unit) {
        buttonSeeOtherProduct?.setOnClickListener(seeOtherProductOnClickListener)
    }

    override fun getCardMaxElevation() = cardViewProductCard?.maxCardElevation ?: 0f

    override fun getCardRadius() = cardViewProductCard?.radius ?: 0f

    override fun applyCarousel() {
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

    override fun getProductImageView(): ImageView? = imageProduct
    override fun setProductImageOnClickListener(l: (View) -> Unit) {
        imageProduct?.setOnClickListener(l)
    }

    override fun setShopTypeLocationOnClickListener(l: (View) -> Unit) {
        imageShopBadge?.setOnClickListener(l)
        textViewShopLocation?.setOnClickListener(l)
    }

    override fun getVideoPlayerController(): VideoPlayerController = video

    override fun setOnClickListener(l: View.OnClickListener?) {
        cardViewProductCard?.setOnClickListener(l)
    }
    override fun setOnClickListener(l: ProductCardClickListener) {
        cardViewProductCard?.setOnClickListener {
            l.onAreaClicked(it)
            l.onClick(it)
        }
        setProductImageOnClickListener {
            l.onProductImageClicked(it)
            l.onClick(it)
        }
        setShopTypeLocationOnClickListener {
            l.onSellerInfoClicked(it)
            l.onClick(it)
        }
    }

    override fun setOnLongClickListener(l: View.OnLongClickListener?) {
        cardViewProductCard?.setOnLongClickListener(l)
    }
}
