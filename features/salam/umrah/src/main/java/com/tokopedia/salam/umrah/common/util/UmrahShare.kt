package com.tokopedia.salam.umrah.common.util

import android.app.Activity
import android.content.Context
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

    fun shareTravelAgent(data: TravelAgent, loadShare: () -> Unit, doneLoadShare: () -> Unit, context: Context) {
        generateBranchLink(data,loadShare,doneLoadShare,context)
    }

    private fun openIntentShare(title: String, shareContent: String, context:Context) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, context.getString(R.string.umrah_travel_share_name, title))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.umrah_travel_share_desc, title,title)+shareContent)
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.umrah_travel_share_name, title))
        }
        activity.startActivity(Intent.createChooser(shareIntent, "Bagikan Produk Ini"))
    }

    private fun generateBranchLink(data: TravelAgent, loadShare: () -> Unit, doneLoadShare: () -> Unit, context: Context) {
        loadShare()
        if(isBranchUrlActive()) {
            LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(0,
                            travelDataToLinkerDataMapper(data, context), object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            openIntentShare(data.name, linkerShareData.shareContents, context)
                            doneLoadShare()
                        }

                        override fun onError(linkerError: LinkerError) {
                            doneLoadShare()
                        }
                    }))
        }else{
            openIntentShare(data.name,activity.resources.getString(R.string.umrah_agen_link_share, data.slugName),context)
            doneLoadShare()
        }
    }

    private fun travelDataToLinkerDataMapper(data: TravelAgent, context: Context): LinkerShareData {
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                id = data.id
                name = context.getString(R.string.umrah_travel_share_name, data.name)
                description = context.getString(R.string.umrah_travel_share_desc, data.name,data.name)
                ogUrl = null
                imgUri = data.imageUrl
                uri = activity.resources.getString(R.string.umrah_agen_link_share, data.slugName)
                deepLink = activity.resources.getString(R.string.umrah_agen_deeplink_share, data.slugName)
            }
        }
    }
}