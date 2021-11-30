package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.domain.model.ShopShareOtherResponse
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.OtherMenuShopShareData
import javax.inject.Inject

class OtherMenuShopShareMapper @Inject constructor() {

    fun mapToOtherMenuShopShareData(response: ShopShareOtherResponse): OtherMenuShopShareData? {
        return response.shopInfoByID.result.firstOrNull()?.let { data ->
            OtherMenuShopShareData(
                shopSnippetUrl = data.shopSnippetUrl,
                location = data.location,
                description = data.shopCore.description,
                tagline = data.shopCore.tagLine,
                coreUrl = data.shopCore.url,
                branchLinkDomain = data.branchLinkDomain
            )
        }
    }

}