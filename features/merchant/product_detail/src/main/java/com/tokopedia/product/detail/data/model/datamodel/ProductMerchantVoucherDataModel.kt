package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

class ProductMerchantVoucherDataModel(
        val type: String = "",
        val name: String = "",
        var voucherData: ArrayList<MerchantVoucherViewModel> = arrayListOf(),
        var shouldRenderInitialData : Boolean = true
) : DynamicPdpDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
}