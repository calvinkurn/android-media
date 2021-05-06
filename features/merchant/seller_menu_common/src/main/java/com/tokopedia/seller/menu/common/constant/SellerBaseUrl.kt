package com.tokopedia.seller.menu.common.constant

import com.tokopedia.url.TokopediaUrl

class SellerBaseUrl {

    companion object {
        const val DIGITAL_WEBSITE_DOMAIN = ""
        const val MOBILE =  "/mobile"
        @JvmField
        val HOSTNAME = TokopediaUrl.getInstance().MOBILEWEB
        @JvmField
        val SELLER_HOSTNAME = TokopediaUrl.getInstance().SELLER
        const val RESO_INBOX = "resolution-center/inbox/"
        const val SELLER_EDU = "edu/articles/"
        const val SELLER_ORDER_PRIORITY = "edu/order-prioritas/"
        const val RESO_INBOX_SELLER = RESO_INBOX + "seller" + MOBILE

        const val ADMIN_ERROR_ILLUSTRATION = "https://ecs7.tokopedia.net/android/others/ic_admin_no_permission.png"
    }

}