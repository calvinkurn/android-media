package com.tokopedia.seamless_login.internal

object SeamlessLoginConstant {

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

    const val RSA_ALGORITHM = "RSA"

    const val PARAMS_PAGE = "page"
    const val LAYOUT_FLOATING = "floating"

    const val REMOTE_CONFIG_FOR_HOME = "android_customer_sticky_login_home"
    const val REMOTE_CONFIG_FOR_PDP = "android_customer_sticky_login_pdp"
    const val REMOTE_CONFIG_FOR_SHOP = "android_customer_sticky_login_shop"
}