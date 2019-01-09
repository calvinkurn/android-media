package com.tokopedia.expresscheckout.domain.mapper.profile

import com.tokopedia.expresscheckout.data.entity.response.Header
import com.tokopedia.expresscheckout.data.entity.response.profile.*
import com.tokopedia.expresscheckout.domain.model.HeaderModel
import com.tokopedia.expresscheckout.domain.model.profile.*

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

class ProfileDomainModelMapper : ProfileDataMapper {

    override fun convertToDomainModel(profileResponse: ProfileResponse): ProfileResponseModel {
        var profileResponseModel = ProfileResponseModel()
        profileResponseModel.headerModel = getHeaderModel(profileResponse.header)
        profileResponseModel.profileDataModel = getProfileDataModel(profileResponse.data)

        return profileResponseModel
    }

    fun getHeaderModel(header: Header): HeaderModel {
        var headerModel = HeaderModel()
        headerModel.processTime = header.processTime
        headerModel.reason = header.reason
        headerModel.errors = header.errors
        headerModel.errorCode = header.errorCode

        return headerModel
    }

    fun getProfileDataModel(profileData: ProfileData): ProfileDataModel {
        var profileDataModel = ProfileDataModel()
        profileDataModel.defaultProfileId = profileData.defaultProfileId

        var profileModels = ArrayList<ProfileModel>()
        for (profile: Profile in profileData.profiles) {
            profileModels.add(getProfileModel(profile))
        }
        profileDataModel.profileModels = profileModels

        return profileDataModel
    }

    fun getProfileModel(profile: Profile): ProfileModel {
        var profileModel = ProfileModel()
        profileModel.id = profile.id
        profileModel.status = profile.status
        profileModel.addressModel = getAddressModel(profile.address)
        profileModel.paymentModel = getPaymentModel(profile.payment)
        profileModel.shipmentModel = getShipmentModel(profile.shipment)

        return profileModel
    }

    fun getAddressModel(address: Address): AddressModel {
        var addressModel = AddressModel()
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

    fun getPaymentModel(payment: Payment): PaymentModel {
        var paymentModel = PaymentModel()
        paymentModel.checkoutParam = payment.checkoutParam
        paymentModel.description = payment.description
        paymentModel.gatewayCode = payment.gatewayCode
        paymentModel.image = payment.image
        paymentModel.url = payment.url

        return paymentModel
    }

    fun getShipmentModel(shipment: Shipment): ShipmentModel {
        var shipmentModel = ShipmentModel()
        shipmentModel.serviceId = shipment.serviceId
        shipmentModel.serviceDuration = shipment.serviceDuration

        return shipmentModel
    }
}