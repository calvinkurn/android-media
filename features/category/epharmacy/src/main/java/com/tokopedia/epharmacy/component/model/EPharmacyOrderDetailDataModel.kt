package com.tokopedia.epharmacy.component.model

import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory

data class EPharmacyOrderDetailHeaderDataModel(
    override val name: String = "",
    override val type: String = "",
    val title: String?,
    val tickerType: Int?,
    val tickerMessage: String?,
    val invoiceTitle: String?,
    val invoiceLink: String?,
    val chatDate: String?,
    val validUntil: String
) :
    BaseEPharmacyDataModelImpl(name, type) {

    override fun type(typeFactory: EPharmacyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

data class EPharmacyOrderDetailInfoDataModel(
    override val name: String = "",
    override val type: String = "",
    val serviceType: String?,
    val enablerName: String?,
    val duration: String?,
    val fees: String?,
    val validity: String
) :
    BaseEPharmacyDataModelImpl(name, type) {

    override fun type(typeFactory: EPharmacyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}


data class EPharmacyOrderDetailPaymentDataModel(
    override val name: String = "",
    override val type: String = "",
    val paymentMethod: String?,
    val totalPrice: String?,
    val totalPayment: String?
) :
    BaseEPharmacyDataModelImpl(name, type) {

    override fun type(typeFactory: EPharmacyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
