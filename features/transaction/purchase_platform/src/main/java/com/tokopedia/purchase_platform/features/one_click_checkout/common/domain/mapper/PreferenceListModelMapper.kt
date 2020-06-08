package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.features.one_click_checkout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.purchase_platform.features.one_click_checkout.common.STATUS_OK
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.*
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.*
import javax.inject.Inject

class PreferenceListModelMapper @Inject constructor() : PreferenceDataMapper {

    override fun convertToDomainModel(response: PreferenceListGqlResponse): PreferenceListResponseModel {
        if (response.data.status.equals(STATUS_OK, true)) {
            val data = response.data.data
            if (data.success == 1) {
                val preferenceListResponseModel = PreferenceListResponseModel()
                preferenceListResponseModel.messages = data.messages

                val profilesModules = ArrayList<ProfilesItemModel>()
                for (profile: ProfilesItem in data.profiles) {
                    profilesModules.add(getProfilesItemModel(profile))
                }

                preferenceListResponseModel.profiles = profilesModules
                preferenceListResponseModel.maxProfile = data.maxProfile

                return preferenceListResponseModel
            } else {
                val errorMessage = data.messages
                if (errorMessage.isNotEmpty()) {
                    throw MessageErrorException(errorMessage[0])
                } else {
                    throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
                }
            }
        } else {
            val errorMessage = response.data.errorMessage
            if (errorMessage.isNotEmpty()) {
                throw MessageErrorException(errorMessage[0])
            } else {
                throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
            }
        }
    }


    private fun getProfilesItemModel(profilesItem: ProfilesItem): ProfilesItemModel {
        val profilesItemsModel = ProfilesItemModel()
        profilesItemsModel.apply {
            profileId = profilesItem.profileId
            status = profilesItem.status
            addressModel = profilesItem.address?.let { getAddressModel(it) }
            paymentModel = profilesItem.payment?.let { getPaymentModel(it) }
            shipmentModel = profilesItem.shipment?.let { getShipmentModel(it) }
        }
        return profilesItemsModel
    }

    private fun getAddressModel(address: Address): AddressModel {
        val addressModel = AddressModel()
        addressModel.apply {
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
        return addressModel
    }

    private fun getShipmentModel(shipment: Shipment): ShipmentModel {
        val shipmentModel = ShipmentModel()
        shipmentModel.apply {
            serviceId = shipment.serviceId
            serviceDuration = shipment.serviceDuration
            serviceName = shipment.serviceName
        }
        return shipmentModel
    }

    private fun getPaymentModel(payment: Payment): PaymentModel {
        val paymentModel = PaymentModel()
        paymentModel.apply {
            gatewayCode = payment.gatewayCode
            description = payment.description
            gatewayName = payment.gatewayName
            image = payment.image
            url = payment.url
            metadata = payment.metadata
        }
        return paymentModel
    }
}