package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

/**
 * Created by zulfikarrahman on 1/16/18.
 */

class ShopMerchantVoucherUiModel(merchantVoucherViewModels: List<MerchantVoucherViewModel>) : BaseShopProductViewModel {

    var shopMerchantVoucherViewModelArrayList: List<MerchantVoucherViewModel>? = null
        private set

    init {
        setMerchantVoucherViewModels(merchantVoucherViewModels)
    }

    fun setMerchantVoucherViewModels(merchantVoucherViewModels: List<MerchantVoucherViewModel>?) {
        if (merchantVoucherViewModels == null) {
            this.shopMerchantVoucherViewModelArrayList = mutableListOf()
        } else {
            this.shopMerchantVoucherViewModelArrayList = merchantVoucherViewModels
        }
    }

    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
