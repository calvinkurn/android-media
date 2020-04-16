package com.tokopedia.rechargegeneral.model.mapper

import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.common.topupbills.data.product.CatalogProductInput
import com.tokopedia.rechargegeneral.model.*
import com.tokopedia.rechargegeneral.presentation.viewmodel.RechargeGeneralViewModel
import javax.inject.Inject

class RechargeGeneralDynamicInputMapper @Inject constructor() {

    fun mapDynamicInputToProductData(dynamicInput: RechargeGeneralDynamicInput): RechargeGeneralProductData {
        val productData = RechargeGeneralProductData()
        productData.isShowingProduct = dynamicInput.isShowingProduct
        productData.needEnquiry = dynamicInput.needEnquiry

        val listEnquiryField = mutableListOf<RechargeGeneralProductInput>()
        dynamicInput.enquiryFields.map {
            if (it.paramName == RechargeGeneralViewModel.PARAM_PRODUCT) {
                productData.product = mapProduct(it)
            } else {
                listEnquiryField.add(mapEnquiryFields(it))
            }
        }
        productData.enquiryFields = listEnquiryField
        return productData
    }

    private fun mapEnquiryFields(dynamicField: RechargeGeneralDynamicField): RechargeGeneralProductInput {
        val productInput = RechargeGeneralProductInput()
        productInput.name = dynamicField.name
        productInput.text = dynamicField.text
        productInput.help = dynamicField.help
        productInput.paramName = dynamicField.paramName
        productInput.style = dynamicField.style
        productInput.value = dynamicField.value
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
        return productInput
    }

    private fun mapProduct(dynamicField: RechargeGeneralDynamicField): RechargeGeneralProductItemData {
        val productItem = RechargeGeneralProductItemData()
        productItem.name = dynamicField.name
        productItem.text = dynamicField.text

        val listDataCollection = mutableListOf<CatalogProductData.DataCollection>()
        dynamicField.dataCollections.map {
            val dataCollection = CatalogProductData.DataCollection()
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

        productItem.dataCollections = listDataCollection

        return productItem
    }
}