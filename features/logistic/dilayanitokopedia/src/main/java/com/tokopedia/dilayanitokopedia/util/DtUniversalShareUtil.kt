package com.tokopedia.dilayanitokopedia.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.DtShareUniversalUiModel
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
import java.util.*

object DtUniversalShareUtil {
    const val SHARE_LINK_TITLE = "Dilayani Tokopedia | Tokopedia"
    const val SHARE_LINK_URL = "https://www.tokopedia.com/dilayani-tokopedia"
    const val SHARE_LINK_THUMBNAIL_IMAGE =
        "https://images.tokopedia.net/img/coCfvv/2023/2/17/d6123177-827e-4843-be61-efbbeea5a658.jpg"
    const val SHARE_LINK_OG_IMAGE =
        "https://images.tokopedia.net/img/coCfvv/2023/2/17/d6123177-827e-4843-be61-efbbeea5a658.jpg"
    const val SHARE_LINK_PAGE_NAME = "DilayaniTokopedia"

    const val SHARE_LINK_LINKER_TYPE = "Dilayani-tokopedia"
    const val SHARE_LINK_PAGE_ID = "home"
    const val SHARE_LINK_DESCRIPTION = "Cek Dilayani Tokopedia! Belanja bebas ongkir dengan harga terbaik hanya di Tokopedia"

    const val SHARE = "share"

    private fun linkerDataMapper(shareData: DtShareUniversalUiModel?): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = shareData?.id.orEmpty()
        linkerData.name = shareData?.specificPageName.orEmpty()
        linkerData.uri = shareData?.sharingUrl.orEmpty()
        linkerData.description = shareData?.specificPageDescription.orEmpty()
        linkerData.isThrowOnError = true
        linkerData.type = shareData?.linkerType.orEmpty()
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

    fun shareRequest(context: Context?, shareData: DtShareUniversalUiModel?): LinkerShareRequest<*>? {
        return LinkerUtils.createShareRequest(
            0,
            linkerDataMapper(shareData),
            object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult) {
                    if (linkerShareData.url != null) {
                        shareData(
                            context = context,
                            shareTxt = String.format(
                                Locale.getDefault(),
                                "%s %s",
                                shareData?.sharingText,
                                linkerShareData.shareUri
                            ),
                            pageUri = linkerShareData.url
                        )
                    }
                }

                override fun onError(linkerError: LinkerError) {
                    shareData(
                        context = context,
                        shareTxt = shareData?.sharingText.orEmpty(),
                        pageUri = shareData?.sharingUrl.orEmpty()
                    )
                }
            }
        )
    }

    fun shareOptionRequest(
        shareModel: ShareModel,
        shareData: DtShareUniversalUiModel?,
        activity: Activity?,
        view: View?,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
        val linkerShareData = linkerDataMapper(shareData)

        linkerShareData.linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            isThrowOnError = false
            if (shareModel.ogImgUrl != null && shareModel.ogImgUrl?.isNotEmpty() == true) {
                ogImageUrl = shareModel.ogImgUrl
            }
        }
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0,
                linkerShareData,
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult?) {
                        val shareString = String.format(
                            Locale.getDefault(),
                            "%s %s",
                            SHARE_LINK_DESCRIPTION,
                            linkerShareData?.shareUri.orEmpty()
                        )
                        SharingUtil.executeShareIntent(shareModel, linkerShareData, activity, view, shareString)
                        onSuccess.invoke()
                    }

                    override fun onError(linkerError: LinkerError?) {
                        onError.invoke()
                    }
                }
            )
        )
    }
}
