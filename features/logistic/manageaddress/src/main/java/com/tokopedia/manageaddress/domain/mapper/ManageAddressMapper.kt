package com.tokopedia.manageaddress.domain.mapper

import com.tokopedia.logisticdata.data.entity.address.AddressModel
import com.tokopedia.manageaddress.domain.model.ManageAddressModel
import com.tokopedia.manageaddress.domain.response.GetPeopleAddressResponse
import com.tokopedia.manageaddress.domain.response.ManageAddressData
import javax.inject.Inject

class ManageAddressMapper @Inject constructor() {

    fun mapAddress(response: GetPeopleAddressResponse): ManageAddressModel {
        return ManageAddressModel().apply {
            this.listAddress = response.keroAddressCorner.data.map(peopleAddressModel)
            this.token = tokenModel(response.keroAddressCorner.token)
        }

    }

    private fun tokenModel(token: com.tokopedia.manageaddress.domain.response.Token): com.tokopedia.logisticdata.data.entity.address.Token {
        return com.tokopedia.logisticdata.data.entity.address.Token().apply {
            this.ut = token.ut
            this.districtRecommendation = token.districtReccomendation
        }
    }

    private val peopleAddressModel: (ManageAddressData) -> AddressModel = {
        AddressModel().apply {
            this.addressId = it.addrId
            this.addressName = it.addrName
            this.receiverName = it.receiverName
            this.receiverPhone = it.phone
            this.addressStreet = it.address1
            this.cityName = it.cityName
            this.cityId = it.city
            this.districtId = it.district
            this.districtName = it.districtName
            this.postalCode = it.postalCode
            this.provinceId = it.province
            this.provinceName = it.provinceName
            this.latitude = it.latitude
            this.longitude = it.longitude
            this.addressStatus = it.status
        }
    }
}
