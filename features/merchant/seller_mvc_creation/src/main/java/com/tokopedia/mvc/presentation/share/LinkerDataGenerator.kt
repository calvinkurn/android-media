package com.tokopedia.mvc.presentation.share

import android.text.TextUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.mvc.util.constant.ShareComponentConstant
import com.tokopedia.universal_sharing.view.model.ShareModel
import javax.inject.Inject

class LinkerDataGenerator @Inject constructor() {

    fun generate(
        galadrielVoucherId: Long,
        shopId : String,
        shopDomain : String,
        shareModel: ShareModel,
        title : String,
        outgoingDescription : String
    ): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            id = "${shopId}/voucher/${galadrielVoucherId}?page_source=${ShareComponentConstant.FEATURE_NAME}"
            linkerData.type = LinkerData.SHOP_TYPE
            name = title
            uri = "https://www.tokopedia.com/${shopDomain}/voucher/${galadrielVoucherId}?page_source=${ShareComponentConstant.FEATURE_NAME}"
            ogTitle = title
            ogDescription = outgoingDescription
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
