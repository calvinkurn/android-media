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
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardCartExtension
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
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadImageRounded
import com.tokopedia.productcard.utils.renderStockBar
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.CardUnify2.Companion.ANIMATE_OVERLAY
import com.tokopedia.unifycomponents.CardUnify2.Companion.ANIMATE_OVERLAY_BOUNCE
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerController
import kotlin.LazyThreadSafetyMode.NONE
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class ListViewStrategy(
    private val productCardView: ViewGroup,
): ProductCardStrategy {

    private val context: Context?
        get() = productCardView.context

    private fun <T> findViewById(@IdRes id: Int): T? = productCardView.findViewById(id)

    private fun <T> findViewById(viewStubId: ViewStubId, viewId: ViewId): T? =
        productCardView.findViewById(viewStubId, viewId)

    private val cartExtension = ProductCardCartExtension(productCardView)
    private val video: VideoPlayerController by lazy{
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
    private var forceWidthPx: Int = 0

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        initAttributes(attrs)

        View.inflate(context, R.layout.product_card_list_layout, productCardView)

        val footerView = createFooterView()
        productCardFooterLayoutContainer?.addView(footerView)

        cardViewProductCard?.updateLayoutParams {
            this.width = if (forceWidthPx > 0) forceWidthPx else MATCH_PARENT
        }
    }

    private fun initAttributes(attrs: AttributeSet?){
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

    private fun inflateFooterView(): View =
        if (isUsingViewStub)
            View.inflate(
                context,
                R.layout.product_card_footer_with_viewstub_layout,
                null
            )
        else
            View.inflate(context, R.layout.product_card_footer_layout, null)

    override fun setProductModel(productCardModel: ProductCardModel) {
        productCardModel.layoutStrategy.renderProductCardShadow(
            productCardModel,
            productCardView,
            cardViewProductCard,
            false,
        )

        productCardView.renderProductCardRibbon(productCardModel, false)

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

        setMediaAnchorToInfoSpaceSize(productCardModel)

        productCardView.renderProductCardContent(
            productCardModel = productCardModel,
            isWideContent = true,
        )

        renderStockBar(progressBarStock, textViewStockLabel, productCardModel)

        productCardView.renderProductCardFooter(productCardModel, isProductCardList = true)

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

        if (productCardModel.forceLightModeColor) {
            forceLightModeColor()
        }
    }

    private fun setMediaAnchorToInfoSpaceSize(productCardModel: ProductCardModel) {
        val layoutParams = spaceMediaAnchorToInfo?.layoutParams ?: return
        layoutParams.width = productCardModel.layoutStrategy.getMediaAnchorToInfoSpaceSize()
        spaceMediaAnchorToInfo?.layoutParams = layoutParams
    }

    private fun cardViewAnimationOnPress(productCardModel: ProductCardModel): Int {
        return if(productCardModel.cardInteraction != null) {
            val isOverlayBounce =
                remoteConfig.getBoolean(RemoteConfigKey.PRODUCT_CARD_ENABLE_INTERACTION, true)
                    && productCardModel.cardInteraction
            if (isOverlayBounce) ANIMATE_OVERLAY_BOUNCE else ANIMATE_OVERLAY
        } else productCardModel.animateOnPress
    }

    private fun forceLightModeColor() {
        val context = context ?: return

        cardViewProductCard?.cardBorderColor = ContextCompat.getColor(
            context,
            R.color.dms_static_white
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

    override fun setOnClickListener(l: View.OnClickListener?) {
        cardViewProductCard?.setOnClickListener(l)
    }

    override fun setOnLongClickListener(l: View.OnLongClickListener?) {
        cardViewProductCard?.setOnLongClickListener(l)
    }

    override fun setImageProductViewHintListener(
        impressHolder: ImpressHolder,
        viewHintListener: ViewHintListener,
    ) {
        imageProduct?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    override fun setThreeDotsOnClickListener(l: View.OnClickListener?) {
        imageThreeDots?.setOnClickListener(l)
    }

    override fun setDeleteProductOnClickListener(deleteProductClickListener: (View) -> Unit) {
        buttonDeleteProduct?.setOnClickListener(deleteProductClickListener)
    }

    override fun setRemoveWishlistOnClickListener(removeWishlistClickListener: (View) -> Unit) {
        buttonRemoveFromWishlist?.setOnClickListener(removeWishlistClickListener)
    }

    override fun setAddToCartOnClickListener(l: View.OnClickListener?) {
        cartExtension.addToCartClickListener = { l?.onClick(it) }
    }

    override fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) {
        cartExtension.addToCartNonVariantClickListener = addToCartNonVariantClickListener
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
        resizeImageProductSize()
    }

    private fun setCardHeightMatchParent() {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = MATCH_PARENT
        cardViewProductCard?.layoutParams = layoutParams
    }

    private fun resizeImageProductSize() {
        val layoutParams = mediaAnchorProduct?.layoutParams
        layoutParams?.width = productCardView.getDimensionPixelSize(R.dimen.product_card_carousel_list_image_size)
        layoutParams?.height = productCardView.getDimensionPixelSize(R.dimen.product_card_carousel_list_image_size)
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
}
