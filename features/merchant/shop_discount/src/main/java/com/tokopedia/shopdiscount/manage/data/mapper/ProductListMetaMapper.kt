package com.tokopedia.shopdiscount.manage.data.mapper

import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListMetaResponse
import com.tokopedia.shopdiscount.manage.domain.entity.DiscountStatusMeta
import javax.inject.Inject

class ProductListMetaMapper @Inject constructor() {

    fun map(
        tab: List<GetSlashPriceProductListMetaResponse.GetSlashPriceProductListMeta.Data.Tab>
    ): List<DiscountStatusMeta> {
        return tab.map { status ->
            DiscountStatusMeta(status.id, status.name, status.value)
        }
    }

}