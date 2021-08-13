package com.tokopedia.home.beranda.data.model

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

data class HomeChooseAddressData(
        var isActive: Boolean = true,
        var lat: String = "",
        var long: String = "",
        var addressId: String = "",
        var cityId: String = "",
        var districId: String = "",
        var postCode: String = "",
        var label: String = ""
) {
    fun setLocalCacheModel(localCacheModel: LocalCacheModel?): HomeChooseAddressData {
        localCacheModel?.let {
            this.lat = localCacheModel.lat
            this.long = localCacheModel.long
            this.addressId = localCacheModel.address_id
            this.cityId = localCacheModel.city_id
            this.districId = localCacheModel.district_id
            this.postCode = ""
            this.label = localCacheModel.label
            this.postCode = localCacheModel.postal_code
        }
        return  this
    }

    fun toLocalCacheModel(): LocalCacheModel {
        return LocalCacheModel(
                address_id = addressId,
                city_id = cityId,
                district_id = districId,
                lat = lat,
                long = long,
                label = label,
                postal_code = postCode
        )
    }
}