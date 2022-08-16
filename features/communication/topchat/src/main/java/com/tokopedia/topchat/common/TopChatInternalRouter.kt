package com.tokopedia.topchat.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_WAREHOUSE_ID
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

open class TopChatInternalRouter {

    object Companion {

        const val CHAT_DELETED_RESULT_CODE = 111

        const val RESULT_INBOX_CHAT_PARAM_INDEX = "position"
        const val RESULT_INBOX_CHAT_PARAM_MOVE_TO_TOP = "move_to_top"
        const val RESULT_LAST_ITEM = "last_item"

        const val EXTRA_SHOP_STATUS_FAVORITE_FROM_SHOP = "SHOP_STATUS_FAVOURITE"

        const val RESULT_KEY_REPORT_USER = "result_key_report_user"
        const val RESULT_KEY_PAYLOAD_REPORT_USER = "result_key_payload_report_user"
        const val RESULT_REPORT_BLOCK_PROMO = "result_report_block_promo"
        const val RESULT_REPORT_BLOCK_USER = "result_report_block_user"
        const val RESULT_REPORT_TOASTER = "result_report_toaster"

        const val SOURCE_TOPCHAT = "topchat"

        fun getAttachProductIntent(
            context: Context,
            shopId: String,
            isSeller: Boolean,
            warehouseId: String
        ): Intent {
            val intent = RouteManager.getIntent(
                context, ApplinkConstInternalMarketplace.ATTACH_PRODUCT
            )
            intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY, shopId)
            intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY, isSeller)
            intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT)
            intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_WAREHOUSE_ID, warehouseId)
            return intent
        }

    }
}