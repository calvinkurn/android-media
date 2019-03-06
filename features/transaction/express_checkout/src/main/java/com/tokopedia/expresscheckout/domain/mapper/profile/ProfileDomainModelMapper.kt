package com.tokopedia.expresscheckout.domain.mapper.profile

import com.tokopedia.expresscheckout.domain.model.profile.*
import com.tokopedia.transactiondata.entity.response.expresscheckout.profile.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

class ProfileDomainModelMapper @Inject constructor() : ProfileDataMapper {

    override fun convertToDomainModel(profileResponse: ProfileResponse): ProfileResponseModel {
        val profileResponseModel = ProfileResponseModel()
        profileResponseModel.status = profileResponse.status
        profileResponseModel.profileDataModel = getProfileDataModel(profileResponse.data)

        return profileResponseModel
    }

    private fun getProfileDataModel(profileData: ProfileData): ProfileDataModel {
        val profileDataModel = ProfileDataModel()
        profileDataModel.defaultProfileId = profileData.defaultProfileId

        val profileModels = ArrayList<ProfileModel>()
        for (profile: Profile in profileData.profiles) {
            profileModels.add(getProfileModel(profile))
        }
        profileDataModel.profileModels = profileModels

        return profileDataModel
    }

    private fun getProfileModel(profile: Profile): ProfileModel {
        val profileModel = ProfileModel()
        profileModel.id = profile.id
        profileModel.status = profile.status
        profileModel.addressModel = getAddressModel(profile.address)
        profileModel.paymentModel = getPaymentModel(profile.payment)
        profileModel.shipmentModel = getShipmentModel(profile.shipment)

        return profileModel
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
        addressModel.latitude = address.latitude
        addressModel.longitude = address.longitude
        addressModel.phone = address.phone
        addressModel.provinceId = address.provinceId
        addressModel.provinceName = address.provinceName
        addressModel.receiverName = address.receiverName
        addressModel.postalCode = address.postalCode

        return addressModel
    }

    private fun getPaymentModel(payment: Payment): PaymentModel {
        val paymentModel = PaymentModel()
        paymentModel.checkoutParam = payment.checkoutParam
        paymentModel.description = payment.description
        paymentModel.gatewayCode = payment.gatewayCode
        paymentModel.image = payment.image
        paymentModel.url = payment.url

        return paymentModel
    }

    private fun getShipmentModel(shipment: Shipment): ShipmentModel {
        val shipmentModel = ShipmentModel()
        shipmentModel.serviceId = shipment.serviceId
        shipmentModel.serviceDuration = shipment.serviceDuration

        return shipmentModel
    }
}