package com.tokopedia.rechargegeneral.model.mapper

import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.common_digital.common.presentation.model.DigitalDppoConsent
import com.tokopedia.rechargegeneral.model.RechargeGeneralDynamicField
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductInput
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductItemData
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralDppoConsentUiModel
import javax.inject.Inject

class RechargeGeneralMapper @Inject constructor() {

    fun mapDynamicInputToProductData(dynamicField: RechargeGeneralDynamicField): RechargeGeneralProductInput {
        val productInput = RechargeGeneralProductInput()
        productInput.name = dynamicField.name
        productInput.text = dynamicField.text
        productInput.help = dynamicField.help
        productInput.paramName = dynamicField.paramName
        productInput.style = dynamicField.style
        productInput.placeholder = dynamicField.placeholder

        val listValidation = mutableListOf<CatalogProductInput.Validation>()
        dynamicField.validations.map {
            val dataValidation = CatalogProductInput.Validation()
            dataValidation.id = it.id
            dataValidation.message = it.message
            dataValidation.rule = it.rule
            dataValidation.title = it.title

            listValidation.add(dataValidation)
        }
        productInput.validations = listValidation

        val listDataCollection = mutableListOf<CatalogProductInput.DataCollection>()
        dynamicField.dataCollections.map {
            val dataCollection = CatalogProductInput.DataCollection()
            dataCollection.name = it.name

            val products = mutableListOf<CatalogProduct>()
            it.products.map { product ->
                val catalogProduct = CatalogProduct()
                catalogProduct.id = product.id

                val attributes = CatalogProduct.Attributes()
                attributes.desc = product.attributes.desc
                attributes.detail = product.attributes.detail
                attributes.detailCompact = product.attributes.detailCompact
                attributes.detailUrl = product.attributes.detailUrl
                attributes.detailUrlText = product.attributes.detailUrlText
                attributes.price = product.attributes.price
                attributes.pricePlain = product.attributes.pricePlain
                attributes.productLabels = product.attributes.productLabels
                attributes.price = product.attributes.price

                val promo = CatalogProduct.Promo()
                product.attributes.promo?.run {
                    promo.bonusText = this.bonusText
                    promo.id = this.id
                    promo.newPrice = this.newPrice
                    promo.newPricePlain = this.newPricePlain
                    promo.tag = this.tag
                    promo.terms = this.terms
                    promo.valueText = this.valueText
                }
                attributes.promo = promo

                catalogProduct.attributes = product.attributes

                products.add(catalogProduct)
            }
            dataCollection.products = products

            listDataCollection.add(dataCollection)
        }

        productInput.dataCollections = listDataCollection

        return productInput
    }

    fun mapInputToProductItemData(dynamicField: RechargeGeneralDynamicField): RechargeGeneralProductItemData {
        val productInput = RechargeGeneralProductItemData()
        productInput.name = dynamicField.name
        productInput.text = dynamicField.text
        productInput.help = dynamicField.help
        productInput.paramName = dynamicField.paramName
        productInput.style = dynamicField.style
        productInput.placeholder = dynamicField.placeholder

        val listValidation = mutableListOf<CatalogProductInput.Validation>()
        dynamicField.validations.map {
            val dataValidation = CatalogProductInput.Validation()
            dataValidation.id = it.id
            dataValidation.message = it.message
            dataValidation.rule = it.rule
            dataValidation.title = it.title

            listValidation.add(dataValidation)
        }
        productInput.validations = listValidation

        val listDataCollection = mutableListOf<CatalogProductInput.DataCollection>()
        dynamicField.dataCollections.map {
            val dataCollection = CatalogProductInput.DataCollection()
            dataCollection.name = it.name

            val products = mutableListOf<CatalogProduct>()
            it.products.map { product ->
                val catalogProduct = CatalogProduct()
                catalogProduct.id = product.id

                val attributes = CatalogProduct.Attributes()
                attributes.desc = product.attributes.desc
                attributes.detail = product.attributes.detail
                attributes.detailCompact = product.attributes.detailCompact
                attributes.detailUrl = product.attributes.detailUrl
                attributes.detailUrlText = product.attributes.detailUrlText
                attributes.price = product.attributes.price
                attributes.pricePlain = product.attributes.pricePlain
                attributes.productLabels = product.attributes.productLabels
                attributes.price = product.attributes.price

                val promo = CatalogProduct.Promo()
                product.attributes.promo?.run {
                    promo.bonusText = this.bonusText
                    promo.id = this.id
                    promo.newPrice = this.newPrice
                    promo.newPricePlain = this.newPricePlain
                    promo.tag = this.tag
                    promo.terms = this.terms
                    promo.valueText = this.valueText
                }
                attributes.promo = promo

                catalogProduct.attributes = product.attributes

                products.add(catalogProduct)
            }
            dataCollection.products = products

            listDataCollection.add(dataCollection)
        }

        productInput.dataCollections = listDataCollection

        return productInput
    }

    fun mapDppoConsentToUiModel(data: DigitalDppoConsent): RechargeGeneralDppoConsentUiModel {
        return RechargeGeneralDppoConsentUiModel(
            description = data.persoData.items.getOrNull(Int.ZERO)?.title ?: ""
        )
    }
}
