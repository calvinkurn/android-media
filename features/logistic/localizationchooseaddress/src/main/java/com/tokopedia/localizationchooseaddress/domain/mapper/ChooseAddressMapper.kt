package com.tokopedia.localizationchooseaddress.domain.mapper

import com.tokopedia.localizationchooseaddress.domain.model.*
import com.tokopedia.localizationchooseaddress.domain.response.*
import com.tokopedia.localizationchooseaddress.domain.response.ErrorChosenAddress
import javax.inject.Inject

class ChooseAddressMapper @Inject constructor() {

    fun mapChosenAddressList(response: GetChosenAddressListResponse): List<ChosenAddressList> {
        val data = response.addressList
        return data.map {
            ChosenAddressList(
                it.addressId.toString(),
                it.receiverName,
                it.addressName,
                it.address1,
                it.address2,
                it.postalCode,
                it.provinceId.toString(),
                it.cityId.toString(),
                it.districtId.toString(),
                it.phone,
                it.provinceName,
                it.cityName,
                it.districtName,
                it.status,
                it.country,
                it.latitude,
                it.longitude,
                it.isStateChosenAddress,
                mapTokonowAddress(it.tokonow)
            )
        }
    }

    fun mapSetStateChosenAddress(response: SetStateChosenAddressResponse): ChosenAddressModel {
        val data = response.data.chosenAddressData
        return ChosenAddressModel().apply {
            addressId = data.addressId
            receiverName = data.receiverName
            addressName = data.addressName
            districtId = data.districtId
            cityId = data.cityId
            cityName = data.cityName
            districtName = data.districtName
            status = data.status
            latitude = data.latitude
            longitude = data.longitude
            postalCode = data.postalCode
            tokonowModel = mapTokonow(response.data.tokonow)
        }
    }

    fun mapGetStateChosenAddress(response: GetStateChosenAddressResponse): ChosenAddressModel {
        val data = response.data
        return ChosenAddressModel().apply {
            addressId = data.addressId
            receiverName = data.receiverName
            addressName = data.addressName
            districtId = data.districtId
            cityId = data.cityId
            cityName = data.cityName
            districtName = data.districtName
            status = data.status
            latitude = data.latitude
            longitude = data.longitude
            postalCode = data.postalCode
            tokonowModel = mapTokonow(response.tokonow)
            errorCode = mapErrorChosenAddress(response.data.error)
        }
    }

    fun mapDefaultChosenAddress(response: GetDefaultChosenAddressResponse): DefaultChosenAddressModel {
        return DefaultChosenAddressModel().apply {
            addressData = mapDefaultAddress(response.data)
            keroAddrError = mapKeroDefaultAddress(response.error)
            tokonow = mapTokonow(response.tokonow)
        }
    }

    private fun mapDefaultAddress(response: DefaultChosenAddressData): DefaultChosenAddress {
        return DefaultChosenAddress().apply {
            addressId = response.addressId
            receiverName = response.receiverName
            addressName = response.addressName
            address1 = response.address1
            address2 = response.address2
            postalCode = response.postalCode
            provinceId = response.provinceId
            cityId = response.cityId
            districtId = response.districtId
            phone = response.phone
            provinceName = response.provinceName
            cityName = response.cityName
            districtName = response.districtName
            status = response.status
            country = response.country
            latitude = response.latitude
            longitude = response.longitude
        }
    }

    private fun mapTokonow(response: Tokonow): TokonowModel {
        return TokonowModel().apply {
            shopId = response.shopId
            warehouseId = response.warehouseId
        }
    }

    private fun mapErrorChosenAddress(response: ErrorChosenAddress): ErrorChosenAddressModel {
        return ErrorChosenAddressModel().apply {
            code = response.code
            title = response.title
        }
    }

    private fun mapTokonowAddress(response: TokonowAddressList): TokonowAddress {
       return TokonowAddress().apply {
           warehouseId = response.warehouseId
           coverageLabel = response.coverageLabel
       }
    }

    private fun mapKeroDefaultAddress(response: ErrorDefaultAddress): KeroDefaultAddressError{
        return KeroDefaultAddressError().apply {
            code = response.code
            detail = response.detail
        }
    }
}