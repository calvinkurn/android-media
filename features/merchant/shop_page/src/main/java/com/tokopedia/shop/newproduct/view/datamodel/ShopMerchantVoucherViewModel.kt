package com.tokopedia.shop.newproduct.view.datamodel

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory

import java.util.ArrayList

/**
 * Created by zulfikarrahman on 1/16/18.
 */

class ShopMerchantVoucherViewModel(merchantVoucherViewModels: List<MerchantVoucherViewModel>) : BaseShopProductViewModel {

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
