package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.ShopShowcasesByShopIDResponse
import com.tokopedia.mvc.domain.entity.ShopShowcase
import javax.inject.Inject

class ShopShowcasesByShopIDMapper @Inject constructor() {

    fun map(response: ShopShowcasesByShopIDResponse): List<ShopShowcase> {
        return response.shopShowcasesByShopID.result.map { showcase ->
            ShopShowcase(
                showcase.id.toLongOrZero(),
                showcase.alias,
                showcase.name,
                showcase.type,
                isSelected = false
            )
        }
    }
}
