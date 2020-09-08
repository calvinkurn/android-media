package com.tokopedia.tradein.mapper

import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse

class TradeInMapper {

    companion object {
        fun mapAddressToKeroAddress(addressModel: RecipientAddressModel): MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data {
            addressModel.apply {
                return MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data(
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

        fun mapSavedAddressToKeroAddress(addressDataModel: SaveAddressDataModel) : MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data {
            addressDataModel.apply {
                return MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data(id, title, address1, address2, cityId,
                        "", "", districtId, selectedDistrict, true,
                        true, true, latitude, longitude, phone, postalCode,
                        provinceId, "", receiverName, 1)
            }
        }
    }
}