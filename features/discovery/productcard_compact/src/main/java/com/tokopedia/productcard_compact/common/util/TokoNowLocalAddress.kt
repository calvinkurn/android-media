package com.tokopedia.productcard_compact.common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper.mapWarehousesResponseToLocal
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import java.util.*
import javax.inject.Inject

class TokoNowLocalAddress @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val OOC_WAREHOUSE_ID = 0L
    }

    private var localAddressData = ChooseAddressUtils.getLocalizingAddressData(context)

    fun updateLocalData() {
        if (ChooseAddressUtils.isLocalizingAddressHasUpdated(context, localAddressData)) {
            localAddressData = ChooseAddressUtils.getLocalizingAddressData(context)
        }
    }

    fun isOutOfCoverage() = getWarehouseId() == OOC_WAREHOUSE_ID

    fun getWarehouseId() = localAddressData.warehouse_id.toLongOrZero()

    fun getShopId() = localAddressData.shop_id.toLongOrZero()
}
