package com.tokopedia.shareexperience.ui

object ShareExConst {
    object Applink {
        const val APPLINK = "tokopedia://share"

        const val DUMMY_APPLINK = "tokopedia://share?" +
            "referral_code=TARI123" +
            "&page_type=7" +
            "&default_url=https://tkp.me/GPL-TARI123" +
            "&default_impression_label=impression-label" +
            "&default_action_label=action-label"

        object Param {
            const val PAGE_TYPE = "page_type"
            const val DEFAULT_URL = "default_url"

            const val PRODUCT_ID = "product_id"
            const val CAMPAIGN_ID = "campaign_id"
            const val SHOP_ID = "shop_id"
            const val REVIEW_ID = "review_id"
            const val ATTACHMENT_ID = "attachment_id"

            const val REFERRAL_CODE = "referral_code"

            const val UTM_CAMPAIGN = "utm_campaign"
            const val EVENT_CATEGORY = "event_category"
            const val DEFAULT_IMPRESSION_LABEL = "default_impression_label"
            const val DEFAULT_ACTION_LABEL = "default_action_label"
            const val LABEL_IMPRESSION_BOTTOMSHEET = "label_impression_bottomsheet"
            const val LABEL_ACTION_CLICK_SHARE_ICON = "label_action_click_share_icon"
            const val LABEL_ACTION_CLICK_CLOSE_ICON = "label_action_click_close_icon"
            const val LABEL_ACTION_CLICK_CHANNEL = "label_action_click_channel"
            const val LABEL_IMPRESSION_AFFILIATE_REGISTRATION = "label_impression_affiliate_registration"
            const val LABEL_ACTION_CLICK_AFFILIATE_REGISTRATION = "label_action_click_affiliate_registration"
        }
    }
}
