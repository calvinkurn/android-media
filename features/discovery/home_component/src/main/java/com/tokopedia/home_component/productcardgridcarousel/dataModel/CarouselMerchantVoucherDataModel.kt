package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

/**
 * Created by dhaba
 */
data class CarouselMerchantVoucherDataModel(
    val shopName: String,
    val benefit: String,
    val benefitPrice: String,
    val totalOtherCoupon: String,
    val iconBadge : String,
    val imageProduct: String,
    val shopAppLink: String,
    val productAppLink: String,
    val merchantVoucherComponentListener: MerchantVoucherComponentListener
) : Visitable<CommonCarouselProductCardTypeFactory> {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}