package com.tokopedia.payment.setting.authenticate.analytics

class PaymentSettingAuthenticateAnalyticsConstant {
    object Key {
        const val TRACKER_ID = "trackerId"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val USER_ID = "userId"
    }

    object Value {
        const val TRACKER_ID_42704 = "TRACKER_ID_42704"
        const val TRACKER_ID_42703 = "42703"
        const val TRACKER_ID_42702 = "42702"
        const val PAYMENT = "payment"
        const val TOKOPEDIAMARKETPLACE = "tokopediamarketplace"
    }

    object Event {
        const val EVENT_VIEW_PAYMENT_IRIS = "viewPaymentIris"
        const val EVENT_CLICK_PAYMENT = "clickPayment"
    }

    object Category {
        const val PAYMENT_SETTING_PAGE = "payment setting page"
    }

    object Action {
        const val VIEW_VERIFICATION_METHOD = "view verification method"
        const val CLICK_ON_VERIFICATION_METHOD = "click radio button on verification method"
        const val CLICK_SAVE = "click save on verification method"
    }
}
