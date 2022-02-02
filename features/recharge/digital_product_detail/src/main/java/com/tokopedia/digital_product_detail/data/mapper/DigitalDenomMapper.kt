package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogProductInputMultiTab
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.data.model.data.RechargeCatalogDataCollection
import com.tokopedia.digital_product_detail.data.model.data.RechargeProduct
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import javax.inject.Inject

@DigitalPDPScope
class DigitalDenomMapper @Inject constructor() {

    fun mapMultiTabFullDenom(inputMultiTab: DigitalCatalogProductInputMultiTab): InputMultiTabDenomModel {
        val productsDenom = inputMultiTab.multitabData.productInputs.firstOrNull()?.product
        val (dataCollectionProduct, dataCollectionMCCM) = getMainDataCollections(inputMultiTab)

        return InputMultiTabDenomModel(
            getDenomFullMapper(productsDenom?.text, dataCollectionProduct),
            getDenomFullMapper(mappingMCCMTitle(dataCollectionMCCM?.firstOrNull()?.clusterType),
                dataCollectionProduct, true),
            inputMultiTab.multitabData.productInputs.firstOrNull()?.filterTagComponents ?: emptyList()
        )
    }


    fun mapMultiTabGridDenom(inputMultiTab: DigitalCatalogProductInputMultiTab): DenomMCCMModel {
        val productsDenom = inputMultiTab.multitabData.productInputs.firstOrNull()?.product
        val (dataCollectionProduct, dataCollectionMCCM) = getMainDataCollections(inputMultiTab)

        return DenomMCCMModel(
            getDenomGridMapper(productsDenom?.text, dataCollectionProduct),
            getDenomGridMapper(mappingMCCMTitle(dataCollectionMCCM?.firstOrNull()?.clusterType),
                dataCollectionMCCM, true)
        )
    }

    private fun getMainDataCollections(inputMultiTab: DigitalCatalogProductInputMultiTab): Pair<List<RechargeCatalogDataCollection>?, List<RechargeCatalogDataCollection>?> {
        val productsDenom = inputMultiTab.multitabData.productInputs.firstOrNull()?.product
        val dataCollections = productsDenom?.dataCollections

        val dataCollectionMCCM = dataCollections?.filter {
            it.clusterType.contains(CLUSTER_MCCM_TYPE, true)
        }

        val dataCollectionProduct = dataCollections?.filterNot {
            it.clusterType.contains(CLUSTER_MCCM_TYPE, true)
        }
        return Pair(dataCollectionProduct, dataCollectionMCCM)
    }

    private fun getDenomGridMapper(title: String?, rechargeDataCollections: List<RechargeCatalogDataCollection>?, isMCCM: Boolean = false): DenomWidgetModel {
        val denomList: MutableList<DenomData> = mutableListOf()
        if (!rechargeDataCollections.isNullOrEmpty()) {
            rechargeDataCollections.forEach {
                denomList.addAll(it.products.map {
                    rechargeToDenomMapperGrid(it, isMCCM)
                })
            }
        }

        return DenomWidgetModel(title?: "", listDenomData = denomList)
    }

    private fun getDenomFullMapper(title:String?, rechargeDataCollections: List<RechargeCatalogDataCollection>?, isMCCM: Boolean = false): DenomWidgetModel {
      val denomList: MutableList<DenomData> = mutableListOf()
        if (!rechargeDataCollections.isNullOrEmpty()) {
            rechargeDataCollections.forEach {
                denomList.addAll(it.products.map {
                    rechargeToDenomMapperFull(it, isMCCM)
                })
            }
        }

        return DenomWidgetModel(title?: "", listDenomData = denomList)
    }
    
    private fun rechargeToDenomMapperGrid(rechargeProduct: RechargeProduct, isMCCM: Boolean = false): DenomData {
        return rechargeProduct?.let {
            DenomData(
                id = it.id,
                promoStatus = if (it.attributes.productPromo != null) PROMO_STATUS_TRUE else PROMO_STATUS_FALSE,
                categoryId = it.attributes.categoryId,
                operatorId = it.attributes.operatorId,
                isSpecialPromo = if (it.attributes.productLabels.isNotEmpty())
                    it.attributes.productLabels[0].equals(SPECIAL_PROMO_LABEL, true)
                else false,
                title = it.attributes.desc,
                price = if(!it.attributes.productPromo?.newPrice.isNullOrEmpty()) it.attributes.productPromo?.newPrice ?: EMPTY_PRICE else it.attributes.price,
                pricePlain = if(it.attributes.productPromo?.newPricePlain.isMoreThanZero()) it.attributes.productPromo?.newPricePlain ?: EMPTY_PRICE_PLAIN else it.attributes.pricePlain,
                specialLabel = it.attributes.productLabels.firstOrNull() ?: "",
                slashPrice = if(!it.attributes.productPromo?.newPrice.isNullOrEmpty()) it.attributes.price  else "",
                slashPricePlain = if(it.attributes.productPromo?.newPricePlain.isMoreThanZero()) it.attributes.pricePlain else EMPTY_PRICE_PLAIN,
                discountLabel = if (isMCCM) "10%" else "", // todo add from gql
            )
        }
    }

    private fun rechargeToDenomMapperFull(rechargeProduct: RechargeProduct, isMCCM: Boolean = false): DenomData {
        return rechargeProduct.let {
            DenomData(
                id = it.id,
                promoStatus = if (it.attributes.productPromo != null) PROMO_STATUS_TRUE else PROMO_STATUS_FALSE,
                categoryId = it.attributes.categoryId,
                operatorId = it.attributes.operatorId,
                isSpecialPromo = if (it.attributes.productLabels.isNotEmpty())
                    it.attributes.productLabels[0].equals(
                        SPECIAL_PROMO_LABEL,
                        true
                    )
                else false,
                title = it.attributes.desc,
                price = if (!it.attributes.productPromo?.newPrice.isNullOrEmpty()) it.attributes.productPromo?.newPrice
                    ?: EMPTY_PRICE else it.attributes.price,
                pricePlain = if (it.attributes.productPromo?.newPricePlain.isMoreThanZero()) it.attributes.productPromo?.newPricePlain
                    ?: EMPTY_PRICE_PLAIN else it.attributes.pricePlain,
                specialLabel = it.attributes.productLabels.firstOrNull() ?: "",
                slashPrice = if (!it.attributes.productPromo?.newPrice.isNullOrEmpty()) it.attributes.price else "",
                slashPricePlain = if (it.attributes.productPromo?.newPricePlain.isMoreThanZero()) it.attributes.pricePlain else EMPTY_PRICE_PLAIN,
                isShowChevron = true,
                quotaInfo = "30 GB", //todo add from gql
                expiredDays = "30 Days", // todo add from gql
                discountLabel = if (isMCCM) "10%" else "", // todo add from gql,
                productDescriptions = it.attributes.productDescriptions
            )
        }
    }

    private fun mappingMCCMTitle(mccmTitle: String?): String {
        return if (!mccmTitle.isNullOrEmpty())
            mccmTitle.split(MCCM_LIMITER).get(1)
        else ""
    }

    companion object {
        const val CLUSTER_MCCM_TYPE = "MCCM"
        const val MCCM_LIMITER = "_"
        const val SPECIAL_PROMO_LABEL: String = "Traktiran Pengguna Baru"
        const val EMPTY_PRICE = "0"
        const val EMPTY_PRICE_PLAIN = 0

        const val PROMO_STATUS_TRUE = "1"
        const val PROMO_STATUS_FALSE = "0"

    }
}