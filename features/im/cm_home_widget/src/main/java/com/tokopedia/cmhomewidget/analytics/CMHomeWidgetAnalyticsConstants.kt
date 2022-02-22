package com.tokopedia.cmhomewidget.analytics

object CMHomeWidgetAnalyticsConstants {

    internal interface Key {
        companion object {
            const val CURRENT_SITE = "currentSite"
            const val BUSINESS_UNIT = "businessUnit"
            const val CAMPAIGN_CODE = "campaignCode"
            const val USER_ID = "userId"
            const val PARENT_ID = "parent_id"
            const val CAMPAIGN_ID = "campaign_id"
            const val SESSION_ID = "session_id"
            const val NOTIFICATION_ID = "notification_id"
            const val MESSAGE_ID = "message_id"
            const val IS_SILENT = "is_silent"
        }
    }

    internal interface CurrentSite {
        companion object {
            const val VALUE_CURRENT_SITE = "tokopediamarketplace"
        }
    }

    internal interface BusinessUnit {
        companion object {
            const val VALUE_BUSINESS_UNIT = "cm"
        }
    }

    internal interface Event {
        companion object {
            const val WIDGET_RECEIVED = "WidgetReceived"
            const val WIDGET_CLICKED = "WidgetClicked"
        }
    }

    internal interface Action {
        companion object {
            const val CLICK_CTA = "click cta"
            const val RECEIVED = "received"
        }
    }

    internal interface Category {
        companion object {
            const val HOME_TO_DO_WIDGET = "home to do widget"
        }
    }

    internal interface IsSilent {
        companion object {
            const val TRUE = "true"
            const val FALSE = "false" //default value
        }
    }
}