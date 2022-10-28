package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSwitcherUiModel.Home20mSwitcher
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSwitcherUiModel.Home2hSwitcher

object SwitcherMapper {

    private const val DEFAULT_WAREHOUSE_ID = 0L

    fun createSwitcherUiModel(localCacheModel: LocalCacheModel): HomeLayoutItemUiModel? {
        val isServiceTypeNow15m = localCacheModel.service_type == ServiceType.NOW_15M
        val isServiceTypeNow2h = localCacheModel.service_type == ServiceType.NOW_2H

        val now15mWarehouse = localCacheModel.warehouses.find {
            it.service_type == ServiceType.NOW_15M
        }
        val now2hWarehouse = localCacheModel.warehouses.find {
            it.service_type == ServiceType.NOW_2H
        }

        val now15mWarehouseId = now15mWarehouse?.warehouse_id.orZero()
        val now2hWarehouseId = now2hWarehouse?.warehouse_id.orZero()

        val isEligibleForNow15m = now15mWarehouseId != DEFAULT_WAREHOUSE_ID
        val isEligibleForNow2h = now2hWarehouseId != DEFAULT_WAREHOUSE_ID

        return when {
            isServiceTypeNow2h && isEligibleForNow15m -> create20mSwitcherUiModel()
            isServiceTypeNow15m && isEligibleForNow2h -> create2HSwitcherUiModel()
            else -> null
        }
    }

    private fun create20mSwitcherUiModel(): HomeLayoutItemUiModel {
        return HomeLayoutItemUiModel(Home20mSwitcher(), HomeLayoutItemState.LOADED)
    }

    private fun create2HSwitcherUiModel(): HomeLayoutItemUiModel {
        return HomeLayoutItemUiModel(Home2hSwitcher(), HomeLayoutItemState.LOADED)
    }
}