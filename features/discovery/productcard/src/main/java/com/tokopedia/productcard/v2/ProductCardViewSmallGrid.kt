package com.tokopedia.productcard.v2

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.product_card_layout_v2_small_grid.view.*

/**
 * ProductCardView with Small Grid layout.
 */
@Deprecated("Please use ProductCardGridView or ProductCardListView")
class ProductCardViewSmallGrid: ProductCardView {

    private var imageShop: ImageView? = null
    private var textViewAddToCart: Typography? = null
    private var labelSoldOut: Label? = null
    private var layoutEmptyStock: View? = null
    private var labelPreOrder: Label? = null
    private var labelWholesale: Label? = null

    /**
     * View components of a Skeleton ProductCardView
     */
    private var skeleton_constraintLayoutProductCard: ConstraintLayout? = null
    private var skeleton_imageProduct: ImageView? = null
    private var skeleton_labelPromo: Label? = null
    private var skeleton_textViewShopName: Typography? = null
    private var skeleton_textViewProductName: Typography? = null
    private var skeleton_labelDiscount: Label? = null
    private var skeleton_textViewSlashedPrice: Typography? = null
    private var skeleton_textViewPrice: Typography? = null
    private var skeleton_linearLayoutShopBadges: LinearLayout? = null
    private var skeleton_textViewShopLocation: Typography? = null
    private var skeleton_linearLayoutImageRating: LinearLayout? = null
    private var skeleton_imageViewRating1: ImageView? = null
    private var skeleton_imageViewRating2: ImageView? = null
    private var skeleton_imageViewRating3: ImageView? = null
    private var skeleton_imageViewRating4: ImageView? = null
    private var skeleton_imageViewRating5: ImageView? = null
    private var skeleton_textViewReviewCount: Typography? = null
    private var skeleton_imageFreeOngkirPromo: ImageView? = null
    private var skeleton_labelCredibility: Label? = null
    private var skeleton_labelOffers: Label? = null
    private var skeleton_imageTopAds: ImageView? = null

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ): super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.product_card_layout_v2_small_grid
    }

    override fun findViews(inflatedView: View) {
        super.findViews(inflatedView)

        imageShop = inflatedView.findViewById(R.id.imageShop)
        textViewAddToCart = inflatedView.findViewById(R.id.textViewAddToCart)

        //skeleton
        skeleton_constraintLayoutProductCard = inflatedView.findViewById(R.id.skeleton_constraintLayoutProductCard)
        skeleton_imageProduct = inflatedView.findViewById(R.id.skeleton_imageProduct)
        skeleton_labelPromo = inflatedView.findViewById(R.id.skeleton_labelPromo)
        skeleton_textViewShopName = inflatedView.findViewById(R.id.skeleton_textViewShopName)
        skeleton_textViewProductName = inflatedView.findViewById(R.id.skeleton_textViewProductName)
        skeleton_labelDiscount = inflatedView.findViewById(R.id.skeleton_labelDiscount)
        skeleton_textViewSlashedPrice = inflatedView.findViewById(R.id.skeleton_textViewSlashedPrice)
        skeleton_textViewPrice = inflatedView.findViewById(R.id.skeleton_textViewPrice)
        skeleton_linearLayoutShopBadges = inflatedView.findViewById(R.id.skeleton_linearLayoutShopBadges)
        skeleton_textViewShopLocation = inflatedView.findViewById(R.id.skeleton_textViewShopLocation)
        skeleton_linearLayoutImageRating = inflatedView.findViewById(R.id.skeleton_linearLayoutImageRating)
        skeleton_imageViewRating1 = inflatedView.findViewById(R.id.skeleton_imageViewRating1)
        skeleton_imageViewRating2 = inflatedView.findViewById(R.id.skeleton_imageViewRating2)
        skeleton_imageViewRating3 = inflatedView.findViewById(R.id.skeleton_imageViewRating3)
        skeleton_imageViewRating4 = inflatedView.findViewById(R.id.skeleton_imageViewRating4)
        skeleton_imageViewRating5 = inflatedView.findViewById(R.id.skeleton_imageViewRating5)
        skeleton_textViewReviewCount = inflatedView.findViewById(R.id.skeleton_textViewReviewCount)
        skeleton_labelCredibility = inflatedView.findViewById(R.id.skeleton_labelCredibility)
        skeleton_imageFreeOngkirPromo = inflatedView.findViewById(R.id.skeleton_imageFreeOngkirPromo)
        skeleton_labelOffers = inflatedView.findViewById(R.id.skeleton_labelOffers)
        labelSoldOut = inflatedView.findViewById(R.id.labelEmptyStock)
        layoutEmptyStock = inflatedView.findViewById(R.id.layout_empty_stock)
        labelPreOrder= inflatedView.findViewById(R.id.label_pre_order)
        labelWholesale= inflatedView.findViewById(R.id.label_wholesale)
    }

    override fun setProductModel(productCardModel: ProductCardModel, blankSpaceConfig: BlankSpaceConfig) {
        super.setProductModel(productCardModel, blankSpaceConfig)
        initLabelSoldOut(productCardModel.isProductSoldOut)
        initLabelPreOrder(productCardModel.isProductPreOrder)
        initLabelWholesale(productCardModel.isProductWholesale)
    }

    private fun initLabelWholesale(productWholesale: Boolean) {
        labelWholesale?.visibility = if (productWholesale) View.VISIBLE else View.GONE
    }

    private fun initLabelPreOrder(productPreOrder: Boolean) {
        labelPreOrder?.visibility = if (productPreOrder) View.VISIBLE else View.GONE
    }

    private fun initLabelSoldOut(productSoldOut: Boolean) {
        val drawable =  MethodChecker.getDrawable(context, R.drawable.sold_out_label_bg)
        drawable?.let{
            labelSoldOut?.background = it
        }
        labelSoldOut?.visibility = if (productSoldOut) View.VISIBLE else View.GONE
        layoutEmptyStock?.visibility = if (productSoldOut) View.VISIBLE else View.GONE
    }

    fun setImageShopVisible(isVisible: Boolean) {
        imageShop?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setImageShopUrl(imageUrl: String) {
        imageShop?.let { imageShop ->
            imageShop.loadImageCircle(imageUrl)
        }
    }

    fun setAddToCartVisible(isVisible: Boolean) {
        textViewAddToCart?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setAddToCartOnClickListener(onClickListener: (view: View) -> Unit) {
        textViewAddToCart?.setOnClickListener(onClickListener)
    }

    override fun initShopName(shopName: String) {
        textViewShopName.setTextWithBlankSpaceConfig(skeleton_textViewShopName, shopName, blankSpaceConfig.shopName)
    }

    override fun initProductName(productName: String) {
        if (blankSpaceConfig.twoLinesProductName) {
            textViewProductName?.setLines(2)
            skeleton_textViewProductName?.setLines(2)
        }
        textViewProductName.setTextWithBlankSpaceConfig(skeleton_textViewProductName, productName, blankSpaceConfig.productName)
    }

    override fun initLabelDiscount(discountPercentage: String) {
        val isLabelDiscountVisible = getIsLabelDiscountVisible(discountPercentage)

        labelDiscount.configureVisibilityWithBlankSpaceConfigSkeleton(
                isLabelDiscountVisible, skeleton_labelDiscount, blankSpaceConfig.discountPercentage) {
            it.text = MethodChecker.fromHtml(discountPercentage)
        }
    }

    override fun initSlashedPrice(slashedPrice: String) {
        textViewSlashedPrice.configureVisibilityWithBlankSpaceConfigSkeleton(
                slashedPrice.isNotEmpty(), skeleton_textViewSlashedPrice, blankSpaceConfig.slashedPrice) {
            it.text = slashedPrice
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun initProductPrice(formattedPrice: String) {
        textViewPrice.setTextWithBlankSpaceConfig(skeleton_textViewPrice, formattedPrice, blankSpaceConfig.price)
    }

    override fun initShopBadgeList(shopBadgeList: List<ProductCardModel.ShopBadge>) {
        removeAllShopBadges()

        linearLayoutShopBadges.configureVisibilityWithBlankSpaceConfigSkeleton(
                hasAnyBadgesShown(shopBadgeList),
                skeleton_linearLayoutShopBadges,
                blankSpaceConfig.shopBadge) {
            loopBadgesListToLoadShopBadgeIcon(shopBadgeList)
        }
    }

    override fun initShopLocation(shopLocation: String) {
        textViewShopLocation.setTextWithBlankSpaceConfig(skeleton_textViewShopLocation, shopLocation, blankSpaceConfig.shopLocation)
    }

    override fun initRating(ratingCount: Int) {
        linearLayoutImageRating.configureVisibilityWithBlankSpaceConfigSkeleton(
                ratingCount > 0, skeleton_linearLayoutImageRating, blankSpaceConfig.ratingCount) {
            setRating(ratingCount)
        }
    }

    override fun initReview(reviewCount: Int) {
        textViewReviewCount.configureVisibilityWithBlankSpaceConfigSkeleton(
                reviewCount > 0, skeleton_textViewReviewCount, blankSpaceConfig.reviewCount) {
            it.text = getReviewCountFormattedAsText(reviewCount)
        }
    }

    override fun initLabelCredibility(ratingCount: Int, reviewCount: Int, labelCredibilityModel: ProductCardModel.Label) {
        val isLabelCredibilityVisible = isLabelCredibilityVisible(ratingCount, reviewCount, labelCredibilityModel)

        labelCredibility.configureVisibilityWithBlankSpaceConfigSkeleton(
                isLabelCredibilityVisible,
                skeleton_labelCredibility,
                blankSpaceConfig.labelCredibility) {
            it.text = labelCredibilityModel.title
            it.setLabelType(getLabelTypeFromString(labelCredibilityModel.type))
        }
    }

    override fun initLabelOffers(labelOffersModel: ProductCardModel.Label) {
        labelOffers.configureVisibilityWithBlankSpaceConfigSkeleton(
                labelOffersModel.title.isNotEmpty(),
                skeleton_labelOffers,
                blankSpaceConfig.labelOffers) {
            it.text = labelOffersModel.title
            it.setLabelType(getLabelTypeFromString(labelOffersModel.type))
        }
    }

    override fun initFreeOngkir(freeOngkir: ProductCardModel.FreeOngkir) {
        val shouldShowFreeOngkirImage = freeOngkir.isActive && freeOngkir.imageUrl.isNotEmpty()

        imageFreeOngkirPromo.configureVisibilityWithBlankSpaceConfigSkeleton(
                shouldShowFreeOngkirImage,
                skeleton_imageFreeOngkirPromo,
                blankSpaceConfig.freeOngkir) {
            it.loadImageRounded(freeOngkir.imageUrl)
        }
    }

    override fun initTopAdsIcon(isVisible: Boolean) {
        if (isVisible) {
            imageTopAds?.visibility = View.VISIBLE
            skeleton_imageTopAds?.visibility = View.VISIBLE
        }
        else if (blankSpaceConfig.topAdsIcon) {
            imageTopAds?.visibility = View.INVISIBLE
            skeleton_imageTopAds?.visibility = View.INVISIBLE
        }
        else {
            imageTopAds?.visibility = GONE
            skeleton_imageTopAds?.visibility = View.GONE
        }
    }

    private fun <T: View> T?.configureVisibilityWithBlankSpaceConfigSkeleton(isVisible: Boolean,
                                                                             viewSkeleton: View? = null,
                                                                             blankSpaceConfigValue:
                                                                      Boolean, action: (T) -> Unit) {
        if (this == null) return

        visibility = if (isVisible) {
            action(this)
            View.VISIBLE
        } else View.GONE

        viewSkeleton?.visibility = getViewNotVisibleWithBlankSpaceConfig(blankSpaceConfigValue)
    }

    private fun TextView?.setTextWithBlankSpaceConfig(viewSkeleton: TextView? = null, textValue: String, blankSpaceConfigValue: Boolean) {
        this?.configureVisibilityWithBlankSpaceConfigSkeleton(textValue.isNotEmpty(), viewSkeleton, blankSpaceConfigValue) {
            it.text = MethodChecker.fromHtml(textValue)
        }
    }

    internal fun getViewNotVisibleWithBlankSpaceConfig(blankSpaceConfigValue: Boolean): Int {
        return if (blankSpaceConfigValue) {
            View.INVISIBLE
        }
        else {
            View.GONE
        }
    }

    fun setFreeOngkirInvisible(isInvisible: Boolean){
        imageFreeOngkirPromo?.visibility =  if (isInvisible) View.INVISIBLE else View.VISIBLE
    }

    fun setLabelPreOrderInvisible(isInvisible: Boolean){
        label_pre_order?.visibility =  if (isInvisible) View.INVISIBLE else View.VISIBLE
    }

    fun setlabelDiscountInvisible(isInvisible: Boolean) {
        labelDiscount?.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
    }
}