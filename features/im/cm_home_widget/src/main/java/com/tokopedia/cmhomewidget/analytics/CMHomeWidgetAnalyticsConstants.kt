package com.tokopedia.cmhomewidget.analytics

object CMHomeWidgetAnalyticsConstants {

    internal interface Key {
        companion object {
            const val CURRENT_SITE = "currentSite"
            const val BUSINESS_UNIT = "businessUnit"
            const val CAMPAIGN_CODE = "campaignCode"
            const val USER_ID = "userId"
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
}