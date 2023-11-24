package com.tokopedia.tokopedianow.common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper.mapWarehousesResponseToLocal
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import java.util.*
import javax.inject.Inject

class TokoNowLocalAddress @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val OOC_WAREHOUSE_ID = 0L
    }

    private var localAddressData: LocalCacheModel = ChooseAddressUtils.getLocalizingAddressData(context)

    fun updateAddressData(response: GetStateChosenAddressResponse) {
        with(response) {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                context = context,
                addressId = data.addressId.toString(),
                cityId = data.cityId.toString(),
                districtId = data.districtId.toString(),
                lat = data.latitude,
                long = data.longitude,
                label = String.format(Locale.getDefault(), "%s %s", data.addressName, data.receiverName),
                postalCode = data.postalCode,
                warehouseId = tokonow.warehouseId.toString(),
                shopId = tokonow.shopId.toString(),
                warehouses = mapWarehousesResponseToLocal(tokonow.warehouses),
                serviceType = tokonow.serviceType,
                lastUpdate = tokonow.tokonowLastUpdate
            )
        }
        updateLocalDataIfAddressHasUpdated()
    }

    fun updateLocalDataIfAddressHasUpdated() {
        if (isChoosenAddressUpdated()) {
            localAddressData = ChooseAddressUtils.getLocalizingAddressData(context)
        }
    }

    fun isChoosenAddressUpdated(): Boolean {
        return ChooseAddressUtils.isLocalizingAddressHasUpdated(context, localAddressData)
    }

    fun isOutOfCoverage(): Boolean {
        return getWarehouseId() == OOC_WAREHOUSE_ID
    }

    fun getWarehouseId(): Long {
        updateLocalDataIfAddressHasUpdated()
        return localAddressData.warehouse_id.toLongOrZero()
    }

    fun getShopId(): Long {
        updateLocalDataIfAddressHasUpdated()
        return localAddressData.shop_id.toLongOrZero()
    }

    fun getWarehousesData(): List<WarehouseData> {
        updateLocalDataIfAddressHasUpdated()
        return AddressMapper.mapToWarehousesData(localAddressData)
    }

    fun getAddressData(): LocalCacheModel {
        updateLocalDataIfAddressHasUpdated()
        return localAddressData
    }

    fun setLocalData(data: LocalCacheModel) {
        localAddressData = data
    }
}
