package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.pdp.fintech.view.FintechPriceDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class FintechWidgetDataModel(
    val name: String = "",
    val type: String = "",
    var productId: String = "",
    var categoryId: String? = null,
    var idToPriceUrlMap: HashMap<String, FintechPriceDataModel> = HashMap(),
    var isLoggedIn: Boolean = false,
    var shopId: String = "",
    ): DynamicPdpDataModel
{
    override fun type() = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory) = typeFactory.type(this)

    override fun name() = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is FintechWidgetDataModel && (newData.productId == this.productId && newData.idToPriceUrlMap == this.idToPriceUrlMap && newData.isLoggedIn == this.isLoggedIn)
    }

    override fun newInstance() = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder = ImpressHolder()

}
