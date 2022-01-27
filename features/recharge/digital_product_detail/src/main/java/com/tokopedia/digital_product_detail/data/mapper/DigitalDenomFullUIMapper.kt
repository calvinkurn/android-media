package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogProductInputMultiTab
import com.tokopedia.digital_product_detail.data.model.data.InputMultiTabDenomModel
import com.tokopedia.digital_product_detail.data.model.data.RechargeCatalogDataCollection
import com.tokopedia.digital_product_detail.data.model.data.RechargeProduct
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import javax.inject.Inject

@DigitalPDPScope
class DigitalDenomFullUIMapper @Inject constructor() {

    fun mapMultiTabDenom(inputMultiTab: DigitalCatalogProductInputMultiTab): InputMultiTabDenomModel {
        val productsDenom = inputMultiTab.multitabData.productInputs.firstOrNull()?.
        product
        val dataCollectionDenom = productsDenom?.dataCollections

        return InputMultiTabDenomModel(
            getDenomFullMapper(productsDenom?.text, dataCollectionDenom),
            getMCCMFullMapper( "TITLE_MCCM", //todo title mccm
                dataCollectionDenom
            ),
            inputMultiTab
        )
    }

    private fun getDenomFullMapper(title:String?, rechargeDataCollections: List<RechargeCatalogDataCollection>?): DenomWidgetModel {
      val denomList: MutableList<DenomData> = mutableListOf()
        if (!rechargeDataCollections.isNullOrEmpty()) {
            rechargeDataCollections.forEach {
                denomList.addAll(it.products.map {
                    rechargeToDenomMapper(it)
                })
            }
        }

        return DenomWidgetModel(title?: "", listDenomData = denomList)
    }

    private fun getMCCMFullMapper(title:String?, rechargeDataCollections: List<RechargeCatalogDataCollection>?): DenomWidgetModel {
        val denomList: MutableList<DenomData> = mutableListOf()
        if (!rechargeDataCollections.isNullOrEmpty()) {
            rechargeDataCollections.filter {
                mccmTitles.contains(it.name)
            }.forEach {
                denomList.addAll(it.products.map {
                    rechargeToDenomMapper(it)
                })
            }
        }

        return DenomWidgetModel(title?: "", listDenomData = denomList)
    }

    private fun rechargeToDenomMapper(rechargeProduct: RechargeProduct): DenomData {
        return rechargeProduct.let {
            DenomData(
                id = it.id,
                promoStatus = if (it.attributes.productPromo != null) PROMO_STATUS_TRUE else DigitalDenomMCCMGridUiMapper.PROMO_STATUS_FALSE,
                categoryId = it.attributes.categoryId,
                operatorId = it.attributes.operatorId,
                isSpecialPromo = if (it.attributes.productLabels.isNotEmpty())
                    it.attributes.productLabels[0].equals(
                        DigitalDenomMCCMGridUiMapper.SPECIAL_PROMO_LABEL,
                        true
                    )
                else false,
                title = it.attributes.desc,
                price = if (!it.attributes.productPromo?.newPrice.isNullOrEmpty()) it.attributes.productPromo?.newPrice
                    ?: DigitalDenomMCCMGridUiMapper.EMPTY_PRICE else it.attributes.price,
                pricePlain = if (it.attributes.productPromo?.newPricePlain.isMoreThanZero()) it.attributes.productPromo?.newPricePlain
                    ?: DigitalDenomMCCMGridUiMapper.EMPTY_PRICE_PLAIN else it.attributes.pricePlain,
                specialLabel = it.attributes.productLabels.firstOrNull() ?: "",
                slashPrice = if (!it.attributes.productPromo?.newPrice.isNullOrEmpty()) it.attributes.price else "",
                slashPricePlain = if (it.attributes.productPromo?.newPricePlain.isMoreThanZero()) it.attributes.pricePlain else DigitalDenomMCCMGridUiMapper.EMPTY_PRICE_PLAIN,
                isShowChevron = true, //todo add from gql
                quotaInfo = "30 GB", //todo add from gql
                expiredDays = "30 Days", // todo add from gql
            )
        }
    }


    companion object {
        const val EMPTY_PRICE = "0"
        const val EMPTY_PRICE_PLAIN = 0

        const val PROMO_STATUS_TRUE = "1"
        const val PROMO_STATUS_FALSE = "0"
        private val mccmTitles = listOf("Promo Spesial Buat Kamu", "Promo Pulsa Hari Ini") //todo map mccm
    }
}