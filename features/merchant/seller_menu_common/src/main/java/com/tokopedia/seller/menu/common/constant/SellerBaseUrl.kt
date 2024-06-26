package com.tokopedia.seller.menu.common.constant

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.url.TokopediaUrl

class SellerBaseUrl {

    companion object {
        const val MOBILE =  "/mobile"
        @JvmField
        val HOSTNAME = TokopediaUrl.getInstance().MOBILEWEB
        @JvmField
        val SELLER_HOSTNAME = TokopediaUrl.getInstance().SELLER
        const val RESO_INBOX = "resolution-center/inbox/"
        const val SELLER_EDU = "edu"
        const val PRINTING = "jasa/print/kemasan-produk"
        const val RESO_INBOX_SELLER = RESO_INBOX + "seller" + MOBILE

        const val ADMIN_ERROR_ILLUSTRATION = TokopediaImageUrl.ERROR_ILLUSTRATION

        const val APPLINK_FORMAT_ALLOW_OVERRIDE = "%s?allow_override=%b&url=%s"

        private const val RM_EDU_SCHEME_PATH = "edu/regular-merchant/"

        fun getRegularMerchantEduApplink(): String {
            val url = "${SELLER_HOSTNAME}${RM_EDU_SCHEME_PATH}"
            return String.format(APPLINK_FORMAT_ALLOW_OVERRIDE, ApplinkConst.WEBVIEW, false, url)
        }

    }

}
