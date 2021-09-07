package com.tokopedia.sellerhome.view.viewhelper

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.sellerhome.R
import com.tokopedia.universal_sharing.view.bottomsheet.ClipboardHandler
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import java.io.File
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 06/09/21
 */

class ShopShareHelper @Inject constructor(
    private val userSession: UserSessionInterface
) {

    companion object {
        private const val TYPE_TEXT = "text/plain"
        private const val TYPE_IMAGE = "image/*"
        private const val CLIP_DATA_LABEL = ""
        private const val LINKER_REQUEST_EVENT_ID = 0
    }

    fun onShareOptionClicked(
        activity: Activity,
        data: DataModel,
        callback: (isSuccess: Boolean) -> Unit
    ) {
        val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
            type = LinkerData.SHOP_TYPE
            uri = data.shopCoreUrl
            id = userSession.shopId
        })

        val linkShareRequest = LinkerUtils.createShareRequest(
            LINKER_REQUEST_EVENT_ID,
            linkerShareData,
            getShopShareCallback(activity, data, callback)
        )

        LinkerManager.getInstance().executeShareRequest(linkShareRequest)
    }

    private fun getShopShareCallback(
        activity: Activity,
        data: DataModel,
        callback: (isSuccess: Boolean) -> Unit
    ): ShareCallback {
        val shareModel = data.shareModel

        return object : ShareCallback {

            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                shareModel.appIntent?.removeExtra(Intent.EXTRA_STREAM)
                shareModel.appIntent?.removeExtra(Intent.EXTRA_TEXT)

                when (shareModel) {
                    is ShareModel.CopyLink -> {
                        linkerShareData?.url?.let { url ->
                            ClipboardHandler().copyToClipboard(activity, url)
                        }
                        activity.runOnUiThread {
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.sah_share_action_copy_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    is ShareModel.Instagram, is ShareModel.Facebook -> {
                        val shopImageFileUri = getShopImageFileUri(activity, data.shopImageFilePath)
                        shareModel.appIntent?.clipData =
                            ClipData.newRawUri(CLIP_DATA_LABEL, shopImageFileUri)
                        activity.startActivity(shareModel.appIntent?.apply {
                            putExtra(Intent.EXTRA_STREAM, shopImageFileUri)
                        })
                    }
                    is ShareModel.Whatsapp -> {
                        activity.startActivity(shareModel.appIntent?.apply {
                            val shopImageFileUri =
                                getShopImageFileUri(activity, data.shopImageFilePath)
                            shareModel.appIntent?.clipData =
                                ClipData.newRawUri(CLIP_DATA_LABEL, shopImageFileUri)
                            putExtra(Intent.EXTRA_STREAM, shopImageFileUri)
                            type = MimeType.TEXT.type
                            putExtra(
                                Intent.EXTRA_TEXT, activity.getString(
                                    R.string.sah_share_text_with_link,
                                    userSession.shopName,
                                    linkerShareData?.shareContents
                                )
                            )
                        })
                    }
                    is ShareModel.Others -> {
                        activity.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                            val shopImageFileUri =
                                getShopImageFileUri(activity, data.shopImageFilePath)
                            shareModel.appIntent?.clipData =
                                ClipData.newRawUri(CLIP_DATA_LABEL, shopImageFileUri)
                            type = MimeType.IMAGE.type
                            putExtra(Intent.EXTRA_STREAM, shopImageFileUri)
                            type = MimeType.TEXT.type
                            putExtra(
                                Intent.EXTRA_TEXT, activity.getString(
                                    R.string.sah_share_text_with_link,
                                    userSession.shopName,
                                    linkerShareData?.shareContents
                                )
                            )
                        }, activity.getString(R.string.sah_share_to_social_media_text)))
                    }
                    else -> {
                        activity.startActivity(shareModel.appIntent?.apply {
                            putExtra(
                                Intent.EXTRA_TEXT, activity.getString(
                                    R.string.sah_share_text_with_link,
                                    userSession.shopName,
                                    linkerShareData?.shareContents
                                )
                            )
                        })
                    }
                }

                callback(true)
            }

            override fun onError(linkerError: LinkerError?) {}
        }
    }

    private fun getShopImageFileUri(activity: Activity, shopImageFilePath: String): Uri {
        return MethodChecker.getUri(activity, File(shopImageFilePath))
    }

    enum class MimeType(val type: String) {
        TEXT(TYPE_TEXT),
        IMAGE(TYPE_IMAGE)
    }

    data class DataModel(
        val shareModel: ShareModel,
        val shopImageFilePath: String,
        val shopCoreUrl: String
    )
}