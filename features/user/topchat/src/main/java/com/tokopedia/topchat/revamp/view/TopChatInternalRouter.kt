package com.tokopedia.topchat.revamp.view

import android.content.Context
import android.content.Intent
import com.tokopedia.attachproduct.view.activity.AttachProductActivity

/**
 * @author by nisie on 07/01/19.
 */
open class TopChatInternalRouter {

    object Companion {

        const val CHAT_DELETED_RESULT_CODE = 111

        const val TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY = "SELECTED_INVOICE"

        fun getAttachProductIntent(context: Context, shopId: String, shopName: String,
                                   isSeller: Boolean): Intent {
            return AttachProductActivity.createInstance(context, shopId, shopName, isSeller)
        }
    }
}