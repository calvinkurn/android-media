package com.tokopedia.epharmacy.component.model

import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory
import com.tokopedia.epharmacy.network.response.EPharmacyOrderDetailResponse

data class EPharmacyOrderDetailHeaderDataModel(
    override val name: String,
    override val type: String,
    val title: String?,
    val tickerType: Int?,
    val tickerMessage: String?,
    val invoiceNumber: String?,
    val invoiceLink: String?,
    val paymentDate: String?,
    val validUntil: String?,
    val chatStartDate: String?,
    val indicatorColor: String?
) :
    BaseEPharmacySimpleDataModelImpl(name, type) {

    override fun type(typeFactory: EPharmacyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

data class EPharmacyOrderDetailInfoDataModel(
    override val name: String,
    override val type: String,
    val serviceType: String?,
    val enablerName: String?,
    val duration: String?,
    val fees: String?,
    val validity: String
) :
    BaseEPharmacySimpleDataModelImpl(name, type) {

    override fun type(typeFactory: EPharmacyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

data class EPharmacyOrderDetailPaymentDataModel(
    override val name: String,
    override val type: String,
    val paymentMethod: String?,
    val totalPrice: String?,
    val totalPayment: String?,
    val helpButton: EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?
) :
    BaseEPharmacySimpleDataModelImpl(name, type) {

    override fun type(typeFactory: EPharmacyAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
