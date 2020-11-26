package com.tokopedia.tradein.mapper

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data

class TradeInMapper {

    companion object {
        fun mapAddressToKeroAddress(addressModel: RecipientAddressModel): Data {
            with(addressModel) {
                return Data(
                        id.toInt(),
                        addressName,
                        street,
                        addressName,
                        cityId.toInt(),
                        cityName ?: "",
                        countryName ?: "",
                        destinationDistrictId.toInt(),
                        destinationDistrictName,
                        isSelected,
                        isSelected,
                        isSelected,
                        latitude,
                        longitude,
                        recipientPhoneNumber,
                        postalCode,
                        provinceId.toInt(),
                        provinceName,
                        recipientName,
                        addressStatus
                )
            }
        }

        fun mapSavedAddressToKeroAddress(addressDataModel: SaveAddressDataModel) : Data {
            with(addressDataModel) {
                return Data(id, title, address1, address2, cityId,
                        "", "", districtId, selectedDistrict, true,
                        true, true, latitude, longitude, phone, postalCode,
                        provinceId, "", receiverName, 1)
            }
        }

        fun mapKeroAddressToRecipientAddress(address: Data?) : RecipientAddressModel{
            return RecipientAddressModel().apply {
                address?.let {
                    addressName = it.addrName
                    id = it.addrId.toString()
                    street = it.address1
                    cityId = it.city.toString()
                    cityName = it.cityName
                    countryName = it.country
                    destinationDistrictId = it.district.toString()
                    destinationDistrictName = it.districtName
                    isSelected = it.isActive
                    latitude = it.latitude
                    longitude = it.longitude
                    recipientPhoneNumber = it.phone
                    postalCode = it.postalCode
                    provinceId = it.province.toString()
                    provinceName = it.provinceName
                    recipientName = it.receiverName
                    addressStatus = it.status
                }
            }
        }
    }
}