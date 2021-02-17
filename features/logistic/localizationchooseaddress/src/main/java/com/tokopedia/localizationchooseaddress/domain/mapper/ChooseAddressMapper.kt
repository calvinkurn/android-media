package com.tokopedia.localizationchooseaddress.domain.mapper

import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListResponse
import javax.inject.Inject

class ChooseAddressMapper @Inject constructor() {

    fun mapChosenAddressList(response: GetChosenAddressListResponse): List<ChosenAddressModel> {
        val data = response.addressList
        return data.map {
            ChosenAddressModel(
                    it.addressId.toString(),
                    it.receiverName,
                    it.addressName,
                    it.address1,
                    it.address2,
                    it.postalCode.toString(),
                    it.provinceId.toString(),
                    it.cityId.toString(),
                    it.districtId.toString(),
                    it.phone,
                    it.provinceName,
                    it.cityName,
                    it.districtName,
                    it.status.toString(),
                    it.country,
                    it.latitude,
                    it.longitude,
                    it.isStateChosenAddress
            )
        }
    }

}