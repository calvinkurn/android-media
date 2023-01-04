package com.tokopedia.shop.search.widget.share

import android.app.Activity
import android.content.Intent
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.shop.R

class ShopShare(private val activity: Activity) {

    companion object {
        private const val TYPE = "text/plain"
    }

    fun share(data: ShopShareData) {
        generateBranchLink(data)
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
        activity.startActivity(Intent.createChooser(shareIntent, activity.getString(com.tokopedia.feedcomponent.R.string.other)))
    }

    private fun generateBranchLink(data: ShopShareData) {
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0,
                productDataToLinkerDataMapper(data),
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult) {
                        openIntentShare(data.shopShareLabel, linkerShareData.shareContents)
                    }

                    override fun onError(linkerError: LinkerError) {}
                }
            )
        )
    }

    private fun productDataToLinkerDataMapper(shopShareData: ShopShareData): LinkerShareData {
        return LinkerShareData().apply {
            linkerData = LinkerData().apply {
                type = LinkerData.SHOP_TYPE
                name = shopShareData.shopShareLabel
                textContent = shopShareData.shopShareLabel
                custmMsg = shopShareData.shopShareLabel
                uri = shopShareData.shopUrl
                id = shopShareData.shopId
            }
        }
    }
}
