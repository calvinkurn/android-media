package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.recharge_component.model.denom.MenuDetailModel

interface RechargeCatalogMenuDetailRepository {
    suspend fun getMenuDetail(menuId: Int, isLoadFromCloud: Boolean = false): MenuDetailModel
}