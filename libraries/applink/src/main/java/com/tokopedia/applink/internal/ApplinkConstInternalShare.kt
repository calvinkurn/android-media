package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalShare {

    private const val GLOBAL_SHARE = "${DeeplinkConstant.SCHEME_INTERNAL}://${ApplinkConstInternalGlobal.HOST_GLOBAL}"
    const val SHARE = "$GLOBAL_SHARE/share"

    const val ACTIVITY_RESULT_COPY_LINK = "copy_link"

    object ActivityResult {
        const val RESULT_CODE_COPY_LINK = 989
        const val RESULT_CODE_FAIL_GENERATE_AFFILIATE_LINK = 990
        const val PARAM_TOASTER_MESSAGE_SUCCESS_COPY_LINK = "toaster_message_success_copy_link"
        const val PARAM_TOASTER_MESSAGE_FAIL_GENERATE_AFFILIATE_LINK = "toaster_message_fail_generate_affiliate_link"
        const val PARAM_TOASTER_CTA_COPY_LINK = "toaster_cta_copy_link"
    }

    object PageType {
        const val ORDER_DETAIL = 6
    }

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
        const val LABEL_ACTION_CLICK_SHARE_ICON = "label_action_click_share_icon"
        const val LABEL_ACTION_CLICK_CLOSE_ICON = "label_action_click_close_icon"
        const val LABEL_ACTION_CLICK_CHANNEL = "label_action_click_channel"
        const val LABEL_IMPRESSION_BOTTOMSHEET = "label_impression_bottomsheet"
        const val LABEL_IMPRESSION_AFFILIATE_REGISTRATION = "label_impression_affiliate_registration"
        const val LABEL_ACTION_CLICK_AFFILIATE_REGISTRATION = "label_action_click_affiliate_registration"
    }
}
