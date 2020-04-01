package com.tokopedia.sellerhomedrawer.data.constant

import com.tokopedia.url.TokopediaUrl

class SellerBaseUrl {

    companion object {
        @JvmStatic
        val DIGITAL_WEBSITE_DOMAIN = ""
        @JvmStatic
        val MOBILE =  "/mobile"
        @JvmStatic
        val HOSTNAME = TokopediaUrl.getInstance().MOBILEWEB
        @JvmStatic
        val RESO_INBOX = "resolution-center/inbox/"
        @JvmStatic
        val RESO_INBOX_SELLER = RESO_INBOX + "seller" + MOBILE
    }

}