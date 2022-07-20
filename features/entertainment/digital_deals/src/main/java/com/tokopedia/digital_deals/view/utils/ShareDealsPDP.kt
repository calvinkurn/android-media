package com.tokopedia.digital_deals.view.utils

import android.app.Activity
import android.content.Intent
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.lang.ref.WeakReference
import com.tokopedia.digital_deals.data.source.DealsUrl

class ShareDealsPDP (private val activity: WeakReference<Activity>) {

    companion object {
        private const val TYPE = "text/plain"
    }

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity.get()) }
    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    fun shareDealsPDP(seoUrl: String, displayName: String, imageUrl: String, webUrl: String, titleShare: String){
        generateBranchLink(seoUrl, displayName, imageUrl, webUrl, titleShare)
    }

    private fun generateBranchLink(seoUrl: String, displayName: String, imageUrl: String, webUrl: String, titleShare: String) {
        if (isBranchUrlActive()) {
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0,
                    pdpToLinkerDataMapper(seoUrl, displayName, imageUrl, webUrl), object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            openIntentShare(displayName, linkerShareData.shareContents, titleShare)
                        }

                        override fun onError(linkerError: LinkerError) {
                        }
                    })
            )
        } else {
            openIntentShare(displayName, webUrl, titleShare)
        }
    }

    private fun pdpToLinkerDataMapper(seoUrl: String, displayName: String, imageUrl: String, webUrl: String): LinkerShareData {
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                name = displayName
                type = LinkerData.ENTERTAINMENT_TYPE
                ogUrl = null
                imgUri = imageUrl
                deepLink = DealsUrl.AppLink.DIGITAL_DEALS + "/" + seoUrl
                uri = webUrl
                desktopUrl = webUrl
            }
        }
    }

    private fun openIntentShare(title: String, url:String, titleShare: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, url)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }
        activity.get()?.startActivity(Intent.createChooser(shareIntent, titleShare))
    }


}