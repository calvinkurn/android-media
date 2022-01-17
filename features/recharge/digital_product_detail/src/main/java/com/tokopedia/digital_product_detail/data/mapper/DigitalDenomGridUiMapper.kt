package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import javax.inject.Inject


@DigitalPDPScope
class DigitalDenomGridUiMapper @Inject constructor() {

    fun mapCatalogDenom(catalogData: CatalogData): DenomWidgetModel {
        val products = catalogData.product.dataCollections.first().products

        val denomWidget =  DenomWidgetModel(
            mainTitle = catalogData.product.text,
            listDenomData = products.map {
                DenomData(
                    title = it.attributes.desc,
                    price = it.attributes.price,
                    specialLabel = it.attributes.productLabels.firstOrNull() ?: "",
                    slashPrice = it.attributes.promo?.newPrice ?: ""
                )
            }
        )

        return denomWidget
    }
}