package com.tokopedia.mvc.presentation.share

import android.text.TextUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
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
        outgoingDescription : String,
        isProductVoucher: Boolean
    ): LinkerShareData {

        val destinationId = if (isProductVoucher) {
            "${shopId}/voucher/${galadrielVoucherId}?page_source=${ShareComponentConstant.FEATURE_NAME}"
        } else {
            "${shopId}?page_source=${ShareComponentConstant.FEATURE_NAME}"
        }

        val desktopUrl = if (isProductVoucher) {
            "https://www.tokopedia.com/${shopDomain}/voucher/${galadrielVoucherId}?page_source=${ShareComponentConstant.FEATURE_NAME}"
        } else {
            "https://www.tokopedia.com/${shopDomain}?page_source=${ShareComponentConstant.FEATURE_NAME}"
        }

        val appLink = if (isProductVoucher) {
            UriUtil.buildUri(ApplinkConst.SHOP_MVC_LOCKED_TO_PRODUCT, shopId, galadrielVoucherId.toString())
        } else {
            UriUtil.buildUri(ApplinkConst.SHOP, shopId)
        }

        val linkerData = LinkerData()
        linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            deepLink = appLink
            id = destinationId
            linkerData.type = LinkerData.SHOP_TYPE
            name = title
            uri = desktopUrl
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
