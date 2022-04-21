package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerController

abstract class BaseProductCardGridView: BaseCustomView, IProductCardView {
    abstract val view: View
    private val cartExtension by lazy {
        ProductCardCartExtension(view)
    }
    private val video: VideoPlayerController by lazy{
        VideoPlayerController(view, R.id.videoProduct, R.id.imageProduct)
    }
    private val cardViewProductCard: CardView? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.cardViewProductCard)
    }
    private val constraintLayoutProductCard: ConstraintLayout? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.constraintLayoutProductCard)
    }
    private val outOfStockOverlay: View? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.outOfStockOverlay)
    }
    private val labelProductStatus: Label? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.labelProductStatus)
    }
    private val textTopAds: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.textTopAds)
    }
    private val imageVideoIdentifier: ImageView? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.imageVideoIdentifier)
    }
    private val progressBarStock: ProgressBarUnify? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.progressBarStock)
    }
    private val textViewStockLabel: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.textViewStockLabel)
    }
    private val imageThreeDots: ImageView? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.imageThreeDots)
    }
    private val buttonSimilarProduct: UnifyButton? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.buttonSeeSimilarProduct)
    }
    private val labelCampaignBackground: ImageView? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.labelCampaignBackground)
    }
    private val textViewLabelCampaign: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.textViewLabelCampaign)
    }
    private val labelBestSeller: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.labelBestSeller)
    }
    private val textCategorySide: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.textCategorySide)
    }
    private val textCategoryBottom: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.textCategoryBottom)
    }
    private val imageProduct: ImageView? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.imageProduct)
    }
    private val buttonAddVariant: UnifyButton? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.buttonAddVariant)
    }
    private val buttonNotify: UnifyButton? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.buttonNotify)
    }
    private val buttonThreeDotsWishlist: FrameLayout? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.buttonThreeDotsWishlist)
    }
    private val buttonAddToCartWishlist: UnifyButton? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.buttonAddToCartWishlist)
    }
    private val buttonSeeSimilarProductWishlist: UnifyButton? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.buttonSeeSimilarProductWishlist)
    }
    private val imageShopBadge: ImageView? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.imageShopBadge)
    }
    private val imageFreeOngkirPromo: ImageView? by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.imageFreeOngkirPromo)
    }

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setProductModel(productCardModel: ProductCardModel) {
        imageProduct?.loadImage(productCardModel.productImageUrl)

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

        outOfStockOverlay?.showWithCondition(productCardModel.isOutOfStock)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        imageVideoIdentifier?.showWithCondition(productCardModel.hasVideo)

        view.renderProductCardContent(productCardModel, productCardModel.isWideContent)

        renderStockBar(progressBarStock, textViewStockLabel, productCardModel)

        view.renderProductCardFooter(productCardModel, isProductCardList = false)

        imageThreeDots.shouldShowWithAction(productCardModel.hasThreeDots) {
            constraintLayoutProductCard?.post {
                imageThreeDots?.expandTouchArea(
                    view.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                    view.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                    view.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                    view.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
                )
            }
        }

        cartExtension.setProductModel(productCardModel)
        video.setVideoURL(productCardModel.customVideoURL)
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
        layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        cardViewProductCard?.layoutParams = layoutParams
    }

    override fun recycle() {
        imageProduct?.glideClear()
        imageFreeOngkirPromo?.glideClear()
        labelCampaignBackground?.glideClear()
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