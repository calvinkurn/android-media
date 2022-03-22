package com.tokopedia.sellerhome.settings.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.sellerhome.settings.view.fragment.SellerEduWebviewFragment
import com.tokopedia.webview.download.BaseDownloadAppLinkActivity

class SellerEduWebviewActivity: BaseDownloadAppLinkActivity() {

    companion object {
        private val SELLER_EDU_URL = "${SellerBaseUrl.SELLER_HOSTNAME}${SellerBaseUrl.SELLER_EDU}"

        @JvmStatic
        fun createIntent(context: Context): Intent =
            Intent(context, SellerEduWebviewActivity::class.java).apply {
                data = Uri.parse(SELLER_EDU_URL)
            }
    }

    override fun getNewFragment(): Fragment {
        return SellerEduWebviewFragment.newInstance(SELLER_EDU_URL)
    }

}