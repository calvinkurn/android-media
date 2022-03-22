package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect

interface RechargeCatalogPrefixSelectRepository {
    suspend fun getOperatorList(menuId: Int): TelcoCatalogPrefixSelect
}