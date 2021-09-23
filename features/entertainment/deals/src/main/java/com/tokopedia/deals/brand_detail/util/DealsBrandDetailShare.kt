package com.tokopedia.deals.brand_detail.util

import android.app.Activity
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
import com.tokopedia.url.TokopediaUrl
import java.lang.ref.WeakReference

class DealsBrandDetailShare (private val activity: WeakReference<Activity>) {

    companion object {
        private const val TYPE = "text/plain"
    }

    fun shareEvent(data: Brand, titleShare: String, loadShare: () -> Unit, doneLoadShare: () -> Unit) {
        generateBranchLink(data, titleShare, loadShare, doneLoadShare)
    }

    private fun openIntentShare(title: String, titleShare:String, url:String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TITLE, titleShare)
            putExtra(Intent.EXTRA_TEXT, url)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }
        activity.get()?.startActivity(Intent.createChooser(shareIntent, "Bagikan Produk Ini"))
    }

    private fun generateBranchLink(data: Brand, titleShare: String, loadShare: () -> Unit, doneLoadShare: () -> Unit) {
        loadShare()
        LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(0,
                            brandDetailToLinkerDataMapper(data), object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            openIntentShare(data.title, titleShare ,linkerShareData.shareContents)
                            doneLoadShare()
                        }

                        override fun onError(linkerError: LinkerError) {
                            doneLoadShare()
                        }
                    }))

    }

    private fun brandDetailToLinkerDataMapper(data: Brand): LinkerShareData {
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                id = data.id
                name = data.title
                description = data.description
                ogUrl = null
                imgUri = data.featuredImage
                uri = "${TokopediaUrl.getInstance().WEB}deals/b/${data.seoUrl}"
                deepLink = activity.get()?.resources?.getString(R.string.deals_brand_detail_share_app_link, data.seoUrl)
            }
        }
    }

}