package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.ShopAdsWithSingleProductModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gm.common.R as gmcommonR
import com.tokopedia.shopwidget.R as shopwidgetR

class ShopAdsSingleItemHorizontalLayout : BaseCustomView {

    private var productShopBadge: ImageUnify? = null
    private var shopImage: ImageUnify? = null
    private var bodyContainer: ConstraintLayout? = null
    private var merchantVoucher: Label? = null
    private var shopSlogan: Typography? = null
    private var productCardGridView: ProductCardGridView? = null

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
        productShopBadge = findViewById(R.id.shop_badge)
        shopImage = findViewById(R.id.headerShopImage)
        bodyContainer = findViewById(R.id.container)
        merchantVoucher = findViewById(R.id.merchantVoucher)
        shopSlogan = findViewById(R.id.shop_desc)
        productCardGridView = findViewById(R.id.product_item)
    }
    
    private fun setProductWidget(shopAdsWithSingleProductModel: ShopAdsWithSingleProductModel) {
        shopAdsWithSingleProductModel.listItem?.let {
            val productModel = getProductCardViewModel(it, shopAdsWithSingleProductModel.hasAddToCartButton)
            productCardGridView?.applyCarousel()
            productCardGridView?.setProductModel(productModel)
        }
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
    }

    private fun getBackgroundColor(shopAdsWithSingleProductModel: ShopAdsWithSingleProductModel): Int {
        if (shopAdsWithSingleProductModel.isOfficial) {
            return R.drawable.purple_gradient
        } else if (shopAdsWithSingleProductModel.isPMPro) {
            return R.drawable.blue_one_gradient
        } else if (shopAdsWithSingleProductModel.isPowerMerchant) {
            return R.drawable.green_gradient
        } else {
            return R.drawable.blue_two_gradient
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
        productShopBadge?.shouldShowWithAction(isImageShopBadgeVisible) {
            when {
                shopAdsWithSingleProductModel.isOfficial -> productShopBadge?.loadImage(R.drawable.ic_official_store)
                shopAdsWithSingleProductModel.isPMPro -> productShopBadge?.loadImage(shopwidgetR.drawable.shopwidget_ic_pm_pro)
                shopAdsWithSingleProductModel.isPowerMerchant -> productShopBadge?.loadImage(
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
