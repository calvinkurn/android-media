package com.tokopedia.atc_common.domain.mapper

import com.google.gson.Gson
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.atc_common.data.model.response.AddToCartOccExternalGqlResponse
import com.tokopedia.atc_common.data.model.response.AddToCartOccGqlResponse
import com.tokopedia.atc_common.data.model.response.DataResponse
import com.tokopedia.atc_common.data.model.response.ocs.AddToCartOcsGqlResponse
import com.tokopedia.atc_common.data.model.response.ocs.OcsDataResponse
import com.tokopedia.atc_common.domain.model.response.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-09-11.
 */

class AddToCartDataMapper @Inject constructor() {

    fun mapAddToCartResponse(addToCartGqlResponse: AddToCartGqlResponse): AddToCartDataModel {
        return addToCartGqlResponse.addToCartResponse.let {
            val errorReporter = ErrorReporterModel()
            errorReporter.eligible = it.errorReporter.eligible

            val errorReporterTextModel = ErrorReporterTextModel()
            errorReporterTextModel.submitTitle = it.errorReporter.texts.submitTitle
            errorReporterTextModel.submitDescription = it.errorReporter.texts.submitDescription
            errorReporterTextModel.submitButton = it.errorReporter.texts.submitButton
            errorReporterTextModel.cancelButton = it.errorReporter.texts.cancelButton
            errorReporter.texts = errorReporterTextModel

            val dataModel = mapDataModel(it.data)

            val addToCartDataModel = AddToCartDataModel()
            addToCartDataModel.status = it.status
            addToCartDataModel.errorMessage = it.errorMessage
            addToCartDataModel.data = dataModel
            addToCartDataModel.errorReporter = errorReporter
            addToCartDataModel.responseJson = Gson().toJson(addToCartGqlResponse)

            addToCartDataModel
        }
    }

    fun mapAddToCartOccResponse(addToCartOccGqlResponse: AddToCartOccGqlResponse): AddToCartDataModel {
        return addToCartOccGqlResponse.addToCartOccResponse.let {
            val addToCartDataModel = AddToCartDataModel()
            addToCartDataModel.status = it.status
            addToCartDataModel.errorMessage = it.errorMessage
            addToCartDataModel.data = DataModel().apply {
                success = it.data.success
                message = it.data.message
                cartId = it.data.detail.cartId.toString()
                productId = it.data.detail.productId
                quantity = it.data.detail.quantity
                notes = it.data.detail.notes
                shopId = it.data.detail.shopId
                customerId = it.data.detail.customerId
                warehouseId = it.data.detail.warehouseId
                isTradeIn = it.data.detail.isTradeIn
            }

            addToCartDataModel
        }
    }

    fun mapAddToCartOccResponse(addToCartOccGqlResponse: AddToCartOccExternalGqlResponse): AddToCartDataModel {
        return addToCartOccGqlResponse.addToCartOccResponse.let {
            val addToCartDataModel = AddToCartDataModel()
            addToCartDataModel.status = it.status
            addToCartDataModel.errorMessage = it.errorMessage
            addToCartDataModel.data = DataModel().apply {
                success = it.data.success
                message = it.data.message
                cartId = it.data.detail.cartId.toString()
                productId = it.data.detail.productId
                quantity = it.data.detail.quantity
                notes = it.data.detail.notes
                shopId = it.data.detail.shopId
                customerId = it.data.detail.customerId
                warehouseId = it.data.detail.warehouseId
                isTradeIn = it.data.detail.isTradeIn
            }

            addToCartDataModel
        }
    }

    fun mapAddToCartOcsResponse(addToCartOcsGqlResponse: AddToCartOcsGqlResponse): AddToCartDataModel {
        return addToCartOcsGqlResponse.addToCartResponse.let {
            val dataModel = mapDataModelOcs(it.data)

            val addToCartDataModel = AddToCartDataModel()
            addToCartDataModel.status = it.status
            addToCartDataModel.errorMessage = it.errorMessage
            addToCartDataModel.data = dataModel

            addToCartDataModel
        }
    }

    private fun mapDataModelOcs(it: OcsDataResponse): DataModel {
        val dataModel = DataModel()
        dataModel.cartId = it.ocsData.cartId
        dataModel.productId = it.ocsData.productId
        dataModel.quantity = it.ocsData.quantity
        dataModel.notes = it.ocsData.notes
        dataModel.shopId = it.ocsData.shopId
        dataModel.customerId = it.ocsData.customerId
        dataModel.warehouseId = it.ocsData.warehouseId
        dataModel.trackerAttribution = it.ocsData.trackerAttribution
        dataModel.trackerListName = it.ocsData.trackerListName
        dataModel.ucUtParam = it.ocsData.ucUtParam
        dataModel.isTradeIn = it.ocsData.isTradeIn

        val ovoValidaation = OvoValidationDataModel(
                it.ocsData.ovoValidation.status,
                it.ocsData.ovoValidation.applink,
                OvoInsufficientBalance(
                        it.ocsData.ovoValidation.ovoInsufficientBalance.title,
                        it.ocsData.ovoValidation.ovoInsufficientBalance.description,
                        OvoInsufficientDetails(
                                it.ocsData.ovoValidation.ovoInsufficientBalance.details.productPrice,
                                it.ocsData.ovoValidation.ovoInsufficientBalance.details.shippingEstimation,
                                it.ocsData.ovoValidation.ovoInsufficientBalance.details.ovoBalance,
                                it.ocsData.ovoValidation.ovoInsufficientBalance.details.topupBalance
                        ),
                        OvoInsufficientButton(
                                OvoInsufficientTopup(
                                        it.ocsData.ovoValidation.ovoInsufficientBalance.buttons.topupButton.text,
                                        it.ocsData.ovoValidation.ovoInsufficientBalance.buttons.topupButton.applink,
                                        it.ocsData.ovoValidation.ovoInsufficientBalance.buttons.topupButton.enable
                                ),
                                OvoInsufficientTopup(
                                        it.ocsData.ovoValidation.ovoInsufficientBalance.buttons.otherMethodButton.text,
                                        it.ocsData.ovoValidation.ovoInsufficientBalance.buttons.otherMethodButton.applink,
                                        it.ocsData.ovoValidation.ovoInsufficientBalance.buttons.otherMethodButton.enable
                                )
                        )
                )
        )

        dataModel.success = it.success
        dataModel.message = it.message
        dataModel.ovoValidationDataModel = ovoValidaation
        dataModel.refreshPrerequisitePage = it.refreshPrerequisitePage

        return dataModel
    }

    private fun mapDataModel(it: DataResponse): DataModel {
        val dataModel = DataModel()
        dataModel.success = it.success
        dataModel.cartId = it.cartId
        dataModel.productId = it.productId
        dataModel.quantity = it.quantity
        dataModel.notes = it.notes
        dataModel.shopId = it.shopId
        dataModel.customerId = it.customerId
        dataModel.warehouseId = it.warehouseId
        dataModel.trackerAttribution = it.trackerAttribution
        dataModel.trackerListName = it.trackerListName
        dataModel.ucUtParam = it.ucUtParam
        dataModel.isTradeIn = it.isTradeIn
        dataModel.message = it.message
        return dataModel
    }

}