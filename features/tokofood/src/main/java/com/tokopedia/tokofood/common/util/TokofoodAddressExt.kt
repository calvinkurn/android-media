package com.tokopedia.tokofood.common.util

import android.content.Context
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse

object TokofoodAddressExt {

    fun KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse.updateLocalChosenAddressPinpoint(
        context: Context
    ) {
        if (this.success) {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                context = context,
                addressId = this.chosenAddressData.addressId.toString(),
                cityId = this.chosenAddressData.cityId.toString(),
                districtId = this.chosenAddressData.districtId.toString(),
                lat = this.chosenAddressData.latitude,
                long = this.chosenAddressData.longitude,
                label = "${this.chosenAddressData.addressName} ${this.chosenAddressData.receiverName}",
                postalCode = this.chosenAddressData.postalCode,
                warehouseId = this.tokonow.warehouseId.toString(),
                shopId = this.tokonow.shopId.toString(),
                warehouses = this.tokonow.warehouses.map { warehouse ->
                    LocalWarehouseModel(
                        warehouse.warehouseId,
                        warehouse.serviceType
                    )
                },
                serviceType = this.tokonow.serviceType,
                lastUpdate = this.tokonow.tokonowLastUpdate
            )
        }
    }
}
