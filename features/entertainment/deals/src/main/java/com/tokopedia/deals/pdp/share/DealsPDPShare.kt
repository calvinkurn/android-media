package com.tokopedia.deals.pdp.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.deals.R
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.kotlin.extensions.view.ZERO
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

class DealsPDPShare(private val activity: WeakReference<Activity>) {

    companion object {
        private const val TYPE = "text/plain"
        private const val WEB_LINK_PATH = "deals/i/"
        private const val APP_LINK_PATH = "tokopedia://deals/"
    }

    private val remoteConfig by lazy(LazyThreadSafetyMode.NONE) { FirebaseRemoteConfigImpl(activity.get()) }
    private fun isBranchUrlActive() = remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true)

    fun shareEvent(data: ProductDetailData, titleShare: String, context: Context, loadShare: () -> Unit, doneLoadShare: () -> Unit) {
        generateBranchLink(data, context, titleShare, loadShare, doneLoadShare)
    }

    private fun openIntentShare(title: String, titleShare: String, url: String, context: Context) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, titleShare)
            putExtra(Intent.EXTRA_TEXT, url)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }
        activity.get()?.startActivity(
            Intent.createChooser(
                shareIntent,
                context.resources.getString(R.string.deals_brand_detail_share_title)
            )
        )
    }

    private fun generateBranchLink(data: ProductDetailData, context: Context, titleShare: String, loadShare: () -> Unit, doneLoadShare: () -> Unit) {
        loadShare()
        if (isBranchUrlActive()) {
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                    Int.ZERO,
                    pdpToLinkerDataMapper(data, context),
                    object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            openIntentShare(data.title, titleShare, linkerShareData.shareContents, context)
                            doneLoadShare()
                        }

                        override fun onError(linkerError: LinkerError) {
                            doneLoadShare()
                        }
                    }
                )
            )
        } else {
            openIntentShare(
                data.title,
                titleShare,
                TkpdBaseURL.WEB_DOMAIN + WEB_LINK_PATH + data.seoUrl,
                context
            )
            doneLoadShare()
        }
    }

    private fun pdpToLinkerDataMapper(data: ProductDetailData, context: Context): LinkerShareData {
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                id = data.id
                name = data.displayName
                description = data.longRichDesc
                type = LinkerData.ENTERTAINMENT_TYPE
                ogUrl = null
                imgUri = data.imageApp
                deepLink = APP_LINK_PATH + data.seoUrl
                uri = data.webUrl
                desktopUrl = data.webUrl
            }
        }
    }
}
