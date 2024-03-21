package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory

data class ProductMiniShopWidgetDataModel(
        val type: String = "",
        val name: String = "",
        var shopName: String = "",
        var shopLocation: String = "",
        var shopAva: String = "",
        var shopBadge: String = ""
) : DynamicPdpDataModel {

    override fun type() = type

    override fun type(typeFactory: ProductDetailAdapterFactory) = typeFactory.type(this)

    override fun name() = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is ProductMiniShopWidgetDataModel &&
                newData.shopName == shopName &&
                newData.shopLocation == shopLocation &&
                newData.shopAva == shopAva &&
                newData.shopBadge == shopBadge
    }

    override fun newInstance() = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder: ImpressHolder
        get() = ImpressHolder()
}
