package com.tokopedia.topchat.common

import android.content.Context
import android.content.Intent
import com.tokopedia.attachproduct.view.activity.AttachProductActivity

/**
 * @author by nisie on 07/01/19.
 */
open class TopChatInternalRouter {

    object Companion {

        const val CHAT_DELETED_RESULT_CODE = 111
        const val CHAT_READ_RESULT_CODE = 112

        const val REQUEST_CODE_USER_LOGIN_CART = 3214

        const val RESULT_INBOX_CHAT_PARAM_INDEX = "position"
        const val RESULT_INBOX_CHAT_PARAM_MUST_REFRESH = "must_refresh"
        const val RESULT_INBOX_CHAT_PARAM_MOVE_TO_TOP = "move_to_top"
        const val RESULT_LAST_ITEM = "last_item"

        const val EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP = "SHOP_STATUS_FAVOURITE"

        const val RESULT_KEY_REPORT_USER = "result_key_report_user"
        const val RESULT_KEY_PAYLOAD_REPORT_USER = "result_key_payload_report_user"
        const val RESULT_REPORT_BLOCK_PROMO = "result_report_block_promo"
        const val RESULT_REPORT_BLOCK_USER = "result_report_block_user"
        const val RESULT_REPORT_TOASTER = "result_report_toaster"


        const val TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY = "SELECTED_INVOICE"

        fun getAttachProductIntent(context: Context, shopId: String, shopName: String,
                                   isSeller: Boolean): Intent {
            return AttachProductActivity.createInstance(context, shopId, shopName, isSeller,
                    AttachProductActivity.SOURCE_TOPCHAT)
        }

    }
}