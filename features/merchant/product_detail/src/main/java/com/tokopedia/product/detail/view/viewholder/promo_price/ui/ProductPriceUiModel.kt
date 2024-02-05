package com.tokopedia.product.detail.view.viewholder.promo_price.ui

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.pdplayout.Price
import com.tokopedia.product.detail.common.data.model.promoprice.PromoPriceUiModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.TabletPosition
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductPriceUiModel(
    val type: String = "",
    val name: String = "",
    var priceComponentType: Int = 0,
    var promoPriceData: PromoPriceUiModel? = null,
    var normalPromoUiModel: Price? = null,
    var normalPriceBoUrl: String = "",
    var promoIdsString: List<String> = listOf()
) : DynamicPdpDataModel {

    override val tabletSectionPosition: TabletPosition
        get() = TabletPosition.LEFT

    override fun type(): String = type
    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductPriceUiModel) {
            priceComponentType == newData.priceComponentType &&
                normalPromoUiModel == newData.normalPromoUiModel &&
                normalPriceBoUrl == newData.normalPriceBoUrl &&
                promoIdsString == newData.promoIdsString &&
                promoPriceData == newData.promoPriceData
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel = copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder: ImpressHolder = ImpressHolder()
}
