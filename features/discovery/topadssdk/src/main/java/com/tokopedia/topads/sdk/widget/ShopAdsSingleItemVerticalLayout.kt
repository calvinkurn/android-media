package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.ShopAdsWithSingleProductModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.shopwidget.R as shopwidgetR
import com.tokopedia.gm.common.R as gmcommonR

class ShopAdsSingleItemVerticalLayout : BaseCustomView {

    private var productShopBadge: ImageUnify? = null
    private var productImage: ImageUnify? = null
    private var shopImage: ImageUnify? = null
    private var bodyContainer: ConstraintLayout? = null
    private var merchantVoucher: Label? = null
    private var shopSlogan: Typography? = null

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
        View.inflate(context, R.layout.shopads_single_item_vertical_layout, this)
    }

    fun setShopProductModel(shopAdsWithSingleProductModel: ShopAdsWithSingleProductModel) {
        initViews()
        setShopAdsBackground(shopAdsWithSingleProductModel)
        setShopTitle(shopAdsWithSingleProductModel.shopName)
        loadBadge(shopAdsWithSingleProductModel)
        setShopImage(shopAdsWithSingleProductModel.shopImageUrl)
        setShopVoucher(shopAdsWithSingleProductModel.merchantVouchers)
        setSlogan(shopAdsWithSingleProductModel.slogan)
    }

    private fun initViews() {
        productShopBadge = findViewById(R.id.shop_badge)
        shopImage = findViewById(R.id.headerShopImage)
        bodyContainer = findViewById(R.id.container)
        merchantVoucher = findViewById(R.id.merchantVoucher)
        shopSlogan = findViewById(R.id.shop_desc)
        productImage = findViewById(R.id.product_image)
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
}
