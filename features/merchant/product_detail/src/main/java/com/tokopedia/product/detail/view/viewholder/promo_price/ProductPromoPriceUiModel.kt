package com.tokopedia.product.detail.view.viewholder.promo_price

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.pdplayout.Price
import com.tokopedia.product.detail.common.data.model.promoprice.PromoPriceUiModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductPromoPriceUiModel(
    val type: String = "",
    val name: String = "",
    //1 for Promo Price
    //2 for Normal Promo
    var priceComponentType: Int = 0,
    var promoPriceData: PromoPriceUiModel? = null,
    var normalPromoUiModel: Price? = null
) : DynamicPdpDataModel {
    override fun type(): String = type
    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return false
    }

    override fun newInstance(): DynamicPdpDataModel = copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder: ImpressHolder = ImpressHolder()

}

