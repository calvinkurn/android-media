package com.tokopedia.tokopedianow.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.requests.LinkerShareRequest
import com.tokopedia.linker.utils.AffiliateLinkType
import com.tokopedia.tokopedianow.common.model.ShareTokonow
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.model.ShareModel
import java.util.*

object TokoNowUniversalShareUtil {
    private fun linkerDataMapper(shareTokoNowData: ShareTokonow?): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = shareTokoNowData?.id.orEmpty()
        linkerData.name = shareTokoNowData?.specificPageName.orEmpty()
        linkerData.uri = shareTokoNowData?.sharingUrl.orEmpty()
        linkerData.description = shareTokoNowData?.specificPageDescription.orEmpty()
        linkerData.isThrowOnError = true
        linkerData.type = shareTokoNowData?.linkerType.orEmpty()
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    fun shareData(context: Context?, shareTxt: String?, pageUri: String?) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, shareTxt + "\n" + pageUri)
        context?.startActivity(Intent.createChooser(share, shareTxt))
    }

    fun shareRequest(context: Context?, shareTokonow: ShareTokonow?): LinkerShareRequest<*>? {
        return LinkerUtils.createShareRequest(0, linkerDataMapper(shareTokonow), object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult) {
                if (linkerShareData.url != null) {
                    shareData(
                        context = context,
                        shareTxt = String.format(Locale.getDefault(), "%s %s", shareTokonow?.sharingText, linkerShareData.shareUri),
                        pageUri = linkerShareData.url
                    )
                }
            }

            override fun onError(linkerError: LinkerError) {
                shareData(
                    context = context,
                    shareTxt = shareTokonow?.sharingText.orEmpty(),
                    pageUri = shareTokonow?.sharingUrl.orEmpty()
                )
            }
        })
    }

    fun shareOptionRequest(
        shareModel: ShareModel,
        shareTokoNowData: ShareTokonow?,
        activity: Activity?,
        view: View?,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
        val linkerShareData = linkerDataMapper(shareTokoNowData)
        linkerShareData.linkerData.apply {
            id = ShopIdProvider.getShopId()
            type = LinkerData.SHOP_TYPE
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            isThrowOnError = false
            if(shareModel.ogImgUrl != null && shareModel.ogImgUrl!!.isNotEmpty()) {
                ogImageUrl = shareModel.ogImgUrl
            }
            isAffiliate = shareModel.isAffiliate
            linkAffiliateType = AffiliateLinkType.SHOP.value
        }
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    val shareString = String.format(Locale.getDefault(), "%s %s", shareTokoNowData?.sharingText, linkerShareData?.shareUri)
                    SharingUtil.executeShareIntent(shareModel, linkerShareData, activity, view, shareString)
                    onSuccess.invoke()
                }

                override fun onError(linkerError: LinkerError?) {
                    onError.invoke()
                }
            } )
        )
    }
}
