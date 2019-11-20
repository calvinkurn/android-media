package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

class ProductMerchantVoucherDataModel(
        val type: String = "",
        val name: String = "",
        val dataLayout: List<ComponentData> = listOf(),
        var voucherData: ArrayList<MerchantVoucherViewModel> = arrayListOf()
) : DynamicPDPDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
}