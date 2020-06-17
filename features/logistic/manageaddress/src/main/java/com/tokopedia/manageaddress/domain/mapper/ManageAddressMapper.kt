package com.tokopedia.manageaddress.domain.mapper

import com.tokopedia.manageaddress.domain.model.ManageAddressModel
import com.tokopedia.manageaddress.domain.model.PeopleAddress
import com.tokopedia.manageaddress.domain.model.Token
import com.tokopedia.manageaddress.domain.response.GetPeopleAddressResponse
import com.tokopedia.manageaddress.domain.response.ManageAddressData
import javax.inject.Inject

class ManageAddressMapper @Inject constructor() {

    fun mapAddress(response: GetPeopleAddressResponse): ManageAddressModel {
        return ManageAddressModel().apply {
            this.liistAddress = response.keroAddressCorner.data.map(peopleAddressModel)
            this.token = tokenModel(response.keroAddressCorner.token)
        }

    }

    private fun tokenModel(token: com.tokopedia.manageaddress.domain.response.Token): Token {
        return Token().apply {
            this.ut = token.ut
            this.districtRecommendation = token.districtReccomendation
        }
    }

    private val peopleAddressModel: (ManageAddressData) -> PeopleAddress = {
        PeopleAddress().apply {
            this.addressName = it.addrName
            this.receiverName = it.receiverName
            this.phoneNumber = it.phone
            this.receiverAddress = it.address1
            this.cityName = it.cityName
            this.districtName = it.districtName
            this.postalCode = it.postalCode
            this.provinceName = it.provinceName
            this.latitude = it.latitude
            this.longitude = it.longitude
            this.status = it.status
        }
    }
}
