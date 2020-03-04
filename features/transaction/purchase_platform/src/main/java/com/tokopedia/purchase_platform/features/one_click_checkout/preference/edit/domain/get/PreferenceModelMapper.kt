package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.*
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.*
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model.GetPreferenceByIdGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model.GetPreferenceData
import javax.inject.Inject

class PreferenceModelMapper @Inject constructor() {

    fun convertToDomainModel(response: GetPreferenceByIdGqlResponse): GetPreferenceData {
        if (response.response.status.equals("OK", true)) {
            val data = response.response.data
            if (data.success == 1) {
                val getPreferenceData = GetPreferenceData()
//                getPreferenceData.messages = data.messages

                val profilesItemModel = getProfilesItemModel(data.profile)

                getPreferenceData.addressModel = profilesItemModel.addressModel
                getPreferenceData.shipmentModel = profilesItemModel.shipmentModel
                getPreferenceData.paymentModel = profilesItemModel.paymentModel
                getPreferenceData.profileId = profilesItemModel.profileId
                getPreferenceData.status = profilesItemModel.status

                return getPreferenceData
            } else {
                val errorMessage = data.messages
                if (errorMessage.isNotEmpty()) {
                    throw MessageErrorException(errorMessage[0])
                } else {
                    throw MessageErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")
                }
            }
        } else {
            val errorMessage = response.response.errorMessage
            if (errorMessage.isNotEmpty()) {
                throw MessageErrorException(errorMessage[0])
            } else {
                throw MessageErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")
            }
        }
    }


    private fun getProfilesItemModel(profilesItem: ProfilesItem): ProfilesItemModel {
        val profilesItemsModel = ProfilesItemModel()
        profilesItemsModel.profileId = profilesItem.profileId
        profilesItemsModel.status = profilesItem.status
        profilesItemsModel.addressModel = profilesItem.address?.let { getAddressModel(it) }
        profilesItemsModel.paymentModel = profilesItem.payment?.let { getPaymentModel(it) }
        profilesItemsModel.shipmentModel = profilesItem.shipment?.let { getShipmentModel(it) }

        return profilesItemsModel
    }

    private fun getAddressModel(address: Address): AddressModel {
        val addressModel = AddressModel()
        addressModel.addressId = address.addressId
        addressModel.addressName = address.addressName
        addressModel.addressStreet = address.addressStreet
        addressModel.cityId = address.cityId
        addressModel.cityName = address.cityName
        addressModel.districtId = address.districtId
        addressModel.districtName = address.districtName
        addressModel.phone = address.phone
        addressModel.postalCode = address.postalCode
        addressModel.provinceId = address.provinceId
        addressModel.provinceName = address.provinceName
        addressModel.receiverName = address.receiverName

        addressModel.fullAddress = address.addressStreet + ", " +
                address.districtName + ", " +
                address.cityName + ", " +
                address.provinceName + " " +
                address.postalCode


        return addressModel
    }

    private fun getShipmentModel(shipment: Shipment): ShipmentModel {
        val shipmentModel = ShipmentModel()
        shipmentModel.serviceId = shipment.serviceId
        shipmentModel.serviceDuration = shipment.serviceDuration
        shipmentModel.serviceName = shipment.serviceName

        return shipmentModel
    }

    private fun getPaymentModel(payment: Payment): PaymentModel {
        val paymentModel = PaymentModel()
        paymentModel.gatewayCode = payment.gatewayCode
        paymentModel.description = payment.description
        paymentModel.gatewayName = payment.gatewayName
        paymentModel.image = payment.image
        paymentModel.url = payment.url

        return paymentModel
    }
}