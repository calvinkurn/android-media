package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.ShopAdsWithSingleProductModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gm.common.R as gmcommonR
import com.tokopedia.shopwidget.R as shopwidgetR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopAdsSingleItemHorizontalLayout : BaseCustomView {

    private var shopBadge: ImageUnify? = null
    private var shopImage: ImageUnify? = null
    private var productImage: ImageUnify? = null
    private var bodyContainer: ConstraintLayout? = null
    private var merchantVoucher: Label? = null
    private var shopSlogan: Typography? = null
    private var productTitle: Typography? = null
    private var productDiscountPrice: Typography? = null
    private var productSlashedPrice: Typography? = null
    private var productDiscountPercent: Typography? = null
    private var productContainer: CardUnify2? = null
    private var productDiscountGroup: Group? = null

    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(context)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.shopads_single_item_horizontal_layout, this)
    }

    fun setShopProductModel(shopAdsWithSingleProductModel: ShopAdsWithSingleProductModel) {
        initViews()
        setShopAdsBackground(shopAdsWithSingleProductModel)
        setShopTitle(shopAdsWithSingleProductModel.shopName)
        loadBadge(shopAdsWithSingleProductModel)
        setShopImage(shopAdsWithSingleProductModel.shopImageUrl)
        setShopVoucher(shopAdsWithSingleProductModel.merchantVouchers)
        setSlogan(shopAdsWithSingleProductModel.slogan)
        setProductWidget(shopAdsWithSingleProductModel)
    }

    private fun initViews() {
        shopBadge = findViewById(R.id.shop_badge)
        shopImage = findViewById(R.id.headerShopImage)
        productImage = findViewById(R.id.product_image)
        bodyContainer = findViewById(R.id.container)
        merchantVoucher = findViewById(R.id.merchantVoucher)
        shopSlogan = findViewById(R.id.shop_desc)
        productTitle = findViewById(R.id.product_title)
        productDiscountPrice = findViewById(R.id.discounted_price)
        productSlashedPrice = findViewById(R.id.original_price)
        productContainer = findViewById(R.id.product_container)
        productDiscountPercent = findViewById(R.id.textRibbon)
        productDiscountGroup = findViewById(R.id.discount_percent_group)
    }

    private fun setProductWidget(shopAdsWithSingleProductModel: ShopAdsWithSingleProductModel) {
        shopAdsWithSingleProductModel.listItem?.let { product ->
            val productModel =
                getProductCardViewModel(product, shopAdsWithSingleProductModel.hasAddToCartButton)
            productImage?.loadImage(productModel.productImageUrl)
            productTitle?.text = MethodChecker.fromHtml(productModel.productName).toString()
            productDiscountPrice?.text = productModel.formattedPrice
            productSlashedPrice?.text = productModel.slashedPrice
            productSlashedPrice?.strikethrough()
            setDiscountPercent(productModel)

            shopAdsWithSingleProductModel.impressHolder?.let { impressHolder ->
                productContainer?.addOnImpressionListener(impressHolder){
                    shopAdsWithSingleProductModel.impressionListener?.onImpressionProductAdsItem(Int.ZERO, shopAdsWithSingleProductModel.listItem, shopAdsWithSingleProductModel.cpmData)
                    shopAdsWithSingleProductModel.impressionListener?.onImpressionHeadlineAdsItem(Int.ZERO, shopAdsWithSingleProductModel.cpmData)
                }
            }

            shopAdsWithSingleProductModel.impressHolder?.let { impressHolder ->
                shopImage?.addOnImpressionListener(impressHolder){
                    shopAdsWithSingleProductModel.impressionListener?.let {
                        it.onImpressionHeadlineAdsItem(Int.ZERO, shopAdsWithSingleProductModel.cpmData)
                        topAdsUrlHitter.hitImpressionUrl(
                            this::class.java.name,
                            shopAdsWithSingleProductModel.cpmData.cpm.cpmImage.fullUrl,
                            "",
                            "",
                            ""
                        )
                    }
                }
            }

            productContainer?.setOnClickListener{
                shopAdsWithSingleProductModel.topAdsBannerClickListener?.onBannerAdsClicked(
                    Int.ZERO,
                    product.applinks,
                    shopAdsWithSingleProductModel.cpmData
                )
                topAdsUrlHitter.hitClickUrl(
                    ShopAdsSingleItemVerticalLayout::class.java.simpleName,
                    shopAdsWithSingleProductModel.adsClickUrl,
                    String.EMPTY,
                    String.EMPTY,
                    String.EMPTY
                )
            }

            bodyContainer?.setOnClickListener {
                shopAdsWithSingleProductModel.topAdsBannerClickListener?.onBannerAdsClicked(
                    Int.ZERO,
                    shopAdsWithSingleProductModel.shopApplink,
                    shopAdsWithSingleProductModel.cpmData
                )
                topAdsUrlHitter.hitClickUrl(
                    ShopAdsSingleItemVerticalLayout::class.java.simpleName,
                    shopAdsWithSingleProductModel.adsClickUrl,
                    String.EMPTY,
                    String.EMPTY,
                    String.EMPTY
                )
            }
        }
    }

    private fun setDiscountPercent(productModel: ProductCardModel) {
        productDiscountPercent?.text = productModel.discountPercentage
        productDiscountGroup?.showWithCondition(productModel.discountPercentage.isNotEmpty())
    }

    private fun setSlogan(slogan: String) {
        shopSlogan?.text = slogan
    }

    private fun setShopVoucher(vouchers: MutableList<String>) {
        merchantVoucher?.text = vouchers.firstOrNull() ?: String.EMPTY
        merchantVoucher?.showWithCondition(vouchers.isNotEmpty())
    }

    private fun setShopAdsBackground(shopAdsWithSingleProductModel: ShopAdsWithSingleProductModel) {
        val colorCode = getBackgroundColor(shopAdsWithSingleProductModel)
        bodyContainer?.background =
            ContextCompat.getDrawable(context, colorCode)
        merchantVoucher?.setLabelType(if (shopAdsWithSingleProductModel.isPMPro || shopAdsWithSingleProductModel.isPowerMerchant) Label.GENERAL_GREEN else Label.HIGHLIGHT_LIGHT_GREEN)
    }

    private fun getBackgroundColor(shopAdsWithSingleProductModel: ShopAdsWithSingleProductModel): Int {
        return when {
            shopAdsWithSingleProductModel.isOfficial -> unifyprinciplesR.color.Unify_PN50
            shopAdsWithSingleProductModel.isPMPro -> unifyprinciplesR.color.Unify_GN50
            shopAdsWithSingleProductModel.isPowerMerchant -> unifyprinciplesR.color.Unify_GN50
            else -> unifyprinciplesR.color.Unify_NN50
        }
    }

    private fun setShopTitle(shopName: String) {
        val title = findViewById<Typography>(R.id.shop_name)
        title.text = shopName
        title.showWithCondition(shopName.isNotEmpty())
    }

    private fun setShopImage(shopImageUrl: String) {
        shopImage?.loadImageCircle(shopImageUrl)
    }

    private fun loadBadge(shopAdsWithSingleProductModel: ShopAdsWithSingleProductModel) {
        val isImageShopBadgeVisible = getIsImageShopBadgeVisible(shopAdsWithSingleProductModel)
        shopBadge?.shouldShowWithAction(isImageShopBadgeVisible) {
            when {
                shopAdsWithSingleProductModel.isOfficial -> shopBadge?.loadImage(R.drawable.ic_official_store)
                shopAdsWithSingleProductModel.isPMPro -> shopBadge?.loadImage(shopwidgetR.drawable.shopwidget_ic_pm_pro)
                shopAdsWithSingleProductModel.isPowerMerchant -> shopBadge?.loadImage(
                    gmcommonR.drawable.ic_power_merchant
                )
            }
        }
    }

    private fun getIsImageShopBadgeVisible(shopAdsWithSingleProductModel: ShopAdsWithSingleProductModel): Boolean {
        return shopAdsWithSingleProductModel.isOfficial
            || shopAdsWithSingleProductModel.isPMPro
            || shopAdsWithSingleProductModel.isPowerMerchant
    }

    private fun getProductCardViewModel(
        product: Product,
        hasAddToCartButton: Boolean
    ): ProductCardModel {
        return ProductCardModel(
            productImageUrl = product.imageProduct.imageUrl,
            productName = product.name,
            discountPercentage = if (product.campaign.discountPercentage != Int.ZERO) "${product.campaign.discountPercentage}%" else "",
            slashedPrice = product.campaign.originalPrice,
            formattedPrice = product.priceFormat,
            reviewCount = product.countReviewFormat.toIntOrZero(),
            ratingCount = product.productRating,
            ratingString = product.productRatingFormat,
            countSoldRating = product.headlineProductRatingAverage,
            freeOngkir = ProductCardModel.FreeOngkir(
                product.freeOngkir.isActive,
                product.freeOngkir.imageUrl
            ),
            labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                product.labelGroupList.map {
                    add(ProductCardModel.LabelGroup(it.position, it.title, it.type))
                }
            },
            hasAddToCartButton = hasAddToCartButton,
        )
    }
}
