package com.tokopedia.shop.flash_sale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.flash_sale.data.response.GetSellerProductListMetaResponse
import com.tokopedia.shop.flash_sale.domain.entity.ProductListMeta
import javax.inject.Inject

class SellerCampaignListMetaMapper @Inject constructor() {

    fun map(data: GetSellerProductListMetaResponse): List<ProductListMeta> {
        return data.getSellerCampaignListMeta.tab.map {
            ProductListMeta(it.id.toIntOrZero(), it.totalCampaign, it.name, it.status)
        }
    }

}