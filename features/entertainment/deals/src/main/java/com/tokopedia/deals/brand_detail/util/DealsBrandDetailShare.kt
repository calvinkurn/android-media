package com.tokopedia.deals.brand_detail.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.deals.R
import com.tokopedia.deals.brand_detail.data.Brand
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

class DealsBrandDetailShare (private val activity: WeakReference<Activity>) {

    companion object {
        private const val TYPE = "text/plain"
    }
    private val remoteConfig by lazy { FirebaseRemoteConfigImpl(activity.get()) }
    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)


    fun shareEvent(data: Brand, titleShare: String, context: Context, loadShare: () -> Unit, doneLoadShare: () -> Unit) {
        generateBranchLink(data,  context, titleShare, loadShare, doneLoadShare)
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
        activity.get()?.startActivity(Intent.createChooser(shareIntent, context.resources.getString(R.string.deals_brand_detail_share_title)))
    }

    private fun generateBranchLink(data: Brand, context: Context, titleShare: String, loadShare: () -> Unit, doneLoadShare: () -> Unit) {
        loadShare()
        if (isBranchUrlActive()) {
            LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(0,
                            brandDetailToLinkerDataMapper(data, context), object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            openIntentShare(data.title, titleShare ,linkerShareData.shareContents, context)
                            doneLoadShare()
                        }

                        override fun onError(linkerError: LinkerError) {
                            doneLoadShare()
                        }
                    }))
        } else {
            openIntentShare(data.title, titleShare, TkpdBaseURL.WEB_DOMAIN + context.resources.getString(R.string.deals_brand_detail_share_web_link, data.seoUrl), context)
            doneLoadShare()
        }

    }

    private fun brandDetailToLinkerDataMapper(data: Brand, context: Context): LinkerShareData {
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                id = data.id
                name = data.title
                description = data.description
                type = LinkerData.ENTERTAINMENT_TYPE
                ogUrl = null
                imgUri = data.featuredImage
                deepLink = context.resources.getString(R.string.deals_brand_detail_share_app_link, data.seoUrl)
                uri = TkpdBaseURL.WEB_DOMAIN + context.resources.getString(R.string.deals_brand_detail_share_web_link, data.seoUrl)
                desktopUrl = TkpdBaseURL.WEB_DOMAIN + context.resources.getString(R.string.deals_brand_detail_share_web_link, data.seoUrl)
            }
        }
    }

}