package com.tokopedia.topchat.chatroom.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst

class TopChatRoomActivity() {

    companion object {

        public val REQUEST_CODE_CHAT_IMAGE = 2325
        val MAX_SIZE_IMAGE_PICKER = 5
        val CHAT_DELETED_RESULT_CODE = 101
        val CHAT_GO_TO_SHOP_DETAILS_REQUEST = 202
        val LABEL_USER = "Pengguna"
        val LABEL_SELLER = "Penjual"
        val ROLE_SELLER = "shop"
        val ROLE_USER = "user"

        /**
         * To create intent with header already initialized.
         */
        @JvmStatic
        fun getCallingIntent(context: Context, messageId: String, name: String,
                             label: String, senderId: String, role: String, mode: Int,
                             keyword: String, image: String): Intent {
            return Intent()
        }

        @JvmStatic
        fun getAskSellerIntent(context: Context, toShopId: String,
                               shopName: String, source: String, avatar: String): Intent {
            return Intent()

        }

        @JvmStatic
        fun getAskSellerIntent(context: Context, toShopId: String, shopName: String,
                               customSubject: String, customMessage: String, source: String,
                               avatar: String): Intent {
            return Intent()
        }

        @JvmStatic
        fun getAskUserIntent(context: Context, userId: String,
                             userName: String, source: String,
                             avatar: String): Intent {
            return Intent()

        }

        @JvmStatic
        fun getAskBuyerIntent(context: Context, toUserId: String, customerName: String,
                              customSubject: String, customMessage: String, source: String,
                              avatar: String): Intent {
            return Intent()
        }

    }

    object DeepLinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.TOPCHAT)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            return Intent()
        }

    }
}
