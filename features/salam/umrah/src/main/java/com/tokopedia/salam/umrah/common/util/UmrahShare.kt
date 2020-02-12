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


class UmrahShare(private val activity: Activity) {

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity) }

    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    companion object {
        private const val TYPE = "text/plain"
    }

    fun share(data: TravelAgent, loadShare: () -> Unit, doneLoadShare: () -> Unit) {
        generateBranchLink(data,loadShare,doneLoadShare)
    }

    private fun openIntentShare(title: String, shareContent: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, shareContent)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }
        activity.startActivity(Intent.createChooser(shareIntent, "Bagikan Produk Ini"))
    }

    private fun generateBranchLink(data: TravelAgent, loadShare: () -> Unit, doneLoadShare: () -> Unit) {
        loadShare()
        if(isBranchUrlActive())
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0,
                        travelDataToLinkerDataMapper(data), object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult) {
                        openIntentShare(data.name, linkerShareData.shareContents)
                        doneLoadShare()
                    }

                    override fun onError(linkerError: LinkerError) {
                        Log.d("ERRORLINKER",linkerError.errorMessage)
                        doneLoadShare()
                    }
                }))
    }

    private fun travelDataToLinkerDataMapper(data: TravelAgent): LinkerShareData {
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                id = data.id
                name = data.name
                description = data.name
                ogUrl = null
                imgUri = data.imageUrl
                uri = activity.resources.getString(R.string.umrah_agen_link_share, data.slugName)
                deepLink = activity.resources.getString(R.string.umrah_agen_deeplink_share, data.slugName)
            }
        }
    }
}