package com.tokopedia.stickylogin.internal

object StickyLoginConstant {

    enum class Page {
        HOME {
            override fun toString() = "header"
        },
        PDP {
            override fun toString() = "pdp"
        },
        SHOP {
            override fun toString() = "shop"
        }
    }

    const val PARAMS_PAGE = "page"
    const val LAYOUT_FLOATING = "floating"

    const val KEY_STICKY_LOGIN_WIDGET_HOME = "android_customer_sticky_login_home"
    const val KEY_STICKY_LOGIN_WIDGET_PDP = "android_customer_sticky_login_pdp"
    const val KEY_STICKY_LOGIN_WIDGET_SHOP = "android_customer_sticky_login_shop"

    /** Login Reminder **/
    const val KEY_STICKY_LOGIN_REMINDER_HOME = "android_customer_sticky_login_reminder_home"
    const val KEY_STICKY_LOGIN_REMINDER_PDP = "android_customer_sticky_login_reminder_pdp"
    const val KEY_STICKY_LOGIN_REMINDER_SHOP = "android_customer_sticky_login_reminder_shop"
}