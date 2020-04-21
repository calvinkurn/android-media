package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.one_click_checkout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.purchase_platform.features.one_click_checkout.common.STATUS_OK
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.Address
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.Payment
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.ProfilesItem
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.Shipment
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.AddressModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.PaymentModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ShipmentModel
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model.GetPreferenceByIdGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model.GetPreferenceData
import javax.inject.Inject

class PreferenceModelMapper @Inject constructor() {

    fun convertToDomainModel(response: GetPreferenceByIdGqlResponse): GetPreferenceData {
        if (response.response.status.equals(STATUS_OK, true)) {
            val data = response.response.data
            if (data.success == 1) {
                val profilesItemModel = getProfilesItemModel(data.profile)
                return GetPreferenceData().apply {
                    addressModel = profilesItemModel.addressModel
                    shipmentModel = profilesItemModel.shipmentModel
                    paymentModel = profilesItemModel.paymentModel
                    profileId = profilesItemModel.profileId
                    status = profilesItemModel.status
                }
            } else {
                val errorMessage = data.messages
                if (errorMessage.isNotEmpty()) {
                    throw MessageErrorException(errorMessage[0])
                } else {
                    throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
                }
            }
        } else {
            val errorMessage = response.response.errorMessage
            if (errorMessage.isNotEmpty()) {
                throw MessageErrorException(errorMessage[0])
            } else {
                throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
            }
        }
    }


    private fun getProfilesItemModel(profilesItem: ProfilesItem): ProfilesItemModel {
        return ProfilesItemModel().apply {
            profileId = profilesItem.profileId
            status = profilesItem.status
            addressModel = profilesItem.address?.let { getAddressModel(it) }
            paymentModel = profilesItem.payment?.let { getPaymentModel(it) }
            shipmentModel = profilesItem.shipment?.let { getShipmentModel(it) }
        }
    }

    private fun getAddressModel(address: Address): AddressModel {
        return AddressModel().apply {
            addressId = address.addressId
            addressName = address.addressName
            addressStreet = address.addressStreet
            cityId = address.cityId
            cityName = address.cityName
            districtId = address.districtId
            districtName = address.districtName
            phone = address.phone
            postalCode = address.postalCode
            provinceId = address.provinceId
            provinceName = address.provinceName
            receiverName = address.receiverName

            fullAddress = "${address.addressStreet}, ${address.districtName}, ${address.cityName}, ${address.provinceName} ${address.postalCode}"
        }
    }

    private fun getShipmentModel(shipment: Shipment): ShipmentModel {
        return ShipmentModel().apply {
            serviceId = shipment.serviceId
            serviceDuration = shipment.serviceDuration
            serviceName = shipment.serviceName
        }
    }

    private fun getPaymentModel(payment: Payment): PaymentModel {
        return PaymentModel().apply {
            gatewayCode = payment.gatewayCode
            description = payment.description
            gatewayName = payment.gatewayName
            image = payment.image
            url = payment.url
            metadata = payment.metadata
        }
    }
}