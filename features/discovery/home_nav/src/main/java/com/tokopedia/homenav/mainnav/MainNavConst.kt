package com.tokopedia.homenav.mainnav

object MainNavConst {
    object Section {
        const val PROFILE = 0
        const val BU_ICON = 1
        const val ORDER = 2
        const val USER_MENU = 3
        const val HOME = 4
        const val ACTIVITY = 5
    }

    object MainNavState{
        var runAnimation = true
    }

    object ImageUrl {
        const val CDN_URL = "https://images.tokopedia.net"
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
