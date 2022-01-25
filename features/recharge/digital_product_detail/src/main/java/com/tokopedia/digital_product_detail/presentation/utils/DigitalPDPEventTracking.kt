package com.tokopedia.digital_product_detail.presentation.utils

interface DigitalPDPEventTracking {

    interface Additional {
        companion object {
            const val USER_ID = "userId"
            const val CURRENT_SITE = "currentSite"
            const val CURRENT_SITE_DIGITAL_RECHARGE = "tokopediadigitalRecharge"
            const val BUSINESS_UNIT = "businessUnit"
            const val BUSINESS_UNIT_RECHARGE = "recharge"
        }
    }

    interface Event {
        companion object {
            const val VIEW_DIGITAL_IRIS = "viewDigitalIris"
            const val CLICK_DIGITAL = "clickDigital"
        }
    }

    interface Category {
        companion object {
            const val DIGITAL_HOMEPAGE = "digital - homepage"
        }
    }

    interface Action {
        companion object {
            const val INPUT_MANUAL_NUMBER = "input manual number"
            const val INPUT_FROM_CONTACT = "input from contact"
            const val INPUT_FROM_FAVORITE_NUMBER = "input from favorite number"
            const val VIEW_FAVORITE_NUMBER_CHIP = "view favorite number chip"
            const val VIEW_FAVORITE_CONTACT_CHIP = "view favorite contact chip"
            const val CLICK_FAVORITE_NUMBER_CHIP = "click favorite number chip"
            const val CLICK_FAVORITE_CONTACT_CHIP = "click favorite contact chip"
            const val CLICK_AUTOCOMPLETE_FAVORITE_NUMBER = "click autocomplete fav number"
            const val CLICK_AUTOCOMPLETE_FAVORITE_CONTACT = "click autocomplete fav contact"
            const val CLEAR_INPUT_NUMBER = "click x on input number"
            const val CLICK_ON_CONTACT_ICON = "click on contact icon"
        }
    }
}