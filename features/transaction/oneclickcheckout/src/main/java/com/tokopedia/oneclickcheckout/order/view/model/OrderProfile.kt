package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.localizationchooseaddress.domain.response.Warehouse

data class OrderProfile(
        val profileId: Int = 0,
        val enable: Boolean = true,
        val message: String = "",
        val address: OrderProfileAddress = OrderProfileAddress(),
        val payment: OrderProfilePayment = OrderProfilePayment(),
        val shipment: OrderProfileShipment = OrderProfileShipment()
) {
    val isValidProfile: Boolean
        get() = address.addressId > 0 && payment.gatewayCode.isNotEmpty()

    fun isDisableChangeCourierAndNeedPinpoint(): Boolean {
        return shipment.isDisableChangeCourier && address.hasNoPinpoint
    }
}

data class OrderProfileAddress(
        val addressId: Long = 0,
        val receiverName: String = "",
        val addressName: String = "",
        val addressStreet: String = "",
        val districtId: Long = 0,
        val districtName: String = "",
        val cityId: Long = 0,
        val cityName: String = "",
        val provinceId: Long = 0,
        val provinceName: String = "",
        val phone: String = "",
        val longitude: String = "",
        val latitude: String = "",
        val postalCode: String = "",
        val state: Int = 0,
        val stateDetail: String = "",
        val status: Int = 0,
        val tokoNow: OrderProfileAddressTokoNow = OrderProfileAddressTokoNow()
) {
    internal val isMainAddress: Boolean
        get() = status == STATUS_MAIN_ADDRESS

    internal val hasNoPinpoint: Boolean
        get() = longitude.isEmpty() || latitude.isEmpty()

    companion object {
        private const val STATUS_MAIN_ADDRESS = 2
        const val STATE_OCC_ADDRESS_ID_NOT_MATCH = 211
    }
}

data class OrderProfileAddressTokoNow(
    val isModified: Boolean = false,
    val shopId: String = "",
    val warehouseId: String = "",
    val warehouses: List<Warehouse> = emptyList(),
    val serviceType: String = ""
)

data class OrderProfileShipment(
        val serviceName: String = "",
        val serviceId: Int = 0,
        val serviceDuration: String = "",
        val spId: Int = 0,
        val recommendationServiceId: Int = 0,
        val recommendationSpId: Int = 0,
        val isFreeShippingSelected: Boolean = false,
        val isDisableChangeCourier: Boolean = false,
        val autoCourierSelection: Boolean = false,
        val courierSelectionError: CourierSelectionError = CourierSelectionError()
)

data class CourierSelectionError(
        val title: String = "",
        val description: String = ""
)

data class OrderProfilePayment(
        val enable: Int = 0,
        val active: Int = 0,
        val gatewayCode: String = "",
        val gatewayName: String = "",
        val image: String = "",
        val description: String = "",
        val metadata: String = "",
        val tickerMessage: String = ""
)