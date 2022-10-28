package com.tokopedia.tokofood.feature.home.presentation.share

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
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.model.ShareModel

object TokoFoodUniversalShareUtil {

    private fun linkerDataMapper(shareHomeTokoFood: TokoFoodHomeShare?): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = shareHomeTokoFood?.id.orEmpty()
        linkerData.name = shareHomeTokoFood?.specificPageName.orEmpty()
        linkerData.uri = shareHomeTokoFood?.sharingUrl.orEmpty()
        linkerData.deepLink = shareHomeTokoFood?.sharingDeeplink.orEmpty()
        linkerData.description = shareHomeTokoFood?.specificPageDescription.orEmpty()
        linkerData.isThrowOnError = true
        linkerData.type = shareHomeTokoFood?.linkerType.orEmpty()
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

    fun shareRequest(context: Context?, shareHomeTokoFood: TokoFoodHomeShare?): LinkerShareRequest<*>? {
        return LinkerUtils.createShareRequest(0, linkerDataMapper(shareHomeTokoFood), object :
            ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult) {
                if (linkerShareData.url != null) {
                    shareData(
                        context = context,
                        shareTxt = String.format("%s %s", shareHomeTokoFood?.sharingText, linkerShareData.shareUri),
                        pageUri = linkerShareData.url
                    )
                }
            }

            override fun onError(linkerError: LinkerError) {
                shareData(
                    context = context,
                    shareTxt = shareHomeTokoFood?.sharingText.orEmpty(),
                    pageUri = shareHomeTokoFood?.sharingUrl.orEmpty()
                )
            }
        })
    }

    fun shareOptionRequest(shareModel: ShareModel, shareHomeTokoFood: TokoFoodHomeShare?, activity: Activity?, view: View?, onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        val linkerShareData = linkerDataMapper(shareHomeTokoFood)
        linkerShareData.linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            isThrowOnError = false
            if(shareModel.ogImgUrl != null && shareModel.ogImgUrl?.isNotEmpty() == true) {
                ogImageUrl = shareModel.ogImgUrl
            }
        }
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    val shareString = String.format("%s %s", shareHomeTokoFood?.sharingText, linkerShareData?.shareUri)
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