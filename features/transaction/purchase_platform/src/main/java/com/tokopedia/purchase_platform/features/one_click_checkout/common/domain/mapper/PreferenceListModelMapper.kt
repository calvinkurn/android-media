package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.*
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.*
import javax.inject.Inject

class PreferenceListModelMapper @Inject constructor() : PreferenceDataMapper {

    override fun convertToDomainModel(response: PreferenceListGqlResponse): PreferenceListResponseModel {
        if (response.data.status.equals("OK", true)) {
            val data = response.data.data
            if (data.success == 1) {
                val preferenceListResponseModel = PreferenceListResponseModel()
                preferenceListResponseModel.messages = data.messages

                val profilesModules = ArrayList<ProfilesItemModel>()
                for (profile: ProfilesItem in data.profiles) {
                    profilesModules.add(getProfilesItemModel(profile))
                }

                preferenceListResponseModel.profiles = profilesModules

                return preferenceListResponseModel
            } else {
                val errorMessage = data.messages
                if (errorMessage.isNotEmpty()) {
                    throw MessageErrorException(errorMessage[0])
                } else {
                    throw MessageErrorException("Terjadi kesalahan pada server. Ulangi beberapa saat lagi")
                }
            }
        } else {
            val errorMessage = response.data.errorMessage
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