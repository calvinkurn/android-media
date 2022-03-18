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
import com.tokopedia.tokopedianow.common.model.ShareTokonow
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.model.ShareModel

object TokoNowUniversalShareUtil {
    private fun linkerDataMapper(shareHomeTokonow: ShareTokonow?): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = shareHomeTokonow?.id.orEmpty()
        linkerData.name = shareHomeTokonow?.specificPageName.orEmpty()
        linkerData.uri = shareHomeTokonow?.sharingUrl.orEmpty()
        linkerData.description = shareHomeTokonow?.specificPageDescription.orEmpty()
        linkerData.isThrowOnError = true
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

    fun shareRequest(context: Context?, shareHomeTokonow: ShareTokonow?): LinkerShareRequest<*>? {
        return LinkerUtils.createShareRequest(0, linkerDataMapper(shareHomeTokonow), object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult) {
                if (linkerShareData.url != null) {
                    shareData(
                        context = context,
                        shareTxt = String.format("%s %s", shareHomeTokonow?.sharingText, linkerShareData.shareUri),
                        pageUri = linkerShareData.url
                    )
                }
            }

            override fun onError(linkerError: LinkerError) {
                shareData(
                    context = context,
                    shareTxt = shareHomeTokonow?.sharingText.orEmpty(),
                    pageUri = shareHomeTokonow?.sharingUrl.orEmpty()
                )
            }
        })
    }

    fun shareOptionRequest(shareModel: ShareModel, shareHomeTokonow: ShareTokonow?, activity: Activity?, view: View?, onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        val linkerShareData = linkerDataMapper(shareHomeTokonow)
        linkerShareData.linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            isThrowOnError = false
            if(shareModel.ogImgUrl != null && shareModel.ogImgUrl!!.isNotEmpty()) {
                ogImageUrl = shareModel.ogImgUrl
            }
        }
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    val shareString = String.format("%s %s", shareHomeTokonow?.sharingText, linkerShareData?.shareUri)
                    SharingUtil.executeShareIntent(shareModel, linkerShareData, activity, view, shareString)
                    onSuccess.invoke()
                }

                override fun onError(linkerError: LinkerError?) {
                    onError.invoke()
                }
            })
        )
    }
}