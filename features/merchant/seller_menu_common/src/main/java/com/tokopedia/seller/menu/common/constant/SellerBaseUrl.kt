package com.tokopedia.seller.menu.common.constant

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl

class SellerBaseUrl {

    companion object {
        const val MOBILE =  "/mobile"
        @JvmField
        val HOSTNAME = TokopediaUrl.getInstance().MOBILEWEB
        @JvmField
        val SELLER_HOSTNAME = TokopediaUrl.getInstance().SELLER
        const val RESO_INBOX = "resolution-center/inbox/"
        const val SELLER_EDU = "edu/articles/"
        const val PRINTING = "jasa/print/kemasan-produk"
        const val RESO_INBOX_SELLER = RESO_INBOX + "seller" + MOBILE

        const val NEW_MEMBERSHIP_SCHEME_PATH = "skema-keanggotaan-baru"

        const val ADMIN_ERROR_ILLUSTRATION = "https://images.tokopedia.net/android/others/ic_admin_no_permission.png"

        const val APPLINK_FORMAT_ALLOW_OVERRIDE = "%s?allow_override=%b&url=%s"

        fun getNewMembershipSchemeApplink(): String {
            val url = "${SELLER_HOSTNAME}${NEW_MEMBERSHIP_SCHEME_PATH}"
            return String.format(APPLINK_FORMAT_ALLOW_OVERRIDE, ApplinkConst.WEBVIEW, false, url)
        }

    }

}
