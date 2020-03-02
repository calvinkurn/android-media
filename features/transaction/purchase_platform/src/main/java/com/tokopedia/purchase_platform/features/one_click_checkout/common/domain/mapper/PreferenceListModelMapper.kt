package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.*
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.*
import javax.inject.Inject

class PreferenceListModelMapper @Inject constructor() : PreferenceDataMapper {

    override fun convertToDomainModel(response: Response): PreferenceListResponseModel {
        val preferenceListResponseModel = PreferenceListResponseModel()
        preferenceListResponseModel.messages = response.messages

        val profilesModules = ArrayList<ProfilesItemModel?>()
        for(profile: ProfilesItem in response.profiles) {
            profilesModules.add(getProfilesItemModel(profile))
        }

        preferenceListResponseModel.profiles = profilesModules

        return preferenceListResponseModel
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
                address.provinceName +
                address.postalCode


        return addressModel
    }

    private fun getShipmentModel(shipment: Shipment): ShipmentModel {
        val shipmentModel = ShipmentModel()
        shipmentModel.serviceId = shipment.serviceId
        shipmentModel.serviceDuration = shipmentModel.serviceDuration
        shipmentModel.serviceName = shipmentModel.serviceName

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