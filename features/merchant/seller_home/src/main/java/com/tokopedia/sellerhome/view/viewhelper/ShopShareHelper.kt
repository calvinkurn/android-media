package com.tokopedia.sellerhome.view.viewhelper

import android.app.Activity
import android.view.View
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.sellerhome.R
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
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
        private val LINKER_REQUEST_EVENT_ID = Int.ZERO
    }

    fun onShareOptionClicked(
        activity: Activity,
        view: View?,
        data: DataModel,
        callback: (shareModel: ShareModel, isSuccess: Boolean) -> Unit
    ) {
        val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
            type = LinkerData.SHOP_TYPE
            uri = data.shopCoreUrl
            id = userSession.shopId
            //set and share in the Linker Data
            feature = data.shareModel.feature
            channel = data.shareModel.channel
            campaign = data.shareModel.campaign
            if (data.shareModel.ogImgUrl != null && data.shareModel.ogImgUrl?.isNotEmpty() == true) {
                ogImageUrl = data.shareModel.ogImgUrl
            }
        })

        val linkShareRequest = LinkerUtils.createShareRequest(
            LINKER_REQUEST_EVENT_ID,
            linkerShareData,
            getShopShareCallback(activity, view, data, callback)
        )

        LinkerManager.getInstance().executeShareRequest(linkShareRequest)
    }

    fun removeTemporaryShopImage(shopImageFilePath: String) {
        if (shopImageFilePath.isNotEmpty()) {
            File(shopImageFilePath).apply {
                if (exists()) {
                    delete()
                }
            }
        }
    }

    private fun getShopShareCallback(
        activity: Activity,
        view: View?,
        data: DataModel,
        callback: (shareModel: ShareModel, isSuccess: Boolean) -> Unit
    ): ShareCallback {
        val shareModel = data.shareModel

        return object : ShareCallback {

            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                val shareString = activity.getString(
                    R.string.sah_new_other_share_text,
                    userSession.shopName,
                    data.shopCoreUrl
                )
                shareModel.subjectName = userSession.shopName
                SharingUtil.executeShareIntent(
                    shareModel,
                    linkerShareData,
                    activity,
                    view,
                    shareString
                )

                callback(shareModel, true)
            }

            override fun onError(linkerError: LinkerError?) {}
        }
    }

    data class DataModel(
        val shareModel: ShareModel,
        val shopImageFilePath: String,
        val shopCoreUrl: String
    )
}