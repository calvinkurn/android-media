package com.tokopedia.salam.umrah.checkout.data


import com.tokopedia.salam.umrah.common.data.UmrahProductModel

class UmrahCheckoutMapperEntity(
    val checkoutPDP : UmrahProductModel.UmrahProduct = UmrahProductModel.UmrahProduct(),
    val user: ContactUser = ContactUser(),
    val summaryPayment: UmrahCheckoutSummaryEntity = UmrahCheckoutSummaryEntity(),
    val paymentOptions: UmrahCheckoutPaymentOptionsEntity = UmrahCheckoutPaymentOptionsEntity(),
    val termCondition: UmrahCheckoutTermConditionsEntity = UmrahCheckoutTermConditionsEntity()
)


class ContactUser(
        val id: String = "",
        var name: String = "",
        var email: String = "",
        var phoneNumber : String = ""
)


