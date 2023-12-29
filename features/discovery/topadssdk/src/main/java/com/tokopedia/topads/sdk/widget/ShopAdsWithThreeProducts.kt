package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_8
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_9
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmShop
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.ShopAdsWithThreeProductModel
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.adapter.ShopWidgetAdapter
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.factory.ShopWidgetFactoryImpl
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.listener.ShopWidgetAddToCartClickListener
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.model.*
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.widget.TopAdsBannerView.Companion.escapeHTML
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.abs

private const val PRODUCT_CARD_COUNT_THREE = 3

class ShopAdsWithThreeProducts : BaseCustomView {

    private var shopImage: ImageUnify? = null
    private var shopBadge: ImageUnify? = null
    private var shopWidgetImage: ImageUnify? = null
    private var shopName: Typography? = null
    private var adsDescription: Typography? = null
    private var shopAdsText: Typography? = null
    private var linearLayoutMerchantVoucher: LinearLayout? = null
    private var btnLihat: Typography? = null
    private var recyclerView: RecyclerView? = null
    private var shopWidgetAdapter: ShopWidgetAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var firstCardBackground: ConstraintLayout? = null
    private var bodyContainer: ConstraintLayout? = null
    private var shopWidgetRootLayout: ConstraintLayout? = null
    private var widgetHeader: View? = null

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
        View.inflate(context, R.layout.topads_with_three_product_layout, this)
    }

    fun setWidgetModel(shopAdsWithThreeProductModel: ShopAdsWithThreeProductModel) {
        initVars()
        initAdapterAndList(
            shopAdsWithThreeProductModel.topAdsBannerClickListener,
            shopAdsWithThreeProductModel.impressionListener,
            shopAdsWithThreeProductModel.shopWidgetAddToCartClickListener
        )
        setHeader(shopAdsWithThreeProductModel)
        setBackgroundAsFirstCard(
            shopAdsWithThreeProductModel.items.cpm.cta,
            shopAdsWithThreeProductModel.items.cpm.cpmShop.slogan,
            shopAdsWithThreeProductModel.shopWidgetImageUrl
        )
        setWidget(
            shopAdsWithThreeProductModel.items,
            shopAdsWithThreeProductModel.shopApplink,
            shopAdsWithThreeProductModel.adsClickUrl,
            shopAdsWithThreeProductModel.hasAddToCartButton
        )
        setShopWidgetBackground(
            shopAdsWithThreeProductModel.items.cpm.cpmShop,
            shopAdsWithThreeProductModel.variant
        )
        setShopImpression(
            shopAdsWithThreeProductModel.items,
            shopAdsWithThreeProductModel.impressionListener
        )
    }

    private fun setShopImpression(
        items: CpmData,
        impressionListener: TopAdsItemImpressionListener?
    ) {
        items.cpm?.cpmShop?.imageShop?.let { it1 ->
            shopImage?.addOnImpressionListener(it1) {
                impressionListener?.let {
                    it.onImpressionHeadlineAdsItem(Int.ZERO, items)
                    topAdsUrlHitter.hitImpressionUrl(
                        this::class.java.name,
                        items.cpm.cpmImage.fullUrl,
                        "",
                        "",
                        ""
                    )
                }
            }
        }
    }

    private fun initAdapterAndList(
        topAdsBannerClickListener: TopAdsBannerClickListener?,
        impressionListener: TopAdsItemImpressionListener?,
        shopWidgetAddToCartClickListener: ShopWidgetAddToCartClickListener?
    ) {
        shopWidgetAdapter =
            ShopWidgetAdapter(
                ShopWidgetFactoryImpl(
                    shopWidgetAddToCartClickListener,
                    impressionListener,
                    topAdsBannerClickListener
                )
            )
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.layoutManager = layoutManager

        recyclerView?.adapter = shopWidgetAdapter
        recyclerView?.addOnScrollListener(getParallaxEffect())
    }

    private fun setBackgroundAsFirstCard(cta: String, slogan: String, shopWidgetImageUrl: String) {
        firstCardBackground?.alpha = 1f
        btnLihat?.text = cta
        adsDescription?.text = escapeHTML(slogan)
        setShopWidgetImage(shopWidgetImageUrl)
    }

    private fun setWidget(
        cpmData: CpmData,
        shopApplink: String,
        adsClickUrl: String,
        hasAddToCartButton: Boolean
    ) {
        val list = getList(cpmData, shopApplink, adsClickUrl, hasAddToCartButton)
        shopWidgetAdapter?.submitList(list)
    }

    private fun setShopWidgetBackground(cpmShop: CpmShop, variant: Int) {
        if (variant == LAYOUT_9) {
            shopWidgetRootLayout?.background = null
            when {
                cpmShop.isOfficial -> {
                    bodyContainer?.background =
                        ContextCompat.getDrawable(context, R.drawable.purple_gradient)
                }
                cpmShop.isPMPro -> {
                    bodyContainer?.background =
                        ContextCompat.getDrawable(context, R.drawable.blue_one_gradient)
                }
                cpmShop.isPowerMerchant -> {
                    bodyContainer?.background =
                        ContextCompat.getDrawable(context, R.drawable.green_gradient)
                }
                else -> {
                    bodyContainer?.background =
                        ContextCompat.getDrawable(context, R.drawable.blue_two_gradient)
                }
            }
        } else {
            bodyContainer?.background = null
            when {
                cpmShop.isOfficial -> {
                    shopWidgetRootLayout?.background =
                        ContextCompat.getDrawable(context, R.drawable.purple_gradient)
                }
                cpmShop.isPMPro -> {
                    shopWidgetRootLayout?.background =
                        ContextCompat.getDrawable(context, R.drawable.blue_one_gradient)
                }
                cpmShop.isPowerMerchant -> {
                    shopWidgetRootLayout?.background =
                        ContextCompat.getDrawable(context, R.drawable.green_gradient)
                }
                else -> {
                    shopWidgetRootLayout?.background =
                        ContextCompat.getDrawable(context, R.drawable.blue_two_gradient)
                }
            }
        }

    }

    private fun getList(
        cpmData: CpmData,
        shopApplink: String,
        adsClickUrl: String,
        hasAddToCartButton: Boolean
    ): List<ShopWidgetItem> {
        val items: MutableList<ShopWidgetItem> = mutableListOf()
        items.add(EmptyShopCardModel(cpmData, shopApplink, adsClickUrl))
        if (cpmData.cpm?.cpmShop?.products?.isNotEmpty() == true) {
            val productCardModelList: ArrayList<ProductCardModel> =
                getProductCardModels(cpmData.cpm.cpmShop.products, hasAddToCartButton)
            for (i in 0 until productCardModelList.size) {
                if (i < PRODUCT_CARD_COUNT_THREE) {
                    val product = cpmData.cpm.cpmShop.products[i]
                    val model = ProductItemModel(
                        cpmData = cpmData,
                        productCardModel = productCardModelList[i],
                        applinks = cpmData.cpm.cpmShop.products[i].applinks,
                        mUrl = cpmData.cpm.cpmShop.products[i].image.m_url,
                        adsClickUrl = cpmData.cpm.cpmShop.products[i].imageProduct.imageClickUrl,
                        productId = product.id,
                        productName = product.name,
                        productMinOrder = product.productMinimumOrder,
                        productCategory = product.categoryBreadcrumb,
                        productPrice = product.priceFormat,
                        shopId = cpmData.cpm.cpmShop.id
                    )
                    items.add(model)
                }
            }
            if (productCardModelList.size < PRODUCT_CARD_COUNT_THREE) {
                items.add(ShowMoreItemModel(cpmData, shopApplink, adsClickUrl))
            }
        } else {
            repeat(PRODUCT_CARD_COUNT_THREE) { items.add(ShopWidgetShimmerUiModel()) }
        }
        return items
    }

    private fun getProductCardModels(
        products: List<Product>,
        hasAddToCartButton: Boolean
    ): ArrayList<ProductCardModel> {
        return ArrayList<ProductCardModel>().apply {
            products.map {
                add(getProductCardViewModel(it, hasAddToCartButton))
            }
        }
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
            addToCartButtonType = UnifyButton.Type.MAIN
        )
    }

    private fun initVars() {
        shopName = findViewById(R.id.shopName)
        adsDescription = findViewById(R.id.adsDescription)
        shopAdsText = findViewById(R.id.shopAdsText)
        shopBadge = findViewById(R.id.shopBadge)
        shopImage = findViewById(R.id.headerShopImage)
        btnLihat = findViewById(R.id.btnLihat)
        firstCardBackground = findViewById(R.id.viewVish)
        shopWidgetImage = findViewById(R.id.shopWidgetImage)
        bodyContainer = findViewById(R.id.bodyContainer)
        shopWidgetRootLayout = findViewById(R.id.shopWidgetRootLayout)
        linearLayoutMerchantVoucher = findViewById(R.id.linearLayoutMerchantVoucher)
        recyclerView = findViewById(R.id.productList)
        widgetHeader = findViewById(R.id.widgetHeader)

        initialTranslationOnBackground()
    }

    private fun initialTranslationOnBackground() {
        firstCardBackground?.translationX = 0f
    }

    private fun getParallaxEffect(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager?.findFirstVisibleItemPosition() == Int.ZERO && dx != Int.ZERO) {
                    val firstView =
                        layoutManager?.let { it.findViewByPosition(it.findFirstVisibleItemPosition()) }
                    firstView?.let {
                        val distanceFromLeft = it.left
                        val translateX = distanceFromLeft * 0.2f
                        if (translateX <= Int.ZERO) {
                            firstCardBackground?.translationX = translateX
                            if (distanceFromLeft <= Int.ZERO) {
                                val itemSize = it.width.toFloat()
                                val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                                firstCardBackground?.alpha = Int.ONE - alpha
                            }
                        } else {
                            firstCardBackground?.translationX = 0f
                            firstCardBackground?.alpha = 1f
                        }
                    }
                }
            }
        }
    }

    private fun setHeader(shopAdsWithThreeProductModel: ShopAdsWithThreeProductModel) {
        val textColor = getTextColor(shopAdsWithThreeProductModel.variant)
        setAdsTextColor(shopAdsWithThreeProductModel.variant)
        setShopName(shopAdsWithThreeProductModel.shopName, textColor)
        setShopBadge(shopAdsWithThreeProductModel.shopBadge)
        setShopImage(shopAdsWithThreeProductModel.shopImageUrl)
        setMerchantVoucher(shopAdsWithThreeProductModel.merchantVouchers)
        widgetHeader?.setOnClickListener {
            shopAdsWithThreeProductModel.topAdsBannerClickListener?.onBannerAdsClicked(
                1,
                shopAdsWithThreeProductModel.shopApplink,
                shopAdsWithThreeProductModel.items
            )
            topAdsUrlHitter.hitClickUrl(
                this::class.java.name,
                shopAdsWithThreeProductModel.items.adClickUrl,
                "",
                "",
                ""
            )
        }
    }

    private fun setShopWidgetImage(shopWidgetImageUrl: String) {
        shopWidgetImage?.loadImage(shopWidgetImageUrl)
    }

    private fun setAdsTextColor(variant: Int) {
        if (variant == LAYOUT_8) {
            shopAdsText?.setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                )
            )
        } else {
            shopAdsText?.setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                )
            )
        }
    }

    private fun setMerchantVoucher(merchantVouchers: MutableList<String>) {
        linearLayoutMerchantVoucher?.removeAllViews()
        if (merchantVouchers.isNotEmpty()) linearLayoutMerchantVoucher?.show()

        merchantVouchers.forEachIndexed { index, voucher ->
            val isFirstItem = index == Int.ZERO
            val labelVoucher = createLabelVoucher(context, voucher, isFirstItem)

            linearLayoutMerchantVoucher?.addView(labelVoucher)
        }
    }

    private fun createLabelVoucher(
        context: Context?,
        voucher: String,
        isFirstItem: Boolean
    ): View? {
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.marginStart = if (isFirstItem) Int.ZERO else 4.toPx()

        val labelVoucher = context?.let { Label(it) }

        labelVoucher?.setLabelType(Label.GENERAL_LIGHT_GREEN)
        labelVoucher?.text = voucher
        labelVoucher?.layoutParams = layoutParams

        return labelVoucher
    }

    private fun setShopImage(shopImageUrl: String) {
        ImageHandler.loadImageRounded(context, shopImage, shopImageUrl, 100f)
    }

    private fun setShopName(shopName: String, textColor: Int) {
        this.shopName?.text = MethodChecker.fromHtml(shopName)
        this.shopName?.setTextColor(textColor)
    }

    private fun setShopBadge(shopBadgeUrl: String) {
        if (shopBadgeUrl.isNotEmpty()) {
            shopBadge?.loadImage(shopBadgeUrl)
            shopBadge?.show()
        } else shopBadge?.hide()
    }

    private fun getTextColor(variant: Int): Int {
        return if (variant == LAYOUT_8) ContextCompat.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN0
        )
        else ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)

    }
}