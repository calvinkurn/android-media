package com.tokopedia.oneclickcheckout.common.domain.mapper

import com.tokopedia.oneclickcheckout.common.data.model.*
import com.tokopedia.oneclickcheckout.common.view.model.preference.*

object PreferenceModelMapper {

    fun convertToUIModel(data: PreferenceListData): PreferenceListResponseModel {
        return PreferenceListResponseModel().apply {
            messages = data.messages
            val profilesModules = ArrayList<ProfilesItemModel>()
            for (profile: ProfilesItem in data.profiles) {
                profilesModules.add(getProfilesItemModel(profile))
            }
            profiles = profilesModules
            maxProfile = data.maxProfile
            ticker = data.tickers.firstOrNull()
            enableOccRevamp = data.enableOccRevamp
        }
    }


    fun getProfilesItemModel(profilesItem: ProfilesItem): ProfilesItemModel {
        return ProfilesItemModel().apply {
            profileId = profilesItem.profileId
            status = profilesItem.status
            enable = profilesItem.enable
            addressModel = getAddressModel(profilesItem.address)
            paymentModel = getPaymentModel(profilesItem.payment)
            shipmentModel = getShipmentModel(profilesItem.shipment)
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
            estimation = shipment.estimation
        }
    }

    private fun getPaymentModel(payment: Payment): PaymentModel {
        return PaymentModel().apply {
            gatewayCode = payment.gatewayCode
            description = payment.description
            gatewayName = payment.gatewayName
            image = payment.image
            metadata = payment.metadata
            tickerMessage = payment.tickerMessage
        }
    }
}