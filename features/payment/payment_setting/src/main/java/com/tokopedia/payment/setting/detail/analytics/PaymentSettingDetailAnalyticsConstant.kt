package com.tokopedia.payment.setting.detail.analytics

class PaymentSettingDetailAnalyticsConstant {

    object Key {
        const val TRACKER_ID = "trackerId"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val USER_ID = "userId"
    }

    object Value {
        const val TRACKER_ID_42699 = "42699"
        const val TRACKER_ID_42698 = "42698"
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
        const val VIEW_CARD_DETAIL = "view card detail"
        const val CLICK_DELETE_CARD = "click delete card"
    }
}
