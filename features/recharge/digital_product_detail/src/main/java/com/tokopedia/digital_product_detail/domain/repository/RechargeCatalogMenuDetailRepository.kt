package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common.topupbills.favoritepdp.domain.model.MenuDetailModel

interface RechargeCatalogMenuDetailRepository {
    suspend fun getMenuDetail(menuId: Int): MenuDetailModel
}