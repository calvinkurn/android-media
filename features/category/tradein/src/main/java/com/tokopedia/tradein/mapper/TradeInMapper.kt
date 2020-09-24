package com.tokopedia.tradein.mapper

import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
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
    }
}