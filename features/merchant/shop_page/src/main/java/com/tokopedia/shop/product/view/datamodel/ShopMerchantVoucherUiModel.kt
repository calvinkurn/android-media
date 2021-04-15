package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.common.data.model.MerchantVoucherCouponUiModel
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

/**
 * Created by zulfikarrahman on 1/16/18.
 */

class ShopMerchantVoucherUiModel(val data: MerchantVoucherCouponUiModel? = null) : BaseShopProductViewModel {
    override fun type(typeFactory: ShopProductAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}
