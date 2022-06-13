package com.tokopedia.attachproduct.analytics

import com.tokopedia.attachproduct.view.tracking.AttachProductEventTracking

object AttachProductAnalytics {
    val eventCheckProduct: AttachProductEventTracking
        get() = AttachProductEventTracking(
            Event.CLICK_CHAT_DETAIL,
            Category.CHAT_DETAIL,
            Action.CHECK_PRODUCT,
            ""
        )

    fun getEventCheckProductTalk(productId: String): AttachProductEventTracking {
        return AttachProductEventTracking(
            "clickInboxChat",
            "inbox - talk",
            "attach product",
            productId
        )
    }

    val eventClickChatAttachedProductImage: AttachProductEventTracking
        get() {
            return AttachProductEventTracking(
                Event.CLICK_CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_PRODUCT_IMAGE,
                ""
            )
        }

    object Event {
        const val CLICK_CHAT_DETAIL: String = "ClickChatDetail"
    }

    object Category {
        const val CHAT_DETAIL: String = "chat detail"
    }

    object Action {
        const val CLICK_PRODUCT_IMAGE: String = "click on product image"
        const val CHECK_PRODUCT: String = "click one of the product"
    }
}
