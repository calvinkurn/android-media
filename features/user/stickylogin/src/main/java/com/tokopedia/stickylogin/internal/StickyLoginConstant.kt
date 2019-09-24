package com.tokopedia.stickylogin.internal

object StickyLoginConstant {

    enum class Page {
        HOME {
            override fun toString() = "home"
        },
        PDP {
            override fun toString() = "pdp"
        },
        SHOP {
            override fun toString() = "shop"
        }
    }

    const val PARAMS_PAGE = "page"

    const val REMOTE_CONFIG_FOR_HOME = "android_customer_sticky_login_home"
    const val REMOTE_CONFIG_FOR_PDP = "android_customer_sticky_login_pdp"
    const val REMOTE_CONFIG_FOR_SHOP = "android_customer_sticky_login_shop"
}