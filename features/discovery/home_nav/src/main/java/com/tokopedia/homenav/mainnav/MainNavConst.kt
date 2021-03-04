package com.tokopedia.homenav.mainnav

object MainNavConst {
    object Section {
        const val PROFILE = 0
        const val BU_ICON = 1
        const val ORDER = 2
        const val USER_MENU = 3
        const val HOME = 4
    }

    object MainNavState{
        var runAnimation = true
    }

    object ImageUrl {
        const val CDN_URL = "https://ecs7.tokopedia.net"
        const val KEY_IMAGE_HOST = "image_host"
        const val OVO_IMG = "/img/android/ovo/drawable-xxxhdpi/ovo.png"
        const val TOKOCASH_IMG = "/img/wallet/ic_tokocash_circle.png";
        const val SALDO_IMG = "/img/android/saldo_tokopedia/drawable-xxxhdpi/saldo_tokopedia.png";
    }

    object RecentViewAb{
        const val EXP_NAME = "review_counter_home"
        const val CONTROL = "control_grey_counter"
        const val VARIANT = "variant_red_counter"
    }

}