package com.tokopedia.stickylogin.common

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

    const val STICKY_PREF = "sticky_login_widget.pref"
    const val KEY_LAST_SEEN_AT_HOME = "last_seen_at_home"
    const val KEY_LAST_SEEN_AT_PDP = "last_seen_at_pdp"
    const val KEY_LAST_SEEN_AT_SHOP = "last_seen_at_shop"

    const val KEY_STICKY_LOGIN_WIDGET_HOME = "android_customer_sticky_login_home"
    const val KEY_STICKY_LOGIN_WIDGET_PDP = "android_customer_sticky_login_pdp"
    const val KEY_STICKY_LOGIN_WIDGET_SHOP = "android_customer_sticky_login_shop"

    /** Login Reminder **/
    const val STICKY_LOGIN_REMINDER_PREF = "sticky_login_reminder.pref"
    const val KEY_USER_NAME = "user_name"
    const val KEY_PROFILE_PICTURE = "profile_picture"

    const val KEY_STICKY_LOGIN_REMINDER_HOME = "android_customer_sticky_login_reminder_home"
    const val KEY_STICKY_LOGIN_REMINDER_PDP = "android_customer_sticky_login_reminder_pdp"
    const val KEY_STICKY_LOGIN_REMINDER_SHOP = "android_customer_sticky_login_reminder_shop"

    const val KEY_IS_REGISTER_FROM_STICKY_LOGIN = "is_register_from_sticky_login"
}