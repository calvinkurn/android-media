package com.tokopedia.expresscheckout.domain.model.checkout

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class ReflectModel(
        var merchantCode: String? = null,
        var profileCode: String? = null,
        var transactionId: String? = null,
        var transactionCode: String? = null,
        var currency: String? = null,
        var amount: Int = 0,
        var gatewayCode: String? = null,
        var gatewayType: String? = null,
        var fee: Int = 0,
        var additionalFee: Int = 0,
        var userDefinedValue: String? = null,
        var customerEmail: String? = null,
        var state: Int = 0,
        var expiredOn: String? = null,
        var updatedOn: String? = null,
        var paymentDetailModels: ArrayList<PaymentDetailModel>? = null,
        var itemModels: ArrayList<ItemModel>? = null,
        var validParam: String? = null,
        var signature: String? = null,
        var tokocashUsage: Int = 0,
        var pairData: String? = null,
        var errorCode: String? = null,
        var use3dsecure: Boolean = false
)