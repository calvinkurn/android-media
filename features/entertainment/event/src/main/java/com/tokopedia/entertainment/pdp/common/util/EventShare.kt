package com.tokopedia.entertainment.pdp.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.lang.ref.WeakReference

class EventShare (private val activity: WeakReference<Activity>) {

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity.get()) }

    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    companion object {
        private const val TYPE = "text/plain"
        private const val ENT_WEB_LINK = "events/detail/"
        private const val ENT_APP_LINK = "tokopedia://events/"
    }

    fun shareEvent(data: ProductDetailData, titleShare: String, context: Context, loadShare: () -> Unit, doneLoadShare: () -> Unit) {
        generateBranchLink(data, titleShare, loadShare,doneLoadShare, context)
    }

    private fun openIntentShare(title: String, titleShare:String, url:String, context: Context) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, titleShare)
            putExtra(Intent.EXTRA_TEXT, url)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }
        activity.get()?.startActivity(Intent.createChooser(shareIntent, context.resources.getString(com.tokopedia.entertainment.R.string.ent_pdp_share_title_intent)))
    }

    private fun generateBranchLink(data: ProductDetailData, titleShare: String, loadShare: () -> Unit, doneLoadShare: () -> Unit, context: Context) {
        loadShare()
        if(isBranchUrlActive()) {
            LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(0,
                            travelDataToLinkerDataMapper(data, context), object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            openIntentShare(data.title, titleShare ,linkerShareData.shareContents, context)
                            doneLoadShare()
                        }

                        override fun onError(linkerError: LinkerError) {
                            doneLoadShare()
                        }
                    }))
        }else{
            openIntentShare(data.title, titleShare, TkpdBaseURL.WEB_DOMAIN + ENT_WEB_LINK +
                data.seoUrl, context)
            doneLoadShare()
        }
    }

    private fun travelDataToLinkerDataMapper(data: ProductDetailData, context: Context): LinkerShareData {
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                id = data.id
                name = data.title
                description = data.metaDescription
                ogUrl = null
                type = LinkerData.ENTERTAINMENT_TYPE
                imgUri = data.thumbnailApp
                uri = TkpdBaseURL.WEB_DOMAIN + ENT_WEB_LINK +
                    data.seoUrl
                deepLink = ENT_APP_LINK + data.seoUrl
                desktopUrl = TkpdBaseURL.WEB_DOMAIN + ENT_WEB_LINK +
                    data.seoUrl
            }
        }
    }
}
