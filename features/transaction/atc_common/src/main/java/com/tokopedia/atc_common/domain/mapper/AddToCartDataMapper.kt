package com.tokopedia.atc_common.domain.mapper

import com.google.gson.Gson
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.atc_common.data.model.response.AddToCartOccExternalGqlResponse
import com.tokopedia.atc_common.data.model.response.AddToCartOccGqlResponse
import com.tokopedia.atc_common.data.model.response.DataResponse
import com.tokopedia.atc_common.data.model.response.occ.AddToCartOccMultiGqlResponse
import com.tokopedia.atc_common.data.model.response.occ.DataOccMultiResponse
import com.tokopedia.atc_common.data.model.response.occ.DetailOccMultiResponse
import com.tokopedia.atc_common.data.model.response.ocs.AddToCartOcsGqlResponse
import com.tokopedia.atc_common.data.model.response.ocs.OcsDataResponse
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiCartData
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiData
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.model.response.ErrorReporterModel
import com.tokopedia.atc_common.domain.model.response.ErrorReporterTextModel
import com.tokopedia.atc_common.domain.model.response.OvoInsufficientBalance
import com.tokopedia.atc_common.domain.model.response.OvoInsufficientButton
import com.tokopedia.atc_common.domain.model.response.OvoInsufficientDetails
import com.tokopedia.atc_common.domain.model.response.OvoInsufficientTopup
import com.tokopedia.atc_common.domain.model.response.OvoValidationDataModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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

    fun mapAddToCartOccMultiResponse(response: AddToCartOccMultiGqlResponse): AddToCartOccMultiDataModel {
        return AddToCartOccMultiDataModel(
                errorMessage = response.response.errorMessage,
                status = response.response.status,
                data = mapAddToCartOccMultiData(response.response.data)
        )
    }

    private fun mapAddToCartOccMultiData(dataResponse: DataOccMultiResponse): AddToCartOccMultiData {
        return AddToCartOccMultiData(
                success = dataResponse.success,
                message = dataResponse.message,
                cart = mapAddToCartOccMultiCartData(dataResponse.detail),
                outOfService = dataResponse.outOfService,
                toasterAction = dataResponse.toasterAction
        )
    }

    private fun mapAddToCartOccMultiCartData(data: List<DetailOccMultiResponse>): List<AddToCartOccMultiCartData> {
        return data.map {
            AddToCartOccMultiCartData(
                    cartId = it.cartId,
                    productId = it.productId,
                    quantity = it.quantity.toIntOrZero(),
                    shopId = it.shopId
            )
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

    fun mapAddToCartOccMultiDataModel(addToCartOccMultiDataModel: AddToCartOccMultiDataModel): AddToCartDataModel {
        return AddToCartDataModel(
                errorMessage = ArrayList(addToCartOccMultiDataModel.errorMessage),
                status = addToCartOccMultiDataModel.status,
                data = mapDataModel(addToCartOccMultiDataModel.data)
        )
    }

    private fun mapDataModel(data: AddToCartOccMultiData): DataModel {
        val cart = data.cart.firstOrNull() ?: AddToCartOccMultiCartData()
        return DataModel(
                success = data.success,
                cartId = cart.cartId,
                productId = cart.productId.toLong(),
                quantity = cart.quantity,
                notes = cart.notes,
                shopId = cart.shopId.toLong(),
                customerId = cart.customerId.toLong(),
                warehouseId = cart.warehouseId.toLong()
        )
    }

}