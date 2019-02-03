package com.tokopedia.expresscheckout.domain.mapper.checkout

import com.tokopedia.expresscheckout.data.entity.response.checkout.*
import com.tokopedia.expresscheckout.domain.model.HeaderModel
import com.tokopedia.expresscheckout.domain.model.checkout.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

class CheckoutDomainModelMapper @Inject constructor() : CheckoutDataMapper {

    override fun convertToDomainModel(checkoutResponse: CheckoutResponse): CheckoutResponseModel {
        val checkoutResponseModel = CheckoutResponseModel()
        checkoutResponseModel.status = checkoutResponse.status
        checkoutResponseModel.headerModel = getHeaderModel(checkoutResponse)
        checkoutResponseModel.checkoutDataModel = getCheckoutDataModel(checkoutResponse.data)

        return checkoutResponseModel
    }

    private fun getHeaderModel(checkoutResponse: CheckoutResponse): HeaderModel {
        val headerModel = HeaderModel()
        headerModel.processTime = checkoutResponse.header.processTime
        headerModel.reason = checkoutResponse.header.reason
        headerModel.errors = checkoutResponse.header.errors
        headerModel.errorCode = checkoutResponse.header.errorCode
        headerModel.messages = checkoutResponse.header.messages

        return headerModel
    }

    private fun getCheckoutDataModel(checkoutData: CheckoutData): CheckoutDataModel {
        val checkoutDataModel = CheckoutDataModel()
        checkoutDataModel.success = checkoutData.success
        checkoutDataModel.error = checkoutData.error
        checkoutDataModel.errorState = checkoutData.errorState
        checkoutDataModel.message = checkoutData.message
        checkoutDataModel.dataModel = getDataModel(checkoutData.data)

        return checkoutDataModel
    }

    private fun getDataModel(data: Data): DataModel {
        val dataModel = DataModel()
        dataModel.applink = data.applink
        dataModel.callbackUrl = data.callbackUrl
        dataModel.redirectParam = data.redirectParam
        dataModel.reflectModel = getReflectModel(data.reflect)

        return dataModel
    }

    private fun getReflectModel(reflect: Reflect): ReflectModel {
        val reflectModel = ReflectModel()
        reflectModel.additionalFee = reflect.additionalFee
        reflectModel.amount = reflect.amount
        reflectModel.currency = reflect.currency
        reflectModel.customerEmail = reflect.customerEmail
        reflectModel.errorCode = reflect.errorCode
        reflectModel.expiredOn = reflect.expiredOn
        reflectModel.fee = reflect.fee
        reflectModel.gatewayCode = reflect.gatewayCode
        reflectModel.gatewayType = reflect.gatewayType

        val itemModels = ArrayList<ItemModel>()
        for (item: Item in reflect.items) {
            itemModels.add(getItemModel(item))
        }
        reflectModel.itemModels = itemModels

        reflectModel.merchantCode = reflect.merchantCode
        reflectModel.pairData = reflect.pairData
        reflectModel.profileCode = reflect.profileCode

        val paymentDetailModels = ArrayList<PaymentDetailModel>()
        for (paymentDetail: PaymentDetail in reflect.paymentDetails) {
            paymentDetailModels.add(getPaymentDetailModel(paymentDetail))
        }
        reflectModel.paymentDetailModels = paymentDetailModels
        reflectModel.signature = reflect.signature
        reflectModel.state = reflect.state
        reflectModel.tokocashUsage = reflect.tokocashUsage
        reflectModel.transactionCode = reflect.transactionCode
        reflectModel.transactionId = reflect.transactionId
        reflectModel.updatedOn = reflect.updatedOn
        reflectModel.use3dsecure = reflect.use3dsecure
        reflectModel.userDefinedValue = reflect.userDefinedValue
        reflectModel.validParam = reflect.validParam

        return reflectModel
    }

    private fun getItemModel(item: Item): ItemModel {
        val itemModel = ItemModel()
        itemModel.id = item.id
        itemModel.name = item.name
        itemModel.price = item.price
        itemModel.quantity = item.quantity

        return itemModel
    }

    private fun getPaymentDetailModel(paymentDetail: PaymentDetail): PaymentDetailModel {
        val paymentDetailModel = PaymentDetailModel()
        paymentDetailModel.amount = paymentDetail.amount
        paymentDetailModel.name = paymentDetail.name

        return paymentDetailModel
    }

}