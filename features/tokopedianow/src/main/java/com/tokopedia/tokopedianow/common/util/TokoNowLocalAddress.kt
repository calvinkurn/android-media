package com.tokopedia.tokopedianow.common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper.mapWarehousesResponseToLocal
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import java.util.*
import javax.inject.Inject

class TokoNowLocalAddress @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val OOC_WAREHOUSE_ID = 0L
    }

    private var localAddressData: LocalCacheModel? = null

    init {
        localAddressData = ChooseAddressUtils.getLocalizingAddressData(context)
    }

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
        updateLocalData()
    }

    fun updateLocalData() {
        localAddressData?.let {
            if (ChooseAddressUtils.isLocalizingAddressHasUpdated(context, it)) {
                localAddressData = ChooseAddressUtils.getLocalizingAddressData(context)
            }
        }
    }

    fun isOutOfCoverage() = getWarehouseId() == OOC_WAREHOUSE_ID

    fun getWarehouseId() = localAddressData?.warehouse_id.toLongOrZero()

    fun getShopId() = localAddressData?.shop_id.toLongOrZero()
}
