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
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

class EventShare (private val activity: Activity) {

    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity) }

    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    companion object {
        private const val TYPE = "text/plain"
    }

    fun shareEvent(data: ProductDetailData, loadShare: () -> Unit, doneLoadShare: () -> Unit, context: Context) {
        generateBranchLink(data,loadShare,doneLoadShare,context)
    }

    private fun openIntentShare(title: String, url:String, context:Context) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, context.getString(R.string.ent_pdp_share_title, title))
            putExtra(Intent.EXTRA_TEXT, url)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }
        activity.startActivity(Intent.createChooser(shareIntent, "Bagikan Produk Ini"))
    }

    private fun generateBranchLink(data: ProductDetailData, loadShare: () -> Unit, doneLoadShare: () -> Unit, context: Context) {
        loadShare()
        if(isBranchUrlActive()) {
            LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(0,
                            travelDataToLinkerDataMapper(data, context), object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            openIntentShare(data.title, linkerShareData.shareContents, context)
                            doneLoadShare()
                        }

                        override fun onError(linkerError: LinkerError) {
                            doneLoadShare()
                        }
                    }))
        }else{
            openIntentShare(data.title, data.webUrl, context)
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
                imgUri = data.thumbnailApp
                uri = activity.resources.getString(R.string.ent_pdp_share_web_link, data.seoUrl)
                deepLink = activity.resources.getString(R.string.ent_pdp_share_app_link, data.seoUrl)
            }
        }
    }
}