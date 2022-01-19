package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import javax.inject.Inject


@DigitalPDPScope
class DigitalDenomMCCMGridUiMapper @Inject constructor() {

    fun mapCatalogDenom(catalogData: CatalogData): DenomMCCMModel {
        val productsMCCM = catalogData.product.dataCollections.filter {
            it.clusterType.contains(CLUSTER_MCCM_TYPE, true)
        }.firstOrNull()?.products

        val productsDenom = catalogData.product.dataCollections.filterNot {
            it.clusterType.contains(CLUSTER_MCCM_TYPE, true)
        }.firstOrNull()?.products

        val denomWidget =  DenomWidgetModel(
            mainTitle = catalogData.product.text,
            listDenomData = if (!productsDenom.isNullOrEmpty()){
                productsDenom.map {
                    DenomData(
                        title = it.attributes.desc,
                        price = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.promo?.newPrice ?: "0" else it.attributes.price,
                        pricePlain = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.promo?.newPricePlain ?: 0 else it.attributes.pricePlain.toIntOrZero(),
                        specialLabel = it.attributes.productLabels.firstOrNull() ?: "",
                        slashPrice = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.price  else "",
                        slashPricePlain = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.pricePlain.toIntOrZero() ?: 0 else 0
                    )
                }
            } else emptyList()
        )

        val mccmWidget =  DenomWidgetModel(
            mainTitle = catalogData.product.text,
            listDenomData = if (!productsDenom.isNullOrEmpty()){ //TODO Change to MCCM Data
                productsDenom.map {
                    DenomData(
                        title = it.attributes.desc,
                        discountLabel = "10%", //TODO Change to real data
                        price = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.promo?.newPrice ?: "0" else it.attributes.price,
                        pricePlain = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.promo?.newPricePlain ?: 0 else it.attributes.pricePlain.toIntOrZero(),
                        specialLabel = it.attributes.productLabels.firstOrNull() ?: "",
                        slashPrice = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.price  else "",
                        slashPricePlain = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.pricePlain.toIntOrZero() ?: 0 else 0
                    )
                }
            } else emptyList()
        )

        return DenomMCCMModel(denomWidget, mccmWidget)
    }

    companion object {
        const val CLUSTER_MCCM_TYPE = "MCCM"
    }
}