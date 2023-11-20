package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.ShopAdsWithThreeProductModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.shopwidget.R as shopwidgetR
import com.tokopedia.gm.common.R as gmcommonR

class ShopAdsSingleItemVerticalLayout : BaseCustomView {

    private var productShopBadge: ImageUnify? = null
    private var productImage: ImageUnify? = null
    private var shopImage: ImageUnify? = null

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

    fun setShopProductModel(shopAdsWithThreeProductModel: ShopAdsWithThreeProductModel) {
        initViews()
        initShopProductTitle(shopAdsWithThreeProductModel)
        initShopProductItem(shopAdsWithThreeProductModel)
    }

    private fun initViews(){
        productShopBadge = findViewById<ImageUnify>(R.id.shop_badge)
        productImage = findViewById<ImageUnify>(R.id.product_image)
        shopImage = findViewById<ImageUnify>(R.id.headerShopImage)
    }

    private fun initShopProductTitle(shopAdsWithThreeProductModel: ShopAdsWithThreeProductModel) {
        val title  = findViewById<Typography>(R.id.shop_name)
        title.text = shopAdsWithThreeProductModel.shopName
        title.showWithCondition(shopAdsWithThreeProductModel.shopName.isNotEmpty())
    }

    private fun initShopProductItem(shopAdsWithThreeProductModel: ShopAdsWithThreeProductModel) {
        loadBadge(shopAdsWithThreeProductModel)
        shopImage?.loadImageCircle(shopAdsWithThreeProductModel.shopImageUrl)
    }

    private fun loadBadge(shopAdsWithThreeProductModel: ShopAdsWithThreeProductModel) {
        val isImageShopBadgeVisible = getIsImageShopBadgeVisible(shopAdsWithThreeProductModel)
        productShopBadge?.shouldShowWithAction(isImageShopBadgeVisible) {
            when {
                shopAdsWithThreeProductModel.isOfficial -> productShopBadge?.loadImage(R.drawable.ic_official_store)
                shopAdsWithThreeProductModel.isPMPro -> productShopBadge?.loadImage(shopwidgetR.drawable.shopwidget_ic_pm_pro)
                shopAdsWithThreeProductModel.isPowerMerchant -> productShopBadge?.loadImage(gmcommonR.drawable.ic_power_merchant)
            }
        }
    }

    private fun getIsImageShopBadgeVisible(shopAdsWithThreeProductModel: ShopAdsWithThreeProductModel): Boolean {
        return shopAdsWithThreeProductModel.isOfficial
            || shopAdsWithThreeProductModel.isPMPro
            || shopAdsWithThreeProductModel.isPowerMerchant
    }
}
