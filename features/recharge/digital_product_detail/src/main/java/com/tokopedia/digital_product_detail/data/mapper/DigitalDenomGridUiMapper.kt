package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import javax.inject.Inject


@DigitalPDPScope
class DigitalDenomMCCMGridUiMapper @Inject constructor() {

    fun mapCatalogDenom(catalogData: CatalogData): DenomMCCMModel {
        val dataCollectionMCCM = catalogData.product.dataCollections.filter {
            it.clusterType.contains(CLUSTER_MCCM_TYPE, true)
        }.firstOrNull()
        val productsMCCM = dataCollectionMCCM?.products

        val productsDenom = catalogData.product.dataCollections.filterNot {
            it.clusterType.contains(CLUSTER_MCCM_TYPE, true)
        }.firstOrNull()?.products

        val denomWidget =  DenomWidgetModel(
            mainTitle = catalogData.product.text,
            listDenomData = if (!productsDenom.isNullOrEmpty()){
                productsDenom.map {
                    DenomData(
                        id = it.id,
                        promoStatus = if (it.attributes.promo != null) PROMO_STATUS_TRUE else PROMO_STATUS_FALSE,
                        categoryId = it.attributes.categoryId,
                        operatorId = it.attributes.operatorId,
                        isSpecialPromo = if (it.attributes.productLabels.isNotEmpty())
                            it.attributes.productLabels[0].equals(SPECIAL_PROMO_LABEL, true)
                        else false,
                        title = it.attributes.desc,
                        price = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.promo?.newPrice ?: EMPTY_PRICE else it.attributes.price,
                        pricePlain = if(it.attributes.promo?.newPricePlain.isMoreThanZero()) it.attributes.promo?.newPricePlain ?: EMPTY_PRICE_PLAIN else it.attributes.pricePlain.toIntOrZero(),
                        specialLabel = it.attributes.productLabels.firstOrNull() ?: "",
                        slashPrice = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.price  else "",
                        slashPricePlain = if(it.attributes.promo?.newPricePlain.isMoreThanZero()) it.attributes.pricePlain.toIntOrZero() ?: EMPTY_PRICE_PLAIN else EMPTY_PRICE_PLAIN
                    )
                }
            } else emptyList()
        )

        val mccmWidget =  DenomWidgetModel(
            mainTitle = mappingMCCMTitle(dataCollectionMCCM?.clusterType),
            listDenomData = if (!productsMCCM.isNullOrEmpty()){
                productsMCCM.map {
                    DenomData(
                        id = it.id,
                        promoStatus = if (it.attributes.promo != null) PROMO_STATUS_TRUE else PROMO_STATUS_FALSE,
                        categoryId = it.attributes.categoryId,
                        operatorId = it.attributes.operatorId,
                        isSpecialPromo = if (it.attributes.productLabels.isNotEmpty())
                            it.attributes.productLabels[0].equals(SPECIAL_PROMO_LABEL, true)
                        else false,
                        title = it.attributes.desc,
                        discountLabel = "10%", //TODO Change to real data
                        price = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.promo?.newPrice ?: EMPTY_PRICE else it.attributes.price,
                        pricePlain = if(it.attributes.promo?.newPricePlain.isMoreThanZero()) it.attributes.promo?.newPricePlain ?: EMPTY_PRICE_PLAIN else it.attributes.pricePlain.toIntOrZero(),
                        specialLabel = it.attributes.productLabels.firstOrNull() ?: "",
                        slashPrice = if(!it.attributes.promo?.newPrice.isNullOrEmpty()) it.attributes.price  else "",
                        slashPricePlain = if(it.attributes.promo?.newPricePlain.isMoreThanZero()) it.attributes.pricePlain.toIntOrZero() ?: EMPTY_PRICE_PLAIN else EMPTY_PRICE_PLAIN
                    )
                }
            } else emptyList()
        )

        return DenomMCCMModel(denomWidget, mccmWidget)
    }

    private fun mappingMCCMTitle(mccmTitle: String?): String{
        return if (!mccmTitle.isNullOrEmpty())
            mccmTitle.split(MCCM_LIMITER).get(1)
            else ""
    }

    companion object {
        const val CLUSTER_MCCM_TYPE = "MCCM"
        const val SPECIAL_PROMO_LABEL: String = "Traktiran Pengguna Baru"
        const val PROMO_STATUS_TRUE = "1"
        const val PROMO_STATUS_FALSE = "0"

        const val EMPTY_PRICE = "0"
        const val EMPTY_PRICE_PLAIN = 0
        const val MCCM_LIMITER = "_"
    }
}