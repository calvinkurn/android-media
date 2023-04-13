package com.tokopedia.mvc.data.mapper


import com.tokopedia.mvc.data.response.ShopBasicDataResponse
import com.tokopedia.mvc.domain.entity.ShopData
import javax.inject.Inject

class ShopBasicDataMapper @Inject constructor() {

    fun map(response: ShopBasicDataResponse): ShopData {
        return ShopData(
            response.shopBasicData.result.domain,
            response.shopBasicData.result.logo,
            response.shopBasicData.result.name,
        )
    }
}
