package com.tokopedia.salam.umrah.common.util

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity

class UmrahShare(val activity: Activity) {
    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity) }

    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    fun generateBranchLink(data: TravelAgent, preBuildImage: () -> Unit, postBuildImage: () -> Unit) {
        preBuildImage()
        if (isBranchUrlActive()) {
            LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                    umrahProductDatatoLinkerDataMapper(data), object : ShareCallback {
                override fun onError(linkerError: LinkerError) {
                    Log.d("LINKER_ERROR", linkerError.errorMessage)
                    postBuildImage()
                }

                override fun urlCreated(linkerShareData: LinkerShareResult) {
                    openIntentShare(data.name, linkerShareData.shareUri, linkerShareData.url)
                    postBuildImage()
                }
            }))
        }
    }

    private fun openIntentShare(title: String?, shareContent: String, shareUri: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_REFERRER, shareUri)
            putExtra(Intent.EXTRA_HTML_TEXT, shareContent)
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, shareContent)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        activity.startActivity(Intent.createChooser(shareIntent, "Bagikan Product Ini"))
    }

    private fun umrahProductDatatoLinkerDataMapper(data: TravelAgent): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = data.id
        linkerData.name = data.name
        linkerData.imgUri = data.imageUrl
        linkerData.type = LinkerData.PRODUCT_TYPE
        linkerData.uri = activity.resources.getString(R.string.umrah_agen_link_share, data.slugName)
        linkerData.deepLink = activity.resources.getString(R.string.umrah_agen_deeplink_share, data.slugName)

        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }
}