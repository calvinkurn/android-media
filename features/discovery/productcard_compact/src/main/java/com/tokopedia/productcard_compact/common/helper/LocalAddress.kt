package com.tokopedia.productcard_compact.common.helper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import javax.inject.Inject

class LocalAddress @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val OOC_WAREHOUSE_ID = 0L
    }

    private var localAddressData: LocalCacheModel? = null

    init {
        localAddressData = ChooseAddressUtils.getLocalizingAddressData(context)
    }

    fun isOutOfCoverage() = getWarehouseId() == OOC_WAREHOUSE_ID

    fun getWarehouseId() = localAddressData?.warehouse_id.toLongOrZero()

    fun getShopId() = localAddressData?.shop_id.toLongOrZero()
}
