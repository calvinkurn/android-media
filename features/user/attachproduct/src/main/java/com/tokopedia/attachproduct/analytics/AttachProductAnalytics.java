package com.tokopedia.attachproduct.analytics;

import com.tokopedia.attachproduct.view.tracking.AttachProductEventTracking;

/**
 * Created by Hendri on 02/03/18.
 */

public class AttachProductAnalytics {

    public static class Event {
        public static final String CLICK_CHAT_DETAIL = "ClickChatDetail";
    }

    public static class Category {
        public static final String CHAT_DETAIL = "chat detail";
    }

    public static class Action {
        public static final String CLICK_PRODUCT_IMAGE = "click on product image";
        public static final String CHECK_PRODUCT = "click one of the product";
    }

    public static class Label {

    }

    public static AttachProductEventTracking getEventCheckProduct() {
        return new AttachProductEventTracking(
                Event.CLICK_CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CHECK_PRODUCT,
                ""
        );
    }

    public static AttachProductEventTracking getEventCheckProductTalk(int productId) {
        return new AttachProductEventTracking(
                "clickInboxChat",
                "inbox - talk",
                "attach product",
                String.valueOf(productId)
        );
    }

    public static AttachProductEventTracking getEventClickChatAttachedProductImage() {
        return new AttachProductEventTracking(
                Event.CLICK_CHAT_DETAIL,
                Category.CHAT_DETAIL,
                Action.CLICK_PRODUCT_IMAGE,
                ""
        );
    }
}
