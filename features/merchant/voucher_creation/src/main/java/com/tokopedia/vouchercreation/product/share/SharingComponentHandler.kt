package com.tokopedia.vouchercreation.product.share

import android.text.TextUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.universal_sharing.view.model.ShareModel
import javax.inject.Inject

class SharingComponentHandler @Inject constructor() {

    fun generateLinkerShareData(
        shopId : String,
        shopDomain : String,
        shareModel: ShareModel,
        title : String,
        description: String
    ): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            id = shopId
            linkerData.type = LinkerData.SHOP_TYPE
            name = title
            uri = "https://www.tokopedia.com/${shopDomain}"
            ogTitle = title
            ogDescription = description
            if (!TextUtils.isEmpty(shareModel.ogImgUrl)) {
                ogImageUrl = shareModel.ogImgUrl
            }
            isThrowOnError = true
        }
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }


}