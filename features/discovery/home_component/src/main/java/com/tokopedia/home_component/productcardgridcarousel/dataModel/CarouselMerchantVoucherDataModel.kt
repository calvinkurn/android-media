package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by dhaba
 */
data class CarouselMerchantVoucherDataModel(
    val shopId: String,
    val shopName: String,
    val benefit: String,
    val benefitPrice: String,
    val totalOtherCoupon: String,
    val iconBadge : String,
    val imageProduct: String,
    val shopAppLink: String,
    val productAppLink: String,
    val bannerId: String,
    val positionWidget : String,
    val headerName: String,
    val userId: String,
    val couponCode: String,
    val couponType: String,
    val creativeName: String,
    val productId: String,
    val productName: String,
    val productVariant: String,
    val productPrice: String,
    val buType: String,
    val topAds: String,
    val carousel: String,
    val recommendationType: String,
    val recomPageName: String,
    val productBrand: String,
    val catNameLevel1: String,
    val catNameLevel2: String,
    val catNameLevel3: String,
    val impressHolder: ImpressHolder = ImpressHolder(),
    val merchantVoucherComponentListener: MerchantVoucherComponentListener
) : Visitable<CommonCarouselProductCardTypeFactory> {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}