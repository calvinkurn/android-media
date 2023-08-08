package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogProductInputMultiTab
import com.tokopedia.digital_product_detail.data.model.data.DigitalCustomAttributes
import com.tokopedia.digital_product_detail.data.model.data.DigitalDigiPersoGetPersonalizedItem
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.data.model.data.PersoRecommendationData
import com.tokopedia.digital_product_detail.data.model.data.PersoRecommendationItem
import com.tokopedia.digital_product_detail.data.model.data.RechargeCatalogDataCollection
import com.tokopedia.digital_product_detail.data.model.data.RechargeProduct
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationWidgetModel
import javax.inject.Inject

@DigitalPDPScope
class DigitalDenomMapper @Inject constructor() {

    fun mapMultiTabFullDenom(inputMultiTab: DigitalCatalogProductInputMultiTab, isRefresheedFilter: Boolean): InputMultiTabDenomModel {
        val productsDenom = inputMultiTab.multitabData.productInputs.firstOrNull()?.product
        val (dataCollectionProduct, dataCollectionMCCM) = getMainDataCollections(inputMultiTab)

        return InputMultiTabDenomModel(
            getDenomFullMapper(productsDenom?.text, dataCollectionProduct),
            inputMultiTab.multitabData.productInputs.firstOrNull()?.otherComponents ?: emptyList(),
            inputMultiTab.multitabData.productInputs.firstOrNull()?.filterTagComponents ?: emptyList(),
            isRefresheedFilter
        )
    }


    fun mapMultiTabGridDenom(inputMultiTab: DigitalCatalogProductInputMultiTab): DenomMCCMModel {
        val productsDenom = inputMultiTab.multitabData.productInputs.firstOrNull()?.product
        val (dataCollectionProduct, dataCollectionMCCM) = getMainDataCollections(inputMultiTab)

        return DenomMCCMModel(
            getDenomGridMapper(productsDenom?.text, dataCollectionProduct),
            getDenomGridMapper(dataCollectionMCCM?.firstOrNull()?.name, dataCollectionMCCM)
        )
    }

    fun mapTokenListrikDenom(inputMultiTab: DigitalCatalogProductInputMultiTab): DenomWidgetModel {
        val productsDenom = inputMultiTab.multitabData.productInputs.firstOrNull()?.product
        return getDenomGridMapper(productsDenom?.text, getProductDataCollection(productsDenom?.dataCollections))
    }

    fun mapTagihanListrikProduct(inputMultiTab: DigitalCatalogProductInputMultiTab): RechargeProduct? {
        val product = inputMultiTab.multitabData.productInputs.firstOrNull()?.product
        return getProductDataCollection(product?.dataCollections)?.firstOrNull()?.products?.firstOrNull()
    }

    fun mapDigiPersoToRecommendation(data: PersoRecommendationData, isBigRecommendation: Boolean): RecommendationWidgetModel {
        return RecommendationWidgetModel(
            title = data.title,
            recommendations = data.items.map { digiPersoToRecommendationCard(it, isBigRecommendation) }
        )
    }

    fun mapDigiPersoToMCCMProducts(data: PersoRecommendationData): DenomWidgetModel {
        return if (!data.items.isNullOrEmpty()) {
            val firstProduct = data.items.first()
            val denomList: MutableList<DenomData> = mutableListOf()
            denomList.addAll(data.items.map {
                digiPersoToMCCMItems(it)
            })
            val denomWidgetModel =  DenomWidgetModel(
                mainTitle = data.title,
                isHorizontalMCCM = firstProduct.mediaUrlType.equals(MCCM_LAYOUT_TYPE_HORIZONTAL,true),
                imageBackgroundUrl = firstProduct.mediaURL,
                imageBackgroundUrlDarkMode = firstProduct.mediaURLDarkMode,
                listDenomData = denomList
            )
            return denomWidgetModel
        } else {
            return DenomWidgetModel()
        }
    }

    private fun getMainDataCollections(inputMultiTab: DigitalCatalogProductInputMultiTab): Pair<List<RechargeCatalogDataCollection>?, List<RechargeCatalogDataCollection>?> {
        val dataCollections = inputMultiTab.multitabData.productInputs.firstOrNull()?.product?.dataCollections
        return Pair(getProductDataCollection(dataCollections), getMCCMDataCollection(dataCollections))
    }

    private fun getProductDataCollection(dataCollections: List<RechargeCatalogDataCollection>?): List<RechargeCatalogDataCollection>? {
       return dataCollections?.filterNot {
            it.clusterType.contains(CLUSTER_MCCM_TYPE, true)
        }
    }

    private fun getMCCMDataCollection(dataCollections: List<RechargeCatalogDataCollection>?): List<RechargeCatalogDataCollection>? {
        return dataCollections?.filter {
            it.clusterType.contains(CLUSTER_MCCM_TYPE, true)
        }
    }

    private fun getDenomGridMapper(title: String?, rechargeDataCollections: List<RechargeCatalogDataCollection>?): DenomWidgetModel {
        val denomList: MutableList<DenomData> = mutableListOf()
        if (!rechargeDataCollections.isNullOrEmpty()) {
            rechargeDataCollections.forEach {
                denomList.addAll(it.products.map {
                    rechargeToDenomMapperGrid(it)
                })
            }
        }

        return DenomWidgetModel(title?: "", listDenomData = denomList)
    }

    private fun getDenomFullMapper(title:String?, rechargeDataCollections: List<RechargeCatalogDataCollection>?): DenomWidgetModel {
      val denomList: MutableList<DenomData> = mutableListOf()
        if (!rechargeDataCollections.isNullOrEmpty()) {
            rechargeDataCollections.forEach {
                denomList.addAll(it.products.map {
                    rechargeToDenomMapperFull(it)
                })
            }
        }

        return DenomWidgetModel(title?: "", listDenomData = denomList)
    }
    
    private fun rechargeToDenomMapperGrid(rechargeProduct: RechargeProduct): DenomData {
        return rechargeProduct.let {
            DenomData(
                id = it.id,
                status = it.attributes.status,
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
                discountLabel = it.attributes.productPromo?.discount ?: "",
                activePeriod = getMapCustomAttributes(it.attributes.customAttributes, PRODUCT_PULSA_EXPIRE)
            )
        }
    }

    private fun rechargeToDenomMapperFull(rechargeProduct: RechargeProduct): DenomData {
        return rechargeProduct.let {
            DenomData(
                id = it.id,
                status = it.attributes.status,
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
                isShowChevron = !it.attributes.productDescriptions.isNullOrEmpty(),
                quotaInfo = getMapCustomAttributes(it.attributes.customAttributes, QUOTA_NAME_KEY),
                expiredDays = getMapCustomAttributes(it.attributes.customAttributes, EXPIRED_DAYS_NAME_KEY),
                discountLabel = it.attributes.productPromo?.discount ?: "",
                productDescriptions = it.attributes.productDescriptions,
                greenLabel = getMapCustomAttributes(it.attributes.customAttributes, PRODUCT_DESC_TICKER_KEY)
            )
        }
    }

    private fun getMapCustomAttributes(customAttributes: List<DigitalCustomAttributes>?, keyName: String): String {
        return customAttributes?.filter {
            it.name.equals(keyName)
        }?.firstOrNull()?.value ?: ""
    }


    private fun digiPersoToRecommendationCard(
        data: PersoRecommendationItem,
        isBigRecommendation: Boolean
    ): RecommendationCardWidgetModel {
        return data.let {
            RecommendationCardWidgetModel(
                if (isBigRecommendation) RecommendationCardEnum.BIG else RecommendationCardEnum.SMALL,
                imageUrl = it.mediaURL,
                clientNumber = it.trackingData.clientNumber,
                title = it.title,
                price = it.price,
                discount = it.discount,
                slashPrice = it.slashedPrice,
                appUrl = it.appLink,
                id = it.trackingData.productId,
                categoryId = it.trackingData.categoryId,
                productId = it.trackingData.productId,
                operatorId = it.trackingData.operatorId,
                productType = if (isBigRecommendation) it.label1 else "",
                productExpired = if (isBigRecommendation) it.label2 else "",
                specialLabel = if (isBigRecommendation) it.label3 else "",
                itemType = it.trackingData.itemType
            )
        }
    }

    private fun digiPersoToMCCMItems(perso: PersoRecommendationItem): DenomData {
        return perso.let {
            DenomData(
                id = it.trackingData.productId,
                categoryId = it.trackingData.categoryId,
                operatorId = it.trackingData.operatorId,
                isSpecialPromo = true,
                title = it.title,
                price = it.price,
                pricePlain = it.pricePlain.toInt(),
                slashPrice = it.slashedPrice,
                slashPricePlain = it.slashedPricePlain.toInt(),
                isShowChevron = !it.descriptions.isNullOrEmpty(),
                quotaInfo = it.label1,
                expiredDays = it.label2,
                discountLabel = it.discount,
                productDescriptions = it.descriptions,
                itemType = it.trackingData.itemType,
                specialLabel = it.campaignLabelText
            )
        }
    }

    companion object {
        const val MCCM_LAYOUT_TYPE_HORIZONTAL = "horizontal"
        const val CLUSTER_MCCM_TYPE = "MCCM"
        const val SPECIAL_PROMO_LABEL: String = "Traktiran Pengguna Baru"
        const val EMPTY_PRICE = "0"
        const val EMPTY_PRICE_PLAIN = 0

        const val PROMO_STATUS_TRUE = "1"
        const val PROMO_STATUS_FALSE = "0"

        const val QUOTA_NAME_KEY = "product_paket_data_kuota"
        const val EXPIRED_DAYS_NAME_KEY = "product_paket_data_expire"
        const val PRODUCT_DESC_TICKER_KEY = "product_detail_ticker"
        const val PRODUCT_PULSA_EXPIRE = "product_pulsa_expire"
    }
}
